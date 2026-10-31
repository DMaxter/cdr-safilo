<template>
  <P-Dialog v-model:visible="enabled" modal>
    <template #header>{{
      editing ? "Editar Grupo de Acabamentos" : "Adicionar Grupo de Acabamentos"
    }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" v-model="draft.id" fluid disabled />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="name"
        v-model="draft.name"
        required
        fluid
        :invalid="touched.name && !!errors.name"
        @blur="touch('name')"
      />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.name && errors.name"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.name }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="finishings"
        v-model="draft.finishings"
        required
        fluid
        :options="availableFinishings"
        option-label="name"
        display="chip"
        placeholder="Acabamentos"
        :invalid="touched.finishings && !!errors.finishings"
        @blur="touch('finishings')"
      />
      <label for="finishings">Acabamentos</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.finishings && errors.finishings"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.finishings }}
    </P-Message>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watch } from "vue";

import { Finishing } from "@router/backend/services/finishing/types";
import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { useFinishingStore } from "@stores/finishings";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed({
  get: () => mode.value !== ManageMode.None,
  set: (value) => {
    if (!value) {
      close();
    }
  },
});
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

// Validation state, cleared every time the dialog (re)opens.
const touched = ref<Record<string, boolean>>({});

// Work on a copy so edits don't leak into the store until save.
const draft = ref<FinishingGroup>(new FinishingGroup());
watch(
  () => [props.group, mode.value] as const,
  () => {
    draft.value = new FinishingGroup(props.group);
    touched.value = {};
  },
  { immediate: true },
);

const availableFinishings = computed(() =>
  finishingStore.finishings.filter(
    (f) => !f.obsolete || draft.value.finishings.some((selected) => selected.id === f.id),
  ),
);

const atLeastOne = (value: Finishing[]) => value.length > 0 || "Selecione pelo menos um acabamento";

const errors = computed<Record<string, string | null>>(() => ({
  name: validateField(draft.value.name.trim(), [required]),
  finishings: validateField(draft.value.finishings, [atLeastOne]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);

const isValid = computed(() => errorList.value.length === 0);

onMounted(async () => {
  await finishingStore.getFinishings();
});

function touch(field: string) {
  touched.value[field] = true;
}

async function action() {
  for (const field of Object.keys(errors.value)) {
    touched.value[field] = true;
  }

  if (!isValid.value) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: errorList.value[0],
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
  const response = await finishingGroupStore.addGroup(draft.value.name, draft.value.finishings);

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
  const response = await finishingGroupStore.editGroup(draft.value);

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
  touched.value = {};
  mode.value = ManageMode.None;
}
</script>
