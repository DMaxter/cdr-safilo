<template>
  <Container>
    <P-DataTable
      paginator
      scrollable
      removableSort
      class="request-data-table"
      scrollHeight="flex"
      filterDisplay="row"
      sortField="id"
      :sortOrder="-1"
      :value="requestStore.requests"
      :rows="25"
      :rowsPerPageOptions="[10, 25, 50, 100]"
      v-model:filters="filters"
      tableStyle="table-layout: fixed; width: 100%"
      responsiveLayout="scroll"
    >
      <template #empty>Não existem pedidos registados</template>

      <P-Column class="w-[10rem]" sortable field="id" header="ID" filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-InputText
            fluid
            v-model="filterModel.value"
            type="number"
            @input="filterCallback()"
            placeholder="ID"
          />
        </template>
        <template #body="{ data }">
          <div class="text-right">{{ data.id }}</div>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="status" header="Estado" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            filter
            fluid
            v-model="filterModel.value"
            @change="filterCallback()"
            :options="states"
            placeholder="Estado"
            optionLabel="name"
            optionValue="value"
          />
        </template>
        <template #body="{ data }">
          <P-Tag rounded :class="getStatusClass(data.status)">
            <Icon :icon="getStatusIcon(data.status)" />
            <span>{{ states.find((s) => s.value === data.status)?.name }}</span>
          </P-Tag>
        </template>
      </P-Column>
      <P-Column class="w-[15rem]" field="client.name" header="Cliente" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            filter
            fluid
            v-model="filterModel.value"
            @change="filterCallback()"
            :options="clients"
            optionLabel="name"
            optionValue="id"
            placeholder="Cliente"
          />
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="user" header="Comercial" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect
            filter
            fluid
            v-model="filterModel.value"
            @change="filterCallback()"
            :options="commercialsFilterOptions"
            placeholder="Comercial"
          />
        </template>
      </P-Column>
      <P-Column class="w-[15rem]" field="created" header="Data de Criação" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-DatePicker
            fluid
            v-model="filterModel.value"
            @date-select="filterCallback()"
            @hide="filterCallback()"
            selectionMode="range"
            :manualInput="false"
            dateFormat="dd/mm/yy"
            placeholder="Data de Criação"
            :showButtonBar="true"
          />
        </template>
        <template #body="{ data }">
          <span>
            {{ new Date(data.created).toLocaleString("pt-PT") }}
          </span>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]" field="cost" header="Custo" sortable filter>
        <template #filter="{ filterModel, filterCallback }">
          <P-InputText
            fluid
            v-model="filterModel.value"
            type="number"
            @input="filterCallback()"
            placeholder="Custo"
          />
        </template>
        <template #body="{ data }">
          <div class="text-right">{{ data.cost.toFixed(2) }}</div>
        </template>
      </P-Column>
      <P-Column class="w-[10rem]">
        <template #body="{ data }">
          <Icon icon="visibility" @click="showSummary(data)" v-tooltip="'Ver resumo'" />
          <Icon icon="open_in_new" @click="console.error('TODO')" v-tooltip="'Ver detalhes'" />
          <!-- TODO: Implement edit -->
          <Icon
            v-if="
              (canManipulate ||
                (authStore.isCommercial() && data.user == authStore.logged?.name)) &&
              data.status!! === Status.Ordered
            "
            icon="edit"
            @click="editRequest(data)"
            v-tooltip="'Editar'"
          />
          <Icon
            v-if="
              (canManipulate ||
                (authStore.isCommercial() && data.user == authStore.logged?.name)) &&
              data.status!! === Status.Ordered
            "
            icon="cancel"
            @click="confirmCancel(data)"
            v-tooltip="'Cancelar'"
          />
        </template>
      </P-Column>
    </P-DataTable>
    <RequestSummary v-model="summary" :request="selectedRequest" @opened="onWaybillOpened" />
  </Container>
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, reactive, ref, type Ref } from "vue";
import { useRoute, useRouter, type LocationQueryRaw } from "vue-router";

