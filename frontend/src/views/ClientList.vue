<template>
  <Container>
    <div class="h-85/100">
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
        <!--FIXME: More than 10 items makes actions go off the page -->
        <template #empty>Não existem clientes registados</template>
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
            <P-InputText
              v-model="filterModel.value"
              @input="filterCallback()"
              placeholder="Código"
            />
          </template>
        </P-Column>
        <P-Column sortable field="banner" header="Banner">
          <template #filter="{ filterModel, filterCallback }">
            <P-MultiSelect
              filter
              v-model="filterModel.value"
              @change="filterCallback()"
              :options="banners"
              placeholder="Banner"
            />
          </template>
        </P-Column>
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Nome" />
          </template>
        </P-Column>
        <P-Column sortable field="city" header="Cidade">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText
              v-model="filterModel.value"
              @input="filterCallback()"
              placeholder="Cidade"
            />
          </template>
        </P-Column>
        <P-Column>
          <template #body="{ data }">
            <Icon icon="visibility" @click="openClientInfo(data)" v-tooltip="'Ver cliente'" />
            <Icon
              v-if="canManage"
              icon="edit"
              @click="editClient(data)"
              v-tooltip="'Editar cliente'"
            />
            <Icon
              v-if="canAnnotate"
              icon="sticky_note_2"
              @click="showClientNote(data)"
              v-tooltip="'Nota do cliente'"
            />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #actions>
      <P-Button @click="refresh">Atualizar</P-Button>
      <P-Button v-if="canManage" @click="addClient">Adicionar Cliente</P-Button>
      <P-Button v-if="canManage" @click="showUpload">Carregar Clientes</P-Button>
      <P-Button @click="getRequests()">Descarregar pedidos</P-Button>
      <FileUpload
        v-if="canManage"
        v-model="uploading"
        accept="text/csv"
        title="Carregar Clientes"
        :multiple="false"
        :maxFiles="1"
        :uploader="importClients"
      />
    </template>
    <ClientManagement v-model="manageMode" :client="selectedClient" />
    <ClientNote v-model="annotating" :client="selectedClient" />
  </Container>
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { Client } from "@router/backend/services/client/types";
import { useAuthStore } from "@stores/auth";
import { useClientStore } from "@stores/clients";
import { ManageMode } from "@/utils";

const route = useRoute();
const router = useRouter();
const failure = ref(false);

const TITLE = "Lista de Clientes";
const IMPORT_TITLE = "Importação de Clientes";

const authStore = useAuthStore();
const clientStore = useClientStore();
const toast = useToast();

const ids = reactive([]); // TODO: REMOVE
const banners = computed(() => [...new Set(clientStore.clients.map((c) => c.banner))]);
const cities = reactive([]); // TODO: REMOVE

const searchValue = ref(""); // TODO: REMOVE

const canAnnotate = authStore.isCdr() || authStore.isAdmin();

const canManage = authStore.isSafilo() || authStore.isAdmin();
const manageMode = ref<ManageMode>(ManageMode.None);

const annotating = ref(false);

const uploading = ref(false);

const selectedClient = ref<Client>(new Client());

onMounted(async () => {
  await refresh();
});

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

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  banner: { value: null, matchMode: FilterMatchMode.IN },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  city: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
});

async function addClient() {
  selectedClient.value = new Client();
  manageMode.value = ManageMode.Add;
}

// FIXME: UPDATE TO USE NEW VALUES
const searchFilter = reactive({
  id: [],
  banner: [],
  name: [],
  city: [],
});

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
// END FIXME:

async function importClients(file: File) {
  const response = await clientStore.importClients(file);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: IMPORT_TITLE,
      detail: "Clientes importados com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: IMPORT_TITLE,
      detail: "Ocorreu um erro ao carregar o ficheiro de clientes",
      life: 10000,
    });
    console.error(response);
  }
}

function showClientNote(client: Client) {
  selectedClient.value = client;
  annotating.value = true;
}

function openClientInfo(client: Client) {
  router.push({ name: "client", query: { id: client.id } });
}

async function editClient(client: Client) {
  selectedClient.value = client;
  manageMode.value = ManageMode.Edit;
}

function showUpload() {
  uploading.value = true;
}
</script>

<style lang="scss"></style>
