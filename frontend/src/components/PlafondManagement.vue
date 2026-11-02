<template>
  <P-Dialog v-model:visible="enabled" modal header="Plafonds" class="w-[95vw] max-w-[560px]">
    <template #header>{{ editing ? "Editar Plafond" : "Adicionar Plafond" }}</template>
    <div class="mb-1 text-xs font-semibold text-gray-500 uppercase tracking-wide">Marca</div>
    <P-FloatLabel class="field" variant="on">
      <P-Select
        id="brand"
        v-model="selectedBrandId"
        fluid
        :options="brandStore.brands"
        option-label="name"
        option-value="id"
        :invalid="touched && !!brandError"
        @blur="touched = true"
      />
      <label for="brand">Marca</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched && brandError"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ brandError }}
    </P-Message>

    <div class="mb-1 mt-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">
      Plafond atual
    </div>
    <P-FloatLabel class="field" variant="on">
      <P-InputNumber id="amount" v-model="draftAmount" fluid :min="0" :max-fraction-digits="2" />
      <label for="amount">Valor</label>
    </P-FloatLabel>

    <div class="mb-1 mt-3 text-xs font-semibold text-gray-500 uppercase tracking-wide">Ajuste</div>
    <div class="field flex flex-col gap-2">
      <P-FloatLabel variant="on">
        <P-InputNumber
          id="change"
          v-model="changeAmount"
          fluid
          :max="10000"
          :min="-10000"
          :step="100"
          show-buttons
        />
        <label for="change">Alteração</label>
      </P-FloatLabel>
      <div class="flex items-center gap-2 text-sm text-gray-600">
        <span>Novo plafond estimado:</span>
        <strong>{{ (draftAmount + changeAmount).toFixed(2) }}</strong>
      </div>
    </div>

    <template #footer>
      <div class="flex justify-end gap-2">
        <P-Button label="Voltar" severity="secondary" text @click="close" />
        <P-Button :label="editing ? 'Atualizar' : 'Adicionar'" severity="primary" @click="action" />
      </div>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref, watch, onMounted } from "vue";

import { Brand } from "@router/backend/services/brand/types";
import { useBrandStore } from "@stores/brands";
import { useUserStore } from "@stores/users";
import { ManageMode } from "@/utils";
import { Plafond } from "@/router/backend/services/user/types";

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
  plafond: Plafond;
  user: string;
}>();

const brandStore = useBrandStore();
const userStore = useUserStore();
const toast = useToast();

const TITLE = computed(() => (editing.value ? "Edição de plafond" : "Criação de plafond"));

const changeAmount = ref(0);
const selectedBrandId = ref<number | null>(null);

// Validation state, cleared every time the dialog (re)opens.
const touched = ref(false);
const brandError = computed(() => (selectedBrandId.value === null ? "Selecione uma marca" : null));

// Work on a copy so edits don't leak into the store until save.
const draftAmount = ref(0);

async function loadBrandsIfNeeded() {
  if (brandStore.brands.length === 0) {
    await brandStore.getBrands();
  }
}

watch(
  mode,
  async (newMode) => {
    if (enabled.value) {
      await loadBrandsIfNeeded();
    }

    touched.value = false;
    changeAmount.value = 0;
    draftAmount.value = props.plafond.amount;

    if (editing.value && props.plafond.brand) {
      const brand = brandStore.brands.find((b: Brand) => b.name === props.plafond.brand);
      if (brand) {
        selectedBrandId.value = brand.id;
      } else {
        selectedBrandId.value = null;
      }
    } else if (newMode === ManageMode.Add) {
      selectedBrandId.value = null;
    }
  },
  { immediate: true },
);

watch(enabled, async (isEnabled) => {
  if (isEnabled) {
    await loadBrandsIfNeeded();
  }
});

onMounted(async () => {
  await loadBrandsIfNeeded();
});

async function action() {
  touched.value = true;

  const brandId = selectedBrandId.value;

  if (brandId === null) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: brandError.value,
      life: 5000,
    });
    return;
  }
  const finalAmount = draftAmount.value + (changeAmount.value || 0);
  if (finalAmount < 0) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: "O plafond não pode ser negativo",
      life: 5000,
    });
    return;
  }

  const response = await userStore.changePlafond(props.user, brandId, finalAmount);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: `Plafond ${editing.value ? "atualizado" : "adicionado"} com sucesso`,
      life: 10000,
    });
    close();
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail:
        response.content || `Ocorreu um erro a ${editing.value ? "editar" : "adicionar"} o plafond`,
      life: 10000,
    });
  }
}

function close() {
  touched.value = false;
  mode.value = ManageMode.None;
}
</script>
