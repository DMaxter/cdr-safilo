<template>
  <Container>
    <P-DataTable
      v-model:filters="filters"
      paginator
      scrollable
      removable-sort
      class="request-data-table"
      scroll-height="flex"
      filter-display="row"
      sort-field="id"
      :sort-order="-1"
      :value="requestStore.requests"
      :rows="25"
      :rows-per-page-options="[10, 25, 50, 100]"
      table-style="table-layout: fixed; width: 100%"
      responsive-layout="scroll"
    >
      <template #empty>Não existem pedidos registados</template>

      <P-Column class="w-[10rem]" sortable field="id" header="ID" filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-InputText
            v-model="filterModel.value"
            fluid
            type="number"
            placeholder="ID"
            @input="filterCallback()"
          />
        </template>
        <template #body="{ data }">
          <div class="text-right">{{ data.id }}</div>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="status" header="Estado" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            v-model="filterModel.value"
            filter
            fluid
            :options="states"
            placeholder="Estado"
            option-label="name"
            option-value="value"
            @change="filterCallback()"
          />
        </template>
        <template #body="{ data }">
          <P-Tag rounded :class="getStatusClass(data.status)">
            <Icon :icon="getStatusIcon(data.status)" />
            <span>{{ states.find((s) => s.value === data.status)?.name }}</span>
          </P-Tag>
        </template>
      </P-Column>
      <P-Column
        class="w-[15rem]"
        field="client.name"
        filter-field="client.id"
        header="Cliente"
        sortable
        filter
      >
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            v-model="filterModel.value"
            filter
            fluid
            :options="clients"
            option-label="name"
            option-value="id"
            placeholder="Cliente"
            @change="filterCallback()"
          />
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="brand.name" header="Marca" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            v-model="filterModel.value"
            filter
            fluid
            :options="brands"
            placeholder="Marca"
            @change="filterCallback()"
          />
        </template>
        <template #body="{ data }">
          <span>{{ data.brand?.name }}</span>
        </template>
      </P-Column>
      <P-Column class="w-[12rem]" field="materialNames" header="Material" filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            v-model="filterModel.value"
            filter
            fluid
            :options="materials"
            placeholder="Material"
            @change="filterCallback()"
          />
        </template>
        <template #body="{ data }">
          <span>{{ data.materialNames.join(", ") }}</span>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="user" header="Comercial" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            v-model="filterModel.value"
            filter
            fluid
            :options="commercialsFilterOptions"
            placeholder="Comercial"
            @change="filterCallback()"
          />
        </template>
      </P-Column>
      <P-Column class="w-[15rem]" field="created" header="Data de Criação" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-DatePicker
            v-model="filterModel.value"
            fluid
            selection-mode="range"
            :manual-input="false"
            date-format="dd/mm/yy"
            placeholder="Data de Criação"
            :show-button-bar="true"
            @date-select="filterCallback()"
            @hide="filterCallback()"
          />
        </template>
        <template #body="{ data }">
          <span>
            {{ new Date(data.created).toLocaleString("pt-PT") }}
          </span>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]">
        <template #body="{ data }">
          <Icon v-tooltip="'Ver resumo'" icon="visibility" @click="showSummary(data)" />
          <Icon v-tooltip="'Ver detalhes'" icon="open_in_new" @click="openDetails(data)" />
          <Icon v-if="canEdit(data)" v-tooltip="'Editar'" icon="edit" @click="editRequest(data)" />
          <Icon
            v-if="canCancel(data)"
            v-tooltip="'Cancelar'"
            icon="cancel"
            @click="confirmCancel(data)"
          />
        </template>
      </P-Column>
    </P-DataTable>
    <RequestSummary v-model="summary" :request="selectedRequest" @opened="onWaybillOpened" />
  </Container>
</template>

<script lang="ts" setup>
import { FilterMatchMode, FilterService } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter, type LocationQueryRaw } from "vue-router";

import { statusItems } from "@/maps";
import { useRequestStore } from "@stores/requests";
import { useRequestAccess } from "@/composables/useRequestAccess";
import { Client } from "@router/backend/services/client/types";
import { Request, Status } from "@router/backend/services/request/types";
import { getStatusIcon, getStatusClass } from "@/utils";

// Custom match mode: a request matches when any of its slot materials is selected.
FilterService.register("materialIn", (value: string[] | undefined, filter: string[] | null) => {
  if (!filter || filter.length === 0) return true;
  return filter.some((name) => value?.includes(name));
});

const confirm = useConfirm();
const toast = useToast();

const requestStore = useRequestStore();
const { canCancel, canEdit } = useRequestAccess();

const route = useRoute();
const router = useRouter();

const TITLE = "Cancelamento de Pedido";

