<template>
  <P-Dialog modal v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Marca" : "Adicionar Marca" }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" fluid disabled v-model="props.brand.id" />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="name" required fluid v-model="props.brand.name" />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed } from "vue";

import { Brand } from "@router/backend/services/brand/types";
import { useBrandStore } from "@stores/brands";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled && mode.value === ManageMode.Edit);

const props = defineProps<{
  brand: Brand;
}>();

const TITLE = computed(() => (editing.value ? "Edição de marca" : "Criação de marca"));

const brandStore = useBrandStore();
const toast = useToast();

async function action() {
  if (editing.value) {
    await updateBrand();
  } else {
    await createBrand();
  }
}

async function createBrand() {
  const response = await brandStore.addBrand(props.brand.name);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Marca adicionada com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a adicionar a marca",
      life: 10000,
    });
    console.error(response);
  }
}

async function updateBrand() {
  const response = await brandStore.editBrand(props.brand.id, props.brand.name);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Marca editada com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a editar a marca",
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  mode.value = ManageMode.None;
}
</script>
