<template>
  <Container>
    <div style="height: 90%">
      <P-DataTable
        paginator
        scrollable
        removableSort
        class="max-w-98/100"
        scrollHeight="flex"
        filterDisplay="row"
        :value="clientStore.clients"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
        <!--FIXME: More than 20 items makes actions go off the page -->

        <template #header>
          <div class="flex justify-end">
            <P-IconField>
              <P-InputIcon>
                <Icon icon="search" />
              </P-InputIcon>
              <P-InputText v-model="filters['global'].value" placeholder="Procurar" />
            </P-IconField>
          </div>
        </template>

        <P-Column sortable field="id" header="Código">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Código" />
          </template>
        </P-Column>
        <P-Column sortable field="banner" header="Banner">
          <template #filter="{ filterModel, filterCallback }">
            <P-MultiSelect filter v-model="filterModel.value" @change="filterCallback()" :options="banners" placeholder="Banner" />
          </template>
        </P-Column>
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Nome" />
          </template>
        </P-Column>
        <P-Column sortable field="city" header="Cidade">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Cidade" />
          </template>
        </P-Column>
        <!--<template v-slot:item.actions="{ item }">
          <v-icon @click="openClientInfo(item)" v-tooltip="'Ver cliente'">visibility</v-icon>
          <v-icon v-if="canEdit" @click="editClient(item)" v-tooltip="'Editar cliente'"
            >edit</v-icon
          >
          <v-icon v-if="canAnnotate" @click="showClientNote(item)" v-tooltip="'Nota do cliente'"
            >sticky_note_2</v-icon
          >
        </template>-->
      </P-DataTable>
    </div>
    <template #actions>
      <P-Button @click="refresh">Atualizar</P-Button>
      <P-Button v-if="canEdit" @click="upload">Carregar Clientes</P-Button>
      <P-Button @click="getRequests()">Descarregar pedidos</P-Button>
        <!--<AddClient v-if="canEdit" @updated="refreshClients" />
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
      />-->
    </template>
  </Container>
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";
import { useClientStore } from "@stores/clients";

import Backend from "@/router/backend_old";
import { Client } from "@router/backend/services/client/types";

const route = useRoute();
const router = useRouter();
const failure = ref(false);

const TITLE = "Lista de Clientes";

const authStore = useAuthStore();
const clientStore = useClientStore();
const toast = useToast();

const ids = reactive([]); // TODO: REMOVE
const banners = computed(() => [...new Set(clientStore.clients.map((c) => c.banner))]);
const cities = reactive([]); // TODO: REMOVE

const searchValue = ref(""); // TODO: REMOVE

const canAnnotate = authStore.isCdr() || authStore.isAdmin();

const canEdit = authStore.isSafilo() || authStore.isAdmin();
const editing = ref<Client | null>(null);

const annotating = ref(false);

const uploading = ref(false);
const uploadSuccess = ref(false);
const uploadFailure = ref(false);

const selectedClient = ref<Client>(new Client());

onMounted(async () => {
  await refresh();
});

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  banner: { value: null, matchMode: FilterMatchMode.IN },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  city: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

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

// FIXME: UPDATE TO USE NEW VALUES
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

async function refresh() {
  const response = await clientStore.getClients();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de clientes",
      life: 10000,
    });
  }
}

// TODO: REMOVE
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

function showClientNote(client: Client) {
  selectedClient.value = client;
  annotating.value = true;
}

function openClientInfo(client) {
  router.push({ name: "client", query: { id: client.id } });
}

async function editClient(client: Client) {
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