// Filter options are derived straight from the loaded requests (like ClientList's
// banner options), so they are never empty when returning to this screen.
const commercialsFilterOptions = computed(() =>
  Array.from(new Set(requestStore.requests.map((r) => r.user!))),
);
const clients = computed(() => {
  const uniqueClients = new Map<number | string, Client>();
  requestStore.requests.forEach((r) => {
    if (r.client?.id && !uniqueClients.has(r.client.id)) {
      uniqueClients.set(r.client.id, r.client);
    }
  });
  return Array.from(uniqueClients.values());
});
const brands = computed(() =>
  Array.from(new Set(requestStore.requests.map((r) => r.brand?.name ?? ""))).filter(
    (b) => b !== "",
  ),
);
const materials = computed(() => {
  const names = new Set<string>();
  requestStore.requests.forEach((r) => {
    r.getMaterials().forEach((m) => {
      if (m) names.add(m);
    });
  });
  return Array.from(names);
});

onMounted(async () => {
  await refreshRequests();
});

const selectedRequestId = ref<number>(0);
const selectedRequest = computed(() => {
  return requestStore.requests.find((r) => r.id === selectedRequestId.value) || new Request();
});

const summary = ref(false);

const states = statusItems;

const filters = ref<Record<string, { value: unknown; matchMode: string }>>({
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  status: { value: null, matchMode: FilterMatchMode.IN },
  "client.id": { value: null, matchMode: FilterMatchMode.IN },
  user: { value: null, matchMode: FilterMatchMode.IN },
  created: { value: null, matchMode: "between" },
  "brand.name": { value: null, matchMode: FilterMatchMode.IN },
  materialNames: { value: null, matchMode: "materialIn" },
});

// Initialize filters from the URL (?id=, ?status=, ?client=, ?commercial=, ?creationDate=, ?brand=, ?material=)
if (route.query.id) {
  filters.value.id.value = Array.isArray(route.query.id)
    ? Number(route.query.id[0])
    : Number(route.query.id);
}
if (route.query.status) {
  filters.value.status.value = ([route.query.status].flat() as string[]).filter((s) =>
    Object.values(Status).includes(s as Status),
  );
}
if (route.query.client) {
  filters.value["client.id"].value = ([route.query.client].flat() as string[]).map(Number);
}
if (route.query.commercial) {
  filters.value.user.value = [route.query.commercial].flat() as string[];
}
if (route.query.creationDate) {
  const dates = ([route.query.creationDate].flat() as string[]).map((d) => new Date(d));
  if (dates.every((d) => !isNaN(d.getTime()))) {
    filters.value.created.value = dates;
  }
}
if (route.query.brand) {
  filters.value["brand.name"].value = [route.query.brand].flat() as string[];
}
if (route.query.material) {
  filters.value["materialNames"].value = [route.query.material].flat() as string[];
}

async function refreshRequests() {
  // Skips the backend call when the store already holds the requests.
  const response = await requestStore.getAllRequests();
  if (!response.success) {
    console.error("Failed to retrieve requests:", response.content);
  }
}

function showSummary(item: Request) {
  selectedRequestId.value = item.id;
  summary.value = true;
}

function openDetails(item: Request) {
  router.push({ name: "request", params: { id: item.id } });
}

async function onWaybillOpened() {
  // Waybill/cancel actions change the request server-side, so force a re-sync.
  await requestStore.getAllRequests(true);
}

async function cancelRequest() {
  const response = await requestStore.cancelRequest(selectedRequest.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Pedido cancelado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao cancelar o pedido",
      life: 10000,
    });
    console.error(response);
  }
}

function confirmCancel(request: Request) {
  selectedRequestId.value = request.id;

  confirm.require({
    message: `Tem a certeza que pretende cancelar o pedido ${selectedRequest.value.id} efetuado por ${selectedRequest.value.user ?? "Desconhecido"} para o cliente ${selectedRequest.value.client?.name ?? "Desconhecido"}?`,
    header: "Confirmar cancelamento de pedido",
    rejectProps: {
      label: "Abortar cancelamento",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Confirmar cancelamento",
      severity: "danger",
    },
    accept: cancelRequest,
  });
}

// Map filter keys to the URL query params the parse block above expects.
const URL_KEYS: Record<string, string> = {
  id: "id",
  status: "status",
  "client.id": "client",
  user: "commercial",
  created: "creationDate",
  "brand.name": "brand",
  materialNames: "material",
};

function updateFilterURL() {
  const query: LocationQueryRaw = {};

  for (const key in filters.value) {
    const filter = (filters.value as Record<string, { value: unknown; matchMode: string }>)[key];
    if (
      filter.value !== null &&
      filter.value !== "" &&
      (!Array.isArray(filter.value) || filter.value.length > 0)
    ) {
      if (key === "created" && Array.isArray(filter.value)) {
        // filter.value is an array of two Dates [startDate, endDate] — send ISO strings.
        query[URL_KEYS[key]] = (filter.value as Date[]).map((d) => d.toISOString());
      } else if (Array.isArray(filter.value)) {
        query[URL_KEYS[key]] = filter.value.map(String);
      } else {
        query[URL_KEYS[key]] = String(filter.value);
      }
    }
  }

  router.replace({ query: query });
}

watch(filters, () => updateFilterURL(), { deep: true });

function editRequest(item: Request) {
  router.push({ name: "order", query: { id: item.id } });
}
</script>

<style lang="scss" scoped>
.request-data-table {
  max-height: calc(100vh - 300px);
  display: flex;
  flex-direction: column;
}
</style>
