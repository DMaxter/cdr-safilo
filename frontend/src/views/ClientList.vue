<template>
  <Container>
    <v-row justify="center" align="center" class="d-flex flex-column mt-2">
      <Message v-model="failure" message="Não foi possível obter a lista de clientes" />
      <v-data-table-virtual
        :headers="headers"
        :items="filteredClients"
        fixed-header
        hover
        item-key="id"
        items-per-page-text="Clientes por página:"
        no-data-text="Não existem clientes registados"
        height="500"
        items-per-page="50"
        style="max-width: 98%"
        class="elevation-1 my-header-style"
      >
        <template
          v-for="(header, i) in headers"
          v-slot:[`header.${header.key}`]="{ column, getSortIcon, toggleSort }"
        >
          <div class="v-data-table-header__content">
            <span>{{ header.title }}</span>
            <TableFilter
              v-if="header.searchable"
              :type="header.type ? header.type : undefined"
              :items="header.items ? header.items : undefined"
              :itemTitle="header.itemTitle ? header.itemTitle : undefined"
              :itemValue="header.itemValue ? header.itemValue : undefined"
              v-model="searchFilter[header.filterKey]"
              @filter="updateFilterURL"
            />
            <v-icon class="v-data-table-header__sort-icon" :icon="getSortIcon(column)"></v-icon>
          </div>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon @click="openClientInfo(item)" v-tooltip="'Ver cliente'">visibility</v-icon>
          <v-icon v-if="canEdit" @click="editClient(item)" v-tooltip="'Editar cliente'"
            >edit</v-icon
          >
          <v-icon v-if="canAnnotate" @click="showClientNote(item)" v-tooltip="'Nota do cliente'"
            >sticky_note_2</v-icon
          >
        </template>
      </v-data-table-virtual>
    </v-row>
    <template v-slot:actions v-if="canEdit">
      <v-row class="flex-row" justify="space-around" style="width: 90%">
        <v-btn class="mt-3 mb-3" v-if="canEdit" @click="upload">Carregar Clientes</v-btn>
        <v-btn class="mt-3 mb-3" @click="getRequests()">Descarregar pedidos</v-btn>
        <AddClient v-if="canEdit" @updated="refreshClients" />
      </v-row>
      <FileUpload
        v-if="canEdit"
        v-model="uploading"
        accept="text/csv"
        title="Carregar Clientes"
        @upload="importClients"
        :multiple="false"
      />
      <EditClient v-model="editing" :client="selectedClient" @updated="refreshClients" />
      <ClientNote v-model="annotating" :client="selectedClient" @updated="refreshClients" />
      <Message v-model="uploadSuccess" message="Clientes carregados com sucesso!" />
      <Message
        v-model="uploadFailure"
        message="Ocorreu um erro ao carregar o ficheiro de clientes"
      />
    </template>
  </Container>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useUserStore } from "@/stores/user";
import Backend from "@/router/backend";
import ClientDto from "@models/dto/ClientDto";

const route = useRoute();
const router = useRouter();
const failure = ref(false);

const user = useUserStore();
await user.init();

const ids = reactive([]);
const clients = reactive([]);
const banners = reactive([]);
const cities = reactive([]);

const searchValue = ref("");

const canAnnotate = user.isCdr() || user.isAdmin();

const canEdit = user.isSafilo() || user.isAdmin();
const editing: ClientDto = ref(false);

const annotating = ref(false);

const uploading = ref(false);
const uploadSuccess = ref(false);
const uploadFailure = ref(false);

const selectedClient = ref(new ClientDto());

await refreshClients();

const headers = [
  {
    title: "Código",
    align: "center",
    key: "id",
    value: "id",
    sortable: true,
    searchable: true,
    items: ids,
    filterKey: "id",
  },
  {
    title: "Banner",
    align: "center",
    key: "banner",
    value: "banner",
    sortable: true,
    searchable: true,
    items: banners,
    filterKey: "banner",
  },
  {
    title: "Nome",
    value: "name",
    align: "center",
    key: "name",
    sortable: true,
    searchable: true,
    items: clients,
    itemTitle: "name",
    itemValue: "id",
    filterKey: "name",
  },
  {
    title: "Cidade",
    value: "city",
    align: "center",
    key: "city",
    sortable: true,
    searchable: true,
    items: cities,
    filterKey: "city",
  },
  { value: "actions", align: "right", sortable: false },
];

const searchFilter = reactive({
  id: [],
  banner: [],
  name: [],
  city: [],
});

const filteredClients = computed(() =>
  clients.filter((c) => {
    let include = true;

    if (searchFilter["id"].length > 0 && !searchFilter["id"].includes(c.id)) {
      include = false;
    } else if (searchFilter["banner"].length > 0 && !searchFilter["banner"].includes(c.banner)) {
      include = false;
    } else if (searchFilter["name"].length > 0 && !searchFilter["name"].includes(c.name)) {
      include = false;
    } else if (searchFilter["city"].length > 0 && !searchFilter["city"].includes(c.city)) {
      include = false;
    }

    return include;
  }),
);

if (route.query.id) {
  searchFilter["id"] = [route.query.id].flat().map((id) => {
    try {
      return Number(id);
    } catch (e) {
      console.error("Invalid request id");
    }
  });
}
if (route.query.banner) {
  searchFilter["banner"] = [route.query.banner].flat();
}
if (route.query.name) {
  searchFilter["name"] = [route.query.name].flat();
}
if (route.query.city) {
  searchFilter["city"] = [route.query.city].flat();
}

async function refreshClients() {
  clients.length = 0;
  banners.length = 0;
  ids.length = 0;
  cities.length = 0;

  try {
    clients.unshift(...(await Backend.getClients()));
    banners.unshift(...new Set(clients.map((c) => c.banner)));
    ids.unshift(...clients.map((c) => c.id));
    cities.unshift(...new Set(clients.map((c) => c.city)));
  } catch (error) {
    console.error(error);
    throw Error(error);
  }
}

async function importClients(file) {
  try {
    await Backend.addClients(file);
    uploadSuccess.value = true;
    await refreshClients();
  } catch (error) {
    uploadFailure.value = true;
    console.error(error);
  }
}

function showClientNote(client: ClientDto) {
  selectedClient.value = client;
  annotating.value = true;
}

function openClientInfo(client) {
  router.push({ name: "client", query: { id: client.id } });
}

async function editClient(client: ClientDto) {
  selectedClient.value = client;
  editing.value = true;
}

async function getRequests() {
  try {
    await Backend.getAllRequests();
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}

function updateFilterURL() {
  let query = {};

  if (searchFilter["id"]) {
    query["id"] = searchFilter["id"];
  }
  if (searchFilter["banner"]) {
    query["banner"] = searchFilter["banner"];
  }
  if (searchFilter["name"]) {
    query["name"] = searchFilter["name"];
  }
  if (searchFilter["city"]) {
    query["city"] = searchFilter["city"];
  }

  router.push({ query: query });
}

function upload() {
  uploading.value = true;
}
</script>

<style lang="scss"></style>
