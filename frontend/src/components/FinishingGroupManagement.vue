<template>
  <P-Dialog v-model:visible="enabled" modal>
    <template #header>{{
      editing ? "Editar Grupo de Acabamentos" : "Adicionar Grupo de Acabamentos"
    }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" v-model="props.group.id" fluid disabled />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="name" v-model="props.group.name" required fluid />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="finishings"
        v-model="props.group.finishings"
        required
        fluid
        :options="availableFinishings"
        option-label="name"
        display="chip"
        placeholder="Acabamentos"
      />
      <label for="finishings">Acabamentos</label>
    </P-FloatLabel>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, onMounted } from "vue";

import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { useFinishingStore } from "@stores/finishings";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled.value && mode.value === ManageMode.Edit);

const props = defineProps<{
  group: FinishingGroup;
}>();

const TITLE = computed(() =>
  editing.value ? "Edição de grupo de acabamentos" : "Criação de grupo de acabamentos",
);

const finishingGroupStore = useFinishingGroupStore();
const finishingStore = useFinishingStore();
const toast = useToast();

const availableFinishings = computed(() =>
  finishingStore.finishings.filter(
    (f) => !f.obsolete || props.group.finishings.some((selected) => selected.id === f.id),
  ),
);

const atLeastOne = (value: FinishingGroup["finishings"]) =>
  value.length > 0 || "Selecione pelo menos um acabamento";

const errors = computed(() =>
  [
    validateField(props.group.name.trim(), [required]),
    validateField(props.group.finishings, [atLeastOne]),
  ].filter((e) => e !== null),
);

const isValid = computed(() => errors.value.length === 0);

onMounted(async () => {
  await finishingStore.getFinishings();
});

async function action() {
  if (!isValid.value) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: errors.value.join(" "),
      life: 10000,
    });
    return;
  }

  if (editing.value) {
    await updateGroup();
  } else {
    await createGroup();
  }
}

async function createGroup() {
  const response = await finishingGroupStore.addGroup(props.group.name, props.group.finishings);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Grupo de acabamentos adicionado com sucesso",
      life: 10000,
    });
    close();
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: response.content,
      life: 10000,
    });
    console.error(response);
  }
}

async function updateGroup() {
  const response = await finishingGroupStore.editGroup(props.group);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Grupo de acabamentos editado com sucesso",
      life: 10000,
    });
    close();
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: response.content,
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  mode.value = ManageMode.None;
}
</script>
