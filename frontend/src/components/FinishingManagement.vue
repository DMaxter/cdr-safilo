<template>
  <P-Dialog modal v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Acabamento" : "Adicionar Acabamento" }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" fluid disabled v-model="props.finishing.id" />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="name" required fluid v-model="props.finishing.name" />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <template v-if="!editing">
      <P-FloatLabel class="field" variant="on">
        <P-InputNumber
          id="cost"
          required
          fluid
          v-model="draft.cost"
          mode="currency"
          currency="EUR"
          locale="pt-PT"
          :minFractionDigits="2"
          :maxFractionDigits="2"
        />
        <label for="cost">Custo</label>
      </P-FloatLabel>
      <P-FloatLabel class="field" variant="on">
        <P-MultiSelect
          id="materials"
          required
          fluid
          v-model="draft.materials"
          :options="availableMaterials"
          optionLabel="name"
          display="chip"
          placeholder="Materiais"
        />
        <label for="materials">Materiais do acabamento</label>
      </P-FloatLabel>
    </template>
    <div v-if="editing" class="field flex items-center gap-2">
      <P-ToggleSwitch id="obsolete" v-model="props.finishing.obsolete" />
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
import { computed, onMounted, ref } from "vue";

import { Finishing, NewFinishing } from "@router/backend/services/finishing/types";
import { useFinishingStore } from "@stores/finishings";
import { useMaterialStore } from "@stores/materials";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled.value && mode.value === ManageMode.Edit);

const props = defineProps<{
  finishing: Finishing;
}>();

const TITLE = computed(() => (editing.value ? "Edição de acabamento" : "Criação de acabamento"));

const finishingStore = useFinishingStore();
const materialStore = useMaterialStore();
const toast = useToast();

const draft = ref<NewFinishing>(new NewFinishing());

const availableMaterials = computed(() => materialStore.materials.filter((m) => !m.obsolete));

const nonNegative = (value: number | null) =>
  value === null || value === undefined
    ? "Campo obrigatório"
    : value >= 0 || "O custo não pode ser negativo";

const atLeastOne = (value: NewFinishing["materials"]) =>
  value.length > 0 || "Selecione pelo menos um material";

const errors = computed(() => {
  const fields = [validateField(props.finishing.name.trim(), [required])];

  if (!editing.value) {
    fields.push(
      validateField(draft.value.cost, [nonNegative]),
      validateField(draft.value.materials, [atLeastOne]),
    );
  }

  return fields.filter((e) => e !== null);
});

const isValid = computed(() => errors.value.length === 0);

onMounted(async () => {
  await materialStore.getMaterials();
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
    await updateFinishing();
  } else {
    await createFinishing();
  }
}

async function createFinishing() {
  const newFinishing = new NewFinishing({
    name: props.finishing.name,
    cost: draft.value.cost,
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
  const response = await finishingStore.editFinishing(props.finishing);

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
  mode.value = ManageMode.None;
}
</script>
