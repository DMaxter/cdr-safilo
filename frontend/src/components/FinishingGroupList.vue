<template>
  <P-Dialog modal v-model:visible="enabled" class="w-8/10 max-w-[1000px]" fluid>
    <template #header>Grupos de Acabamentos</template>
    <div class="h-85/100">
      <P-DataTable
        paginator
        removableSort
        filterDisplay="row"
        :value="finishingGroupStore.finishingGroups"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
        <template #empty>Não existem grupos de acabamentos registados</template>

        <P-Column sortable field="id" header="ID" style="width: 60px" />
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Nome" />
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
              @click="openFinishingGroupManagement(data, ManageMode.Edit)"
              v-tooltip="'Editar grupo de acabamentos'"
            />
            <Icon icon="delete" @click="confirmDeletion(data)" v-tooltip="'Eliminar grupo'" />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="openFinishingGroupManagement(new FinishingGroup(), ManageMode.Add)"
        >Adicionar</P-Button
      >
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <FinishingGroupManagement v-model="manageMode" :group="selectedGroup" />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { ManageMode } from "@/utils";

const TITLE = "Lista de Grupos de Acabamentos";

const enabled = defineModel<boolean>();
const finishingGroupStore = useFinishingGroupStore();
const confirm = useConfirm();
const toast = useToast();

const selectedGroup = ref<FinishingGroup>(new FinishingGroup());

const manageMode = ref<ManageMode>(ManageMode.None);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

async function refresh() {
  const response = await finishingGroupStore.getFinishingGroups();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de grupos de acabamentos",
      life: 10000,
    });
  }
}

async function deleteGroup() {
  const response = await finishingGroupStore.deleteGroup(selectedGroup.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Grupo de acabamentos eliminado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao remover o grupo de acabamentos",
      life: 10000,
    });
    console.error(response);
  }
}

function openFinishingGroupManagement(group: FinishingGroup, mode: ManageMode) {
  selectedGroup.value = new FinishingGroup(group);
  manageMode.value = mode;
}

function confirmDeletion(group: FinishingGroup) {
  selectedGroup.value = new FinishingGroup(group);

  confirm.require({
    message: `Tem a certeza que pretende eliminar o grupo de acabamentos '${selectedGroup.value.name}'?`,
    header: "Confirmar remoção de grupo de acabamentos",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Eliminar",
      severity: "danger",
    },
    accept: deleteGroup,
  });
}

function close() {
  enabled.value = false;
}
</script>
