<template>
  <P-Dialog modal v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Preço" : "Adicionar Preço" }}</template>
    <P-FloatLabel v-if="editing" class="field" variant="on">
      <P-InputText id="id" fluid disabled v-model="props.price.id" />
      <label for="id">ID</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-Select
        id="material"
        required
        fluid
        v-model="props.price.material"
        :options="availableMaterials"
        optionLabel="name"
        optionValue="id"
        placeholder="Material"
      />
      <label for="material">Material</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputNumber
        id="costPerSquareMeter"
        required
        fluid
        v-model="props.price.costPerSquareMeter"
        mode="currency"
        currency="EUR"
        locale="pt-PT"
        :minFractionDigits="2"
        :maxFractionDigits="2"
      />
      <label for="costPerSquareMeter">Preço por m²</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputNumber
        id="fixedCost"
        required
        fluid
        v-model="props.price.fixedCost"
        mode="currency"
        currency="EUR"
        locale="pt-PT"
        :minFractionDigits="2"
        :maxFractionDigits="2"
      />
      <label for="fixedCost">Custo fixo</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-MultiSelect
        id="finishings"
        fluid
        v-model="props.price.finishings"
        :options="availableFinishings"
        optionLabel="name"
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

import { Price } from "@router/backend/services/price/types";
import { usePriceStore } from "@stores/prices";
import { useMaterialStore } from "@stores/materials";
import { useFinishingStore } from "@stores/finishings";
import { required, validateField } from "@/rules";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled.value && mode.value === ManageMode.Edit);

const props = defineProps<{
  price: Price;
}>();

const TITLE = computed(() => (editing.value ? "Edição de preço" : "Criação de preço"));

const priceStore = usePriceStore();
const materialStore = useMaterialStore();
const finishingStore = useFinishingStore();
const toast = useToast();

const availableMaterials = computed(() => materialStore.materials.filter((m) => !m.obsolete));
const availableFinishings = computed(() => finishingStore.finishings.filter((f) => !f.obsolete));

const positive = (value: number) => value > 0 || "O valor tem de ser maior que zero";
const notNegative = (value: number) => value >= 0 || "O valor não pode ser negativo";

const costPerSquareMeterRules = [required, positive];
const fixedCostRules = [required, notNegative];

const errors = computed(() =>
  [
    props.price.material ? null : "Selecione um material",
    validateField(props.price.costPerSquareMeter, costPerSquareMeterRules),
    validateField(props.price.fixedCost, fixedCostRules),
  ].filter((e) => e !== null),
);

const isValid = computed(() => errors.value.length === 0);

onMounted(async () => {
  await materialStore.getMaterials();
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
    await updatePrice();
  } else {
    await createPrice();
  }
}

async function createPrice() {
  const response = await priceStore.addPrice(props.price);

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
  const response = await priceStore.editPrice(props.price);

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
  mode.value = ManageMode.None;
}
</script>