import { statusItems } from "@/maps";
import { useAuthStore } from "@stores/auth";
import { useRequestStore } from "@stores/requests";
import { Client } from "@router/backend/services/client/types";
import { Request, Status } from "@router/backend/services/request/types";
import { getStatusIcon, getStatusClass } from "@/utils";

const confirm = useConfirm();
const toast = useToast();

const authStore = useAuthStore();
const requestStore = useRequestStore();

const route = useRoute();
const router = useRouter();

const TITLE = "Cancelamento de Pedido";

const canManipulate = authStore.isSafilo() || authStore.isCdr() || authStore.isAdmin();

const clients: Ref<Client[]> = ref([]);
const commercialsFilterOptions: Ref<string[]> = ref([]);

onMounted(async () => {
  await refreshRequests();
});

const selectedRequestId = ref<number>(0);
const selectedRequest = computed(() => {
  return requestStore.requests.find((r) => r.id === selectedRequestId.value) || new Request();
});

const summary = ref(false);

const states = statusItems;

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  status: { value: null, matchMode: FilterMatchMode.IN },
  "client.name": { value: null, matchMode: FilterMatchMode.IN },
  user: { value: null, matchMode: FilterMatchMode.IN },
  created: { value: null, matchMode: FilterMatchMode.DATE_BETWEEN },
  cost: { value: null, matchMode: FilterMatchMode.EQUALS },
});

// FIXME:
// Parse route path and initialize filters
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
  filters.value["client.name"].value = ([route.query.client].flat() as string[]).map(Number);
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
if (route.query.cost) {
  filters.value.cost.value = Array.isArray(route.query.cost)
    ? Number(route.query.cost[0])
    : Number(route.query.cost);
}
// END FIXME:

async function refreshRequests() {
  commercialsFilterOptions.value = [];
  clients.value = [];

  const response = await requestStore.getAllRequests();
  if (!response.success) {
    console.error("Failed to retrieve requests:", response.content);
    return;
  }

  const currentRequests = requestStore.requests;

  // Populate commercial filter options
  const uniqueCommercials = new Set(currentRequests.map((r) => r.user!!));
  commercialsFilterOptions.value = Array.from(uniqueCommercials);

  // Populate client filter options
  const uniqueClients = new Map<number | string, Client>();
  currentRequests.forEach((r) => {
    if (r.client?.id && !uniqueClients.has(r.client.id)) {
      uniqueClients.set(r.client.id, r.client);
    }
  });
  clients.value = Array.from(uniqueClients.values());
}

function showSummary(item: Request) {
  selectedRequestId.value = item.id;
  summary.value = true;
}

async function onWaybillOpened() {
  await requestStore.getAllRequests();
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
    message: `Tem a certeza que pretende cancelar o pedido ${selectedRequest.value.id} efetuado por ${selectedRequest.value.user} para o cliente ${selectedRequest.value.client.name}?`,
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

// TODO: FIXME
function updateFilterURL() {
  let query: LocationQueryRaw = {};

  for (const key in filters.value) {
    const filter = filters.value[key];
    if (
      filter.value !== null &&
      filter.value !== "" &&
      (!Array.isArray(filter.value) || filter.value.length > 0)
    ) {
      if (key === "created" && Array.isArray(filter.value)) {
        // filter.value will be an array of two Dates [startDate, endDate]
        // Convert to ISO string for URL
        query[key] = filter.value.map((d) => d.toISOString());
      } else if (key === "status" && Array.isArray(filter.value)) {
        query[key] = filter.value.map((s: Status) => s.toString());
      } else {
        query[key] = filter.value;
      }
    }
  }
  router.push({ query: query });
}

// TODO: Implement
function editRequest(item: Request) {}
</script>

<style lang="scss" scoped>
.request-data-table {
  max-height: calc(100vh - 300px);
  display: flex;
  flex-direction: column;
}
</style>
