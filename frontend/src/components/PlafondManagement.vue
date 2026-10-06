<template>
  <P-Dialog v-model:visible="enabled" modal header="Plafonds" class="w-[500px]">
    <template #header>{{ editing ? "Editar Plafond" : "Adicionar Plafond" }}</template>
    <P-FloatLabel class="field" variant="on">
      <P-Select
        fluid
        id="brand"
        v-model="selectedBrandId"
        :options="brandStore.brands"
        optionLabel="name"
        optionValue="id"
      />
      <label for="brand">Marca</label>
    </P-FloatLabel>

    <P-FloatLabel class="field" variant="on">
      <P-InputNumber fluid id="amount" v-model="draftAmount" :min="0" :maxFractionDigits="2" />
      <label for="amount">Plafond</label>
    </P-FloatLabel>

    <div class="field flex items-center gap-2">
      <P-FloatLabel variant="on">
        <P-InputNumber
          fluid
          id="change"
          v-model="changeAmount"
          :min="0"
          :max="10000"
          :step="100"
          showButtons
          class="w-28"
        />
        <label for="change">Alteração</label>
      </P-FloatLabel>
      <P-Button fluid label="Aumentar" @click="increasePlafond" />
      <P-Button fluid label="Diminuir" @click="decreasePlafond" />
    </div>

    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
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
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled && mode.value === ManageMode.Edit);

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

function increasePlafond() {
  draftAmount.value += changeAmount.value;
}

function decreasePlafond() {
  draftAmount.value -= changeAmount.value;
  if (draftAmount.value < 0) {
    draftAmount.value = 0;
  }
}

async function action() {
  if (selectedBrandId.value === null) {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Selecione uma marca",
      life: 5000,
    });
    return;
  }

  const response = await userStore.changePlafond(
    props.user,
    selectedBrandId.value,
    draftAmount.value,
  );

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
      detail: `Ocorreu um erro a ${editing.value ? "editar" : "adicionar"} o plafond`,
      life: 10000,
    });
  }
}

function close() {
  mode.value = ManageMode.None;
}
</script>
