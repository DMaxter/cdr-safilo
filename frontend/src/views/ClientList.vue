<template>
  <Container>
    <div class="h-85/100">
      <P-DataTable
        paginator
        scrollable
        removableSort
        class="client-data-table"
        scrollHeight="flex"
        filterDisplay="row"
        :value="clientStore.clients"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
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
              optionLabel="label"
              optionValue="value"
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
      <P-Button
        v-if="canManage"
        :loading="exporting"
        :disabled="exporting"
        @click="downloadRequests"
        >Descarregar pedidos</P-Button
      >
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
import { FilterMatchMode, FilterService } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter, type LocationQueryRaw } from "vue-router";

import { Client } from "@router/backend/services/client/types";
import { useAuthStore } from "@stores/auth";
import { useClientStore } from "@stores/clients";
import { useRequestStore } from "@stores/requests";
import { ManageMode } from "@/utils";

FilterService.register("bannerIn", (value: string | null | undefined, filter: string[] | null) => {
  if (!filter || filter.length === 0) return true;
  return filter.includes(value ?? "");
});

const route = useRoute();
const router = useRouter();

const TITLE = "Lista de Clientes";
const IMPORT_TITLE = "Importação de Clientes";
const EXPORT_TITLE = "Exportação de Pedidos";

const authStore = useAuthStore();
const clientStore = useClientStore();
const requestStore = useRequestStore();
const toast = useToast();

const banners = computed(() => {
  const unique = new Set(clientStore.clients.map((c) => c.banner ?? ""));
  const options: { label: string; value: string }[] = [];

  if (unique.has("")) {
    options.push({ label: "Sem banner", value: "" });
  }

  for (const banner of unique) {
    if (banner !== "") {
      options.push({ label: banner, value: banner });
    }
  }

  return options;
});

const canAnnotate = authStore.isCdr() || authStore.isAdmin();

const canManage = authStore.isSafilo() || authStore.isAdmin();
const manageMode = ref<ManageMode>(ManageMode.None);

const annotating = ref(false);

const uploading = ref(false);
const exporting = ref(false);

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

async function downloadRequests() {
  exporting.value = true;
  const response = await requestStore.exportRequests();
  exporting.value = false;

  if (response.success) {
    const url = window.URL.createObjectURL(response.content as Blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "pedidos.csv";
    link.click();
    window.URL.revokeObjectURL(url);

    toast.add({
      severity: "success",
      summary: EXPORT_TITLE,
      detail: "Pedidos descarregados com sucesso",
      life: 5000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: EXPORT_TITLE,
      detail: response.content as string,
      life: 10000,
    });
    console.error(response);
  }
}

const filters = ref({
  global: { value: null as string | null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null as string | null, matchMode: FilterMatchMode.STARTS_WITH },
  banner: { value: null as string[] | null, matchMode: "bannerIn" },
  name: { value: null as string | null, matchMode: FilterMatchMode.STARTS_WITH },
  city: { value: null as string | null, matchMode: FilterMatchMode.STARTS_WITH },
});

async function addClient() {
  selectedClient.value = new Client();
  manageMode.value = ManageMode.Add;
}

// Initialize table filters from the URL (?id=, ?banner=, ?name=, ?city=)
if (route.query.id) {
  filters.value.id.value = [route.query.id].flat()[0];
}
if (route.query.banner) {
  filters.value.banner.value = [route.query.banner].flat().filter((v): v is string => v !== null);
}
if (route.query.name) {
  filters.value.name.value = [route.query.name].flat()[0];
}
if (route.query.city) {
  filters.value.city.value = [route.query.city].flat()[0];
}

function updateFilterURL() {
  let query: LocationQueryRaw = {};

  const entries: Array<[string, string | string[] | null]> = [
    ["id", filters.value.id.value],
    ["banner", filters.value.banner.value],
    ["name", filters.value.name.value],
    ["city", filters.value.city.value],
  ];

  for (const [key, value] of entries) {
    if (value === null || value === "" || (Array.isArray(value) && value.length === 0)) continue;
    query[key] = Array.isArray(value) ? value : String(value);
  }

  router.replace({ query: query });
}

watch(filters, () => updateFilterURL(), { deep: true });

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

<style lang="scss">
.client-data-table {
  max-height: calc(100vh - 300px);
  display: flex;
  flex-direction: column;
}
</style>
