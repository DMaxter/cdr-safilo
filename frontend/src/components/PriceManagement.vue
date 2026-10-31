<template>
  <P-Dialog v-model:visible="enabled" modal>
    <template #header>{{ editing ? "Editar Preço" : "Adicionar Preço" }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" v-model="draft.id" fluid disabled />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-Select
        id="material"
        v-model="draft.material"
        required
        fluid
        :options="availableMaterials"
        option-label="name"
        option-value="id"
        placeholder="Material"
        :invalid="touched.material && !!errors.material"
        @blur="touch('material')"
      />
      <label for="material">Material</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.material && errors.material"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.material }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputNumber
        id="costPerSquareMeter"
        v-model="draft.costPerSquareMeter"
        required
        fluid
        mode="currency"
        currency="EUR"
        locale="pt-PT"
        :min-fraction-digits="2"
        :max-fraction-digits="2"
        :invalid="touched.costPerSquareMeter && !!errors.costPerSquareMeter"
        @blur="touch('costPerSquareMeter')"
      />
      <label for="costPerSquareMeter">Preço por m²</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.costPerSquareMeter && errors.costPerSquareMeter"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.costPerSquareMeter }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputNumber
        id="fixedCost"
        v-model="draft.fixedCost"
        required
        fluid
        mode="currency"
        currency="EUR"
        locale="pt-PT"
        :min-fraction-digits="2"
        :max-fraction-digits="2"
        :invalid="touched.fixedCost && !!errors.fixedCost"
        @blur="touch('fixedCost')"
      />
      <label for="fixedCost">Custo fixo</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.fixedCost && errors.fixedCost"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.fixedCost }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="finishings"
        v-model="draft.finishings"
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
import { computed, onMounted, ref, watch } from "vue";

import { Price } from "@router/backend/services/price/types";
import { usePriceStore } from "@stores/prices";
import { useMaterialStore } from "@stores/materials";
import { useFinishingStore } from "@stores/finishings";
import { validateField } from "@/rules";
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
  price: Price;
}>();

const TITLE = computed(() => (editing.value ? "Edição de preço" : "Criação de preço"));

const priceStore = usePriceStore();
const materialStore = useMaterialStore();
const finishingStore = useFinishingStore();
const toast = useToast();

// Validation state, cleared every time the dialog (re)opens.
const touched = ref<Record<string, boolean>>({});

// Work on a copy so edits don't leak into the store until save.
const draft = ref<Price>(new Price());
watch(
  () => [props.price, mode.value] as const,
  () => {
    draft.value = new Price(props.price);
    touched.value = {};
  },
  { immediate: true },
);

const availableMaterials = computed(() => materialStore.materials.filter((m) => !m.obsolete));
const availableFinishings = computed(() => finishingStore.finishings.filter((f) => !f.obsolete));

const positive = (value: number | null) =>
  value === null || value === undefined
    ? "Campo obrigatório"
    : value > 0 || "O valor tem de ser maior que zero";

const notNegative = (value: number | null) =>
  value === null || value === undefined
    ? "Campo obrigatório"
    : value >= 0 || "O valor não pode ser negativo";

const errors = computed<Record<string, string | null>>(() => ({
  material: draft.value.material ? null : "Selecione um material",
  costPerSquareMeter: validateField(draft.value.costPerSquareMeter, [positive]),
  fixedCost: validateField(draft.value.fixedCost, [notNegative]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);

const isValid = computed(() => errorList.value.length === 0);

onMounted(async () => {
  await materialStore.getMaterials();
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
    await updatePrice();
  } else {
    await createPrice();
  }
}

async function createPrice() {
  const response = await priceStore.addPrice(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Preço adicionado com sucesso",
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

async function updatePrice() {
  const response = await priceStore.editPrice(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Preço editado com sucesso",
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
