<template>
  <P-Dialog modal v-model:visible="enabled" class="w-8/10 max-w-[1000px]" fluid>
    <template #header>Preços</template>
    <div class="h-85/100">
      <P-DataTable
        paginator
        removableSort
        filterDisplay="row"
        :value="priceStore.prices"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
        <template #empty>Não existem preços registados</template>

        <P-Column sortable field="id" header="ID" style="width: 60px" />
        <P-Column sortable field="material" header="Material">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="ID" />
          </template>
          <template #body="{ data }">
            {{ materialName(data.material) }}
          </template>
        </P-Column>
        <P-Column sortable field="costPerSquareMeter" header="Preço por m²" style="width: 140px">
          <template #body="{ data }">
            <div class="text-right">{{ data.costPerSquareMeter.toFixed(2) }}€</div>
          </template>
        </P-Column>
        <P-Column sortable field="fixedCost" header="Custo fixo" style="width: 120px">
          <template #body="{ data }">
            <div class="text-right">{{ data.fixedCost.toFixed(2) }}€</div>
          </template>
        </P-Column>
        <P-Column field="finishings" header="Acabamentos">
          <template #body="{ data }">
            <span v-if="data.finishings.length === 0">Sem acabamentos</span>
            <div v-else class="flex flex-wrap gap-1">
              <P-Tag v-for="finishing in data.finishings" :key="finishing.id" severity="secondary">
                {{ finishing.name }}
              </P-Tag>
            </div>
          </template>
        </P-Column>
        <P-Column>
          <template #body="{ data }">
            <Icon
              icon="edit"
              @click="openPriceManagement(data, ManageMode.Edit)"
              v-tooltip="'Editar preço'"
            />
            <Icon icon="delete" @click="confirmDeletion(data)" v-tooltip="'Eliminar preço'" />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="openPriceManagement(new Price(), ManageMode.Add)">Adicionar</P-Button>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <PriceManagement v-model="manageMode" :price="selectedPrice" />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { Price } from "@router/backend/services/price/types";
import { usePriceStore } from "@stores/prices";
import { useMaterialStore } from "@stores/materials";
import { ManageMode } from "@/utils";

const TITLE = "Lista de Preços";

const enabled = defineModel<boolean>();
const priceStore = usePriceStore();
const materialStore = useMaterialStore();
const confirm = useConfirm();
const toast = useToast();

const selectedPrice = ref<Price>(new Price());

const manageMode = ref<ManageMode>(ManageMode.None);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  material: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

function materialName(id: number) {
  return materialStore.materials.find((m) => m.id === id)?.name ?? `Desconhecido (${id})`;
}

async function refresh() {
  const response = await priceStore.getPrices();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de preços",
      life: 10000,
    });
  }

  await materialStore.getMaterials();
}

async function deletePrice() {
  const response = await priceStore.deletePrice(selectedPrice.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Preço eliminado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao remover o preço",
      life: 10000,
    });
    console.error(response);
  }
}

function openPriceManagement(price: Price, mode: ManageMode) {
  selectedPrice.value = price;
  manageMode.value = mode;
}

function confirmDeletion(price: Price) {
  selectedPrice.value = price;

  confirm.require({
    message: `Tem a certeza que pretende eliminar o preço #${selectedPrice.value.id} do material '${materialName(selectedPrice.value.material)}'?`,
    header: "Confirmar remoção de preço",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Eliminar",
      severity: "danger",
    },
    accept: deletePrice,
  });
}

function close() {
  enabled.value = false;
}
</script>
