<template>
  <P-Dialog modal v-model:visible="enabled" class="w-8/10 max-w-[1000px]" fluid>
    <template #header>Materiais</template>
    <div class="h-85/100">
    <P-DataTable
      paginator
      removableSort
      filterDisplay="row"
      :value="materialStore.materials"
      :rows="10"
      :rowsPerPageOptions="[5, 10, 20, 50, 100]"
      v-model:filters="filters"
    >
      <template #empty>Não existem materiais registados</template>

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
            <Icon icon="edit" @click="openMaterialManagement(data, ManageMode.Edit)" v-tooltip="'Editar material'" />
            <Icon icon="delete" @click="confirmDeletion(data)" v-tooltip="'Eliminar material'" />
          </template>
        </P-Column>
    </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="openMaterialManagement(new Material(), ManageMode.Add)">Adicionar</P-Button>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <MaterialManagement v-model="manageMode" :material="selectedMaterial" />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { Material } from "@router/backend/services/material/types";
import { useMaterialStore } from "@stores/materials";
import { ManageMode } from "@/utils";

const TITLE = "Lista de Materiais";

const enabled = defineModel<boolean>();
const materialStore = useMaterialStore();
const confirm = useConfirm();
const toast = useToast();

const selectedMaterial = ref<Material>(new Material());

const manageMode = ref<ManageMode>(ManageMode.None);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

async function refresh() {
  const response = await materialStore.getMaterials();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de materiais",
      life: 10000,
    });
  }
}

async function obsoleteMaterial() {
  const response = await materialStore.makeMaterialObsolete(selectedMaterial.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Material marcado como obsoleto",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao marcar o material como obsoleto",
      life: 10000,
    });
    console.error(response);
  }
}

async function deleteMaterial() {
  const response = await materialStore.deleteMaterial(selectedMaterial.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Material eliminado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro a remover o material",
      life: 10000,
    });
    console.error(response);
  }
}

function openMaterialManagement(material: Material, mode: ManageMode) {
  selectedMaterial.value = material;
  manageMode.value = mode;
}

function confirmObsolete(material: Material) {
  selectedMaterial.value = material;

  confirm.require({
    message: `Tem a certeza que pretende marcar o material '${selectedMaterial.value.name}' como obsoleto?`,
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
    accept: obsoleteMaterial,
  });
}

function confirmDeletion(material: Material) {
  selectedMaterial.value = material;

  confirm.require({
    message: `Tem a certeza que pretende eliminar o material '${selectedMaterial.value.name}'?`,
    header: "Confirmar remoção de material",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Eliminar",
      severity: "danger",
    },
    accept: deleteMaterial,
  });
}

function close() {
  enabled.value = false;
}
</script>
