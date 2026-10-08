<template>
  <P-DataTable
    v-model:filters="filters"
    :value="clientStore.clients"
    :loading="loading"
    :row-class="rowClass"
    data-key="id"
    paginator
    :rows="10"
    :rows-per-page-options="[10, 25, 50]"
    scrollable
    scroll-height="flex"
    removable-sort
    sort-field="name"
    :sort-order="1"
    table-style="table-layout: fixed; width: 100%"
    class="client-picker-table"
    @row-click="onRowClick"
  >
    <template #header>
      <div class="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <P-IconField class="w-full sm:w-96">
          <P-InputIcon>
            <Icon icon="search" />
          </P-InputIcon>
          <P-InputText
            v-model="filters.global.value"
            placeholder="Procurar por nome, cidade, código postal ou morada"
            class="w-full"
          />
        </P-IconField>
        <span class="hidden text-sm text-gray-500 sm:inline">
          {{ clientStore.clients.length }} cliente(s)
        </span>
      </div>
    </template>

    <template #empty>
      <div class="py-6 text-center text-gray-500">Nenhum cliente corresponde à pesquisa</div>
    </template>

    <P-Column field="id" header="Código" sortable class="w-16 sm:w-24">
      <template #body="{ data }">
        <div class="text-right">{{ data.id }}</div>
      </template>
    </P-Column>

    <P-Column field="name" header="Nome" sortable>
      <template #body="{ data }">
        <div class="font-medium">{{ data.name }}</div>
        <div class="text-xs text-gray-500 md:hidden">
          {{ data.address }} — {{ data.postalCode }} {{ data.city }}
        </div>
      </template>
    </P-Column>

    <P-Column field="address" header="Morada" sortable class="hidden md:table-cell" />

    <P-Column field="postalCode" header="Cód. Postal" sortable class="hidden lg:table-cell" />

    <P-Column field="city" header="Cidade" sortable class="w-24 sm:w-40" />

    <P-Column class="w-14">
      <template #body="{ data }">
        <Icon
          v-tooltip="isPicked(data) ? 'Cliente selecionado' : 'Selecionar cliente'"
          :icon="isPicked(data) ? 'check_circle' : 'add_circle'"
          :class="isPicked(data) ? 'text-primary' : 'text-gray-400 hover:text-primary'"
          class="cursor-pointer"
          @click.stop="select(data)"
        />
      </template>
    </P-Column>
  </P-DataTable>
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import type { Client } from "@router/backend/services/client/types";
import { useClientStore } from "@stores/clients";

const TITLE = "Selecionar Cliente";

const selectedClient = defineModel<Client | null>();

const toast = useToast();
const clientStore = useClientStore();

const loading = ref(false);

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  if (clientStore.clients.length > 0) {
    return;
  }

  loading.value = true;

  const response = await clientStore.getClients();

  loading.value = false;

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de clientes",
      life: 10000,
    });
  }
});

function isPicked(client: Client) {
  return client.id === selectedClient.value?.id;
}

function rowClass(client: Client) {
  return isPicked(client) ? "client-row-picked" : "";
}

function select(client: Client) {
  selectedClient.value = client;
}

function onRowClick(event: { data: Client }) {
  select(event.data);
}
</script>

<style lang="scss" scoped>
.client-picker-table {
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 380px);
  max-height: calc(100dvh - 380px);
}

@media (min-width: 640px) {
  .client-picker-table {
    max-height: calc(100vh - 300px);
  }
}

.client-picker-table :deep(tr.client-row-picked > td) {
  background-color: var(--p-primary-50) !important;
}
</style>
