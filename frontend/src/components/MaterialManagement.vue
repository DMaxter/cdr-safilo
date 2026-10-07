<template>
  <P-Dialog modal v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Material" : "Adicionar Material" }}</template>
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
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="mandatoryFinishings"
        fluid
        v-model="draft.mandatoryFinishings"
        :options="finishingGroupStore.finishingGroups"
        optionLabel="name"
        placeholder="Grupos de acabamentos obrigatórios"
      />
      <label for="mandatoryFinishings">Acabamentos obrigatórios</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="additionalFinishings"
        fluid
        v-model="draft.additionalFinishings"
        :options="finishingStore.finishings"
        optionLabel="name"
        placeholder="Acabamentos adicionais"
      />
      <label for="additionalFinishings">Acabamentos adicionais</label>
    </P-FloatLabel>
    <div v-if="editing" class="field flex items-center gap-2">
      <P-ToggleSwitch id="obsolete" v-model="draft.obsolete" />
      <label for="obsolete">Obsoleto</label>
    </div>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watch } from "vue";

import { Material } from "@router/backend/services/material/types";
import { useMaterialStore } from "@stores/materials";
import { useFinishingStore } from "@stores/finishings";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled && mode.value === ManageMode.Edit);

const props = defineProps<{
  material: Material;
}>();

const TITLE = computed(() => (editing.value ? "Edição de material" : "Criação de material"));

// Work on a copy so edits don't leak into the store until save.
const draft = ref<Material>(new Material());
watch(
  () => [props.material, mode.value] as const,
  () => {
    draft.value = new Material(props.material);
  },
  { immediate: true },
);

const materialStore = useMaterialStore();
const finishingStore = useFinishingStore();
const finishingGroupStore = useFinishingGroupStore();
const toast = useToast();

const touched = ref(false);
const nameError = computed(() => validateField(draft.value.name.trim(), [required]));

onMounted(async () => {
  await finishingStore.getFinishings();
  await finishingGroupStore.getFinishingGroups();
});

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
    await updateMaterial();
  } else {
    await createMaterial();
  }
}

async function createMaterial() {
  const response = await materialStore.addMaterial(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Material adicionado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a adicionar o material",
      life: 10000,
    });
    console.error(response);
  }
}

async function updateMaterial() {
  const response = await materialStore.editMaterial(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Material editado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a editar o material",
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  mode.value = ManageMode.None;
}
</script>
