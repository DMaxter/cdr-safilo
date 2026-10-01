<template>
  <P-Dialog modal v-model:visible="enabled" class="w-8/10 max-w-[1000px]" fluid>
    <template #header>Acabamentos</template>
    <div class="h-85/100">
      <P-DataTable
        paginator
        removableSort
        filterDisplay="row"
        :value="finishingStore.finishings"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
        <template #empty>Não existem acabamentos registados</template>

        <P-Column sortable field="id" header="ID" style="width: 60px" />
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Nome" />
          </template>
        </P-Column>
        <P-Column sortable field="obsolete" header="Estado" style="width: 120px">
          <template #body="{ data }">
            <P-Tag v-if="data.obsolete" severity="danger" value="Obsoleto" />
            <P-Tag v-else severity="success" value="Ativo" />
          </template>
        </P-Column>
        <P-Column>
          <template #body="{ data }">
            <Icon
              v-if="!data.obsolete"
              icon="archive"
              @click="confirmObsolete(data)"
              v-tooltip="'Marcar como obsoleto'"
            />
            <Icon
              icon="edit"
              @click="openFinishingManagement(data, ManageMode.Edit)"
              v-tooltip="'Editar acabamento'"
            />
            <Icon icon="delete" @click="confirmDeletion(data)" v-tooltip="'Eliminar acabamento'" />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="openFinishingManagement(new Finishing(), ManageMode.Add)"
        >Adicionar</P-Button
      >
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <FinishingManagement v-model="manageMode" :finishing="selectedFinishing" />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { Finishing } from "@router/backend/services/finishing/types";
import { useFinishingStore } from "@stores/finishings";
import { ManageMode } from "@/utils";

const TITLE = "Lista de Acabamentos";

const enabled = defineModel<boolean>();
const finishingStore = useFinishingStore();
const confirm = useConfirm();
const toast = useToast();

const selectedFinishing = ref<Finishing>(new Finishing());

const manageMode = ref<ManageMode>(ManageMode.None);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

async function refresh() {
  const response = await finishingStore.getFinishings();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de acabamentos",
      life: 10000,
    });
  }
}

async function obsoleteFinishing() {
  const response = await finishingStore.makeFinishingObsolete(selectedFinishing.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Acabamento marcado como obsoleto",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao marcar o acabamento como obsoleto",
      life: 10000,
    });
    console.error(response);
  }
}

async function deleteFinishing() {
  const response = await finishingStore.deleteFinishing(selectedFinishing.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Acabamento eliminado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao remover o acabamento",
      life: 10000,
    });
    console.error(response);
  }
}

function openFinishingManagement(finishing: Finishing, mode: ManageMode) {
  selectedFinishing.value = new Finishing(finishing);
  manageMode.value = mode;
}

function confirmObsolete(finishing: Finishing) {
  selectedFinishing.value = new Finishing(finishing);

  confirm.require({
    message: `Tem a certeza que pretende marcar o acabamento '${selectedFinishing.value.name}' como obsoleto?`,
    header: "Confirmar obsolescência",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Confirmar",
      severity: "warn",
    },
    accept: obsoleteFinishing,
  });
}

function confirmDeletion(finishing: Finishing) {
  selectedFinishing.value = new Finishing(finishing);

  confirm.require({
    message: `Tem a certeza que pretende eliminar o acabamento '${selectedFinishing.value.name}'?`,
    header: "Confirmar remoção de acabamento",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Eliminar",
      severity: "danger",
    },
    accept: deleteFinishing,
  });
}

function close() {
  enabled.value = false;
}
</script>
