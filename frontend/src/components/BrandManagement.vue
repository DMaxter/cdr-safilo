<template>
  <P-Dialog modal v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Marca" : "Adicionar Marca" }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" fluid disabled v-model="draft.id" />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="name"
        required
        fluid
        v-model="draft.name"
        @blur="touched = true"
        :invalid="touched && !!nameError"
      />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched && nameError"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ nameError }}
    </P-Message>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref, watch } from "vue";

import { Brand } from "@router/backend/services/brand/types";
import { useBrandStore } from "@stores/brands";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled && mode.value === ManageMode.Edit);

const props = defineProps<{
  brand: Brand;
}>();

const TITLE = computed(() => (editing.value ? "Edição de marca" : "Criação de marca"));

// Work on a copy so edits don't leak into the store until save.
const draft = ref<Brand>(new Brand());
watch(
  () => [props.brand, mode.value] as const,
  () => {
    draft.value = new Brand(props.brand);
  },
  { immediate: true },
);

const brandStore = useBrandStore();
const toast = useToast();

const touched = ref(false);
const nameError = computed(() => validateField(draft.value.name.trim(), [required]));

async function action() {
  touched.value = true;

  if (nameError.value) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: nameError.value,
      life: 10000,
    });
    return;
  }

  if (editing.value) {
    await updateBrand();
  } else {
    await createBrand();
  }
}

async function createBrand() {
  const response = await brandStore.addBrand(draft.value.name);

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
  const response = await brandStore.editBrand(draft.value.id, draft.value.name);

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
