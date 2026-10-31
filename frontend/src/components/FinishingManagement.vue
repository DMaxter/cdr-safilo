<template>
  <P-Dialog v-model:visible="enabled" modal>
    <template #header>{{ editing ? "Editar Acabamento" : "Adicionar Acabamento" }}</template>
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
    <template v-if="!editing">
      <P-FloatLabel class="field" variant="on">
        <P-InputNumber
          id="cost"
          v-model="draft.cost"
          required
          fluid
          mode="currency"
          currency="EUR"
          locale="pt-PT"
          :min-fraction-digits="2"
          :max-fraction-digits="2"
          :invalid="touched.cost && !!errors.cost"
          @blur="touch('cost')"
        />
        <label for="cost">Custo</label>
      </P-FloatLabel>
      <P-Message
        v-if="touched.cost && errors.cost"
        class="mt-2"
        severity="error"
        size="small"
        variant="simple"
      >
        {{ errors.cost }}
      </P-Message>
      <P-FloatLabel class="field" variant="on">
        <P-MultiSelect
          id="materials"
          v-model="draft.materials"
          required
          fluid
          :options="availableMaterials"
          option-label="name"
          display="chip"
          placeholder="Materiais"
          :invalid="touched.materials && !!errors.materials"
          @blur="touch('materials')"
        />
        <label for="materials">Materiais do acabamento</label>
      </P-FloatLabel>
      <P-Message
        v-if="touched.materials && errors.materials"
        class="mt-2"
        severity="error"
        size="small"
        variant="simple"
      >
        {{ errors.materials }}
      </P-Message>
    </template>
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

import { Finishing, NewFinishing } from "@router/backend/services/finishing/types";
import type { Material } from "@router/backend/services/material/types";
import { useFinishingStore } from "@stores/finishings";
import { useMaterialStore } from "@stores/materials";
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
  finishing: Finishing;
}>();

const TITLE = computed(() => (editing.value ? "Edição de acabamento" : "Criação de acabamento"));

const finishingStore = useFinishingStore();
const materialStore = useMaterialStore();
const toast = useToast();

type FinishingDraft = {
  id: number;
  name: string;
  obsolete: boolean;
  cost: number | null;
  materials: Material[];
};

function newDraft(finishing?: Finishing): FinishingDraft {
  return {
    id: finishing?.id ?? 0,
    name: finishing?.name ?? "",
    obsolete: finishing?.obsolete ?? false,
    cost: 0,
    materials: [],
  };
}

// Validation state, cleared every time the dialog (re)opens.
const touched = ref<Record<string, boolean>>({});

// Work on a copy so edits don't leak into the store until save.
const draft = ref<FinishingDraft>(newDraft());
watch(
  () => [props.finishing, mode.value] as const,
  () => {
    draft.value = newDraft(props.finishing);
    touched.value = {};
  },
  { immediate: true },
);

const availableMaterials = computed(() => materialStore.materials.filter((m) => !m.obsolete));

const nonNegative = (value: number | null) =>
  value === null || value === undefined
    ? "Campo obrigatório"
    : value >= 0 || "O custo não pode ser negativo";

const atLeastOne = (value: Material[]) => value.length > 0 || "Selecione pelo menos um material";

const errors = computed<Record<string, string | null>>(() => ({
  name: validateField(draft.value.name.trim(), [required]),
  cost: editing.value ? null : validateField(draft.value.cost, [nonNegative]),
  materials: editing.value ? null : validateField(draft.value.materials, [atLeastOne]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);

const isValid = computed(() => errorList.value.length === 0);

onMounted(async () => {
  await materialStore.getMaterials();
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
    await updateFinishing();
  } else {
    await createFinishing();
  }
}

async function createFinishing() {
  const newFinishing = new NewFinishing({
    name: draft.value.name,
    cost: draft.value.cost ?? 0,
    materials: draft.value.materials,
  });

  const response = await finishingStore.addFinishing(newFinishing);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Acabamento adicionado com sucesso",
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

async function updateFinishing() {
  const finishing = new Finishing({
    id: draft.value.id,
    name: draft.value.name,
    obsolete: draft.value.obsolete,
  });

  const response = await finishingStore.editFinishing(finishing);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Acabamento editado com sucesso",
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
