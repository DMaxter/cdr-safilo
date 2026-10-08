<template>
  <Container>
    <div v-if="!selectedClient" class="pt-4">
      <ClientPicker v-model="selectedClient" />
    </div>

    <div v-if="selectedClient" class="flex items-center gap-2 mt-4 mb-4">
      <b>Cliente:</b> {{ selectedClient.name }} — {{ selectedClient.address }},
      {{ selectedClient.postalCode }} {{ selectedClient.city }}
      <P-Button
        v-tooltip="'Alterar cliente'"
        text
        severity="secondary"
        size="small"
        @click="selectedClient = null"
        ><template #icon><Icon icon="edit" /></template
      ></P-Button>
    </div>

    <P-Stepper v-if="selectedClient" v-model:value="wizard.step">
      <P-StepList>
        <P-Step :value="1">Tipo</P-Step>
        <P-Step :value="2" :disabled="!wizard.type">Detalhes</P-Step>
        <P-Step :value="3" :disabled="!wizard.type || !allSlotsValid">Rever</P-Step>
      </P-StepList>
      <P-StepPanels>
        <P-StepPanel :value="1">
          <div class="text-center mb-5">
            <div class="text-lg font-semibold">Selecione o tipo de pedido</div>
            <div class="text-sm text-gray-500">
              O tipo define quantas faces é preciso configurar
            </div>
          </div>

          <div class="grid grid-cols-2 lg:grid-cols-5 gap-3 sm:gap-4">
            <button
              v-for="rt in requestTypes"
              :key="rt.name"
              type="button"
              class="relative flex flex-col border rounded p-3 text-left cursor-pointer transition-colors"
              :class="
                wizard.type === rt.name
                  ? 'border-primary ring-2 ring-primary bg-primary-50'
                  : 'hover:border-primary'
              "
              @click="selectType(rt.name as RequestTypeName)"
            >
              <Icon
                v-if="wizard.type === rt.name"
                icon="check_circle"
                class="absolute top-2 right-2 text-primary text-xl"
              />
              <div
                class="flex items-center justify-center h-20 sm:h-28 rounded-lg"
                :class="
                  wizard.type === rt.name
                    ? 'bg-primary-100 text-primary'
                    : 'bg-gray-100 text-gray-500'
                "
              >
                <span
                  class="inline-flex"
                  :style="
                    MIRRORED_ICONS.includes(rt.name as RequestTypeName)
                      ? 'transform: scaleX(-1)'
                      : ''
                  "
                >
                  <Icon :icon="TYPE_ICONS[rt.name as RequestTypeName]" :size="48" />
                </span>
              </div>
              <div class="font-bold mt-3 text-sm sm:text-base">{{ rt.value }}</div>
              <div class="text-xs text-gray-500">
                {{ TYPE_DESCRIPTIONS[rt.name as RequestTypeName] }}
              </div>
              <div class="flex flex-wrap gap-1 mt-2">
                <span
                  v-for="face in typeFaces(rt.name as RequestTypeName)"
                  :key="face"
                  class="text-[10px] px-1.5 py-0.5 rounded bg-gray-100 text-gray-600"
                >
                  {{ face }}
                </span>
              </div>
            </button>
          </div>
          <div class="flex justify-end gap-2 mt-4">
            <P-Button label="Anterior" severity="secondary" @click="selectedClient = null"
              ><template #icon><Icon icon="arrow_back" /></template
            ></P-Button>
            <P-Button label="Próximo" :disabled="!wizard.type" @click="goToStep(2)"
              ><template #icon><Icon icon="arrow_forward" /></template
            ></P-Button>
          </div>
        </P-StepPanel>

        <P-StepPanel :value="2">
          <div class="flex flex-col gap-3">
            <div v-if="!wizard.brand" class="sm:w-96">
              <P-FloatLabel variant="on" class="w-full">
                <P-Select
                  :model-value="wizard.brand"
                  :options="brandStore.brands"
                  option-label="name"
                  fluid
                  filter
                  @update:model-value="onBrandSelect"
                />
                <label>Marca</label>
              </P-FloatLabel>
            </div>
            <div v-else class="flex items-center gap-2">
              <b>Marca:</b> {{ wizard.brand?.name }}
              <P-Button
                v-tooltip="'Alterar marca'"
                text
                severity="secondary"
                size="small"
                @click="wizard.setBrand(null as any)"
                ><template #icon><Icon icon="edit" /></template
              ></P-Button>
            </div>

            <div
              v-if="currentSlotConfig"
              class="grid grid-cols-3 sm:grid-cols-4 lg:grid-cols-5 gap-2"
            >
              <button
                v-for="(slotConfig, i) in wizard.slotConfigs"
                :key="`nav-${slotConfig.key}`"
                type="button"
                class="relative border rounded p-2 text-center cursor-pointer transition-colors"
                :class="
                  i + 1 === slotStep
                    ? 'border-primary ring-2 ring-primary bg-primary-50'
                    : 'hover:border-primary'
                "
                @click="slotStep = i + 1"
              >
                <Icon
                  v-if="isSlotValid(slotConfig.key)"
                  icon="check_circle"
                  class="absolute top-1 right-1 text-green-600"
                />
                <div class="text-xs font-bold truncate">{{ slotShortLabel(slotConfig.key) }}</div>
                <img
                  v-if="wizard.slots[slotConfig.key]?.image"
                  :src="wizard.slots[slotConfig.key]?.image?.link"
                  class="h-10 sm:h-12 w-full object-contain mt-1"
                  alt="Imagem da zona"
                />
                <div
                  v-else
                  class="h-10 sm:h-12 w-full flex items-center justify-center text-gray-400 mt-1"
                >
                  <Icon icon="image" class="text-xl" />
                </div>
              </button>
            </div>

            <SlotForm
              v-if="currentSlotConfig"
              :key="currentSlotConfig.key"
              :slot-key="currentSlotConfig.key"
              :slot-label="currentSlotConfig.label"
              :draft="wizard.slots[currentSlotConfig.key]"
              :materials="nonObsoleteMaterials"
              :brand="wizard.brand"
            />

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:max-w-2xl">
              <P-FloatLabel variant="on">
                <P-InputNumber v-model="wizard.amount" :min="1" fluid />
                <label>Quantidade</label>
              </P-FloatLabel>
              <div class="flex items-center gap-2">
                <P-ToggleSwitch v-model="wizard.application" input-id="application" />
                <label for="application">Aplicação (+100 Créditos)</label>
              </div>
            </div>
          </div>
          <div class="flex justify-between mt-4">
            <P-Button label="Anterior" severity="secondary" @click="goToStep(1)"
              ><template #icon><Icon icon="arrow_back" /></template
            ></P-Button>
            <P-Button label="Próximo" :disabled="!allSlotsValid" @click="goToStep(3)"
              ><template #icon><Icon icon="arrow_forward" /></template
            ></P-Button>
          </div>
        </P-StepPanel>

        <P-StepPanel :value="3">
          <div class="flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label>Cliente:</label>
                <div class="font-bold">{{ wizard.client?.name }}</div>
              </div>
              <div>
                <label>Marca:</label>
                <div class="font-bold">{{ wizard.brand?.name }}</div>
              </div>
              <div>
                <label>Tipo:</label>
                <div class="font-bold">{{ wizard.typeName }}</div>
              </div>

              <div
                v-for="slotConfig in wizard.slotConfigs"
                :key="slotConfig.key"
                class="md:col-span-3 flex flex-col sm:flex-row items-start sm:items-center gap-3 border rounded p-3"
              >
                <img
                  v-if="wizard.slots[slotConfig.key]?.image"
                  :src="wizard.slots[slotConfig.key]?.image?.link"
                  class="h-20 w-20 object-contain rounded border bg-gray-50 shrink-0"
                  alt="Imagem da zona"
                />
                <div
                  v-else
                  class="h-20 w-20 flex items-center justify-center text-gray-300 rounded border bg-gray-50 shrink-0"
                >
                  <Icon icon="image" class="text-3xl" />
                </div>
                <div>
                  <div class="font-bold">{{ slotConfig.label }}</div>
                  <div class="text-sm">
                    Material: {{ wizard.slots[slotConfig.key]?.material?.name ?? "—" }}
                    | Medidas:
                    {{ wizard.slots[slotConfig.key]?.width }}x{{
                      wizard.slots[slotConfig.key]?.height
                    }}
                    cm | Acabamentos:
                    {{
                      wizard.slots[slotConfig.key]?.finishings.map((f: any) => f.name).join(", ") ||
                      "Nenhum"
                    }}
                  </div>
                </div>
              </div>

              <div v-if="wizard.isShowcase">
                <label>Quantidade:</label>
                <div class="font-bold">{{ wizard.amount }}</div>
              </div>
              <div>
                <label>Aplicação:</label>
                <div class="font-bold">{{ wizard.application ? "Sim" : "Não" }}</div>
              </div>
              <div>
                <label>Custo estimado:</label>
                <div class="font-bold">
                  <template v-if="estimatedPrice !== null"
                    >{{ estimatedPrice.toFixed(2) }} Créditos</template
                  >
                  <template v-else-if="!canEstimatePrice">—</template>
                  <template v-else>A calcular...</template>
                </div>
              </div>

              <div class="md:col-span-3">
                <P-FloatLabel variant="on">
                  <P-Textarea v-model="wizard.observations" rows="3" fluid />
                  <label>Observações</label>
                </P-FloatLabel>
              </div>
            </div>

            <div v-if="currentPlafond !== null" class="border rounded p-3 bg-gray-50">
              <label>Plafond ({{ wizard.brand?.name }}):</label>
              <div class="font-bold mt-1">
                {{ currentPlafond.toFixed(2) }} Créditos
                <span v-if="estimatedPrice !== null" class="text-red-500 font-normal">
                  (depois: {{ (currentPlafond - estimatedPrice).toFixed(2) }})
                </span>
              </div>
            </div>
          </div>
          <div class="flex justify-between mt-4">
            <P-Button label="Anterior" severity="secondary" @click="goToStep(2)"
              ><template #icon><Icon icon="arrow_back" /></template
            ></P-Button>
            <P-Button
              :label="wizard.isEditing ? 'Editar Pedido' : 'Criar Pedido'"
              :loading="wizard.submitting"
              :disabled="wizard.submitting"
              @click="submit"
              ><template #icon><Icon icon="check" /></template
            ></P-Button>
          </div>
        </P-StepPanel>
      </P-StepPanels>
    </P-Stepper>

    <P-Dialog
      v-model:visible="successDialog"
      modal
      header="Pedido Submetido"
      :style="{ width: '400px' }"
    >
      <div class="text-center">
        <Icon icon="check_circle" class="text-6xl text-green-500 mb-4" />
        <p>O pedido foi submetido com sucesso.</p>
      </div>
      <template #footer>
        <P-Button :label="successButtonLabel" @click="onSuccessContinue" />
      </template>
    </P-Dialog>
  </Container>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";
import { useBrandStore } from "@stores/brands";
import { useMaterialStore } from "@stores/materials";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { useFinishingStore } from "@stores/finishings";
import { useRequestAccess } from "@/composables/useRequestAccess";
import {
  useOrderWizardStore,
  mandatoryGroupIssues,
  ORDER_TYPE_CONFIG,
  type RequestTypeName,
} from "@stores/orderWizard";
import { useRequestStore } from "@stores/requests";
import type { Client } from "@router/backend/services/client/types";
import type { Brand } from "@router/backend/services/brand/types";
import { requestTypes } from "@/maps";

const toast = useToast();
const router = useRouter();
const route = useRoute();

const TITLE = "Novo Pedido";

const authStore = useAuthStore();
const brandStore = useBrandStore();
const materialStore = useMaterialStore();
const finishingGroupStore = useFinishingGroupStore();
const finishingStore = useFinishingStore();
const requestStore = useRequestStore();
const wizard = useOrderWizardStore();

const { canEdit } = useRequestAccess();

const submitTitle = computed(() => (wizard.isEditing ? "Editar Pedido" : TITLE));

// Mirrors @RolesAllowed(COMMERCIAL, ADMIN) on POST /request/price
const canEstimatePrice = computed(() => authStore.isCommercial() || authStore.isAdmin());

const selectedClient = ref<Client | null>(null);
const successDialog = ref(false);
const estimatedPrice = ref<number | null>(null);
const slotStep = ref(1);

const successButtonLabel = computed(() =>
  wizard.isEditing ? "Voltar à pesquisa" : "Ir para o perfil",
);

function onSuccessContinue() {
  successDialog.value = false;
  router.push({ name: wizard.isEditing ? "search" : "profile" });
}

const TYPE_ICONS: Record<RequestTypeName, string> = {
  OneFace: "crop_portrait",
  TwoFaces: "filter_2",
  SimpleShowcase: "storefront",
  LeftShowcase: "view_sidebar",
  RightShowcase: "view_sidebar",
};

const MIRRORED_ICONS: RequestTypeName[] = ["LeftShowcase"];

const TYPE_DESCRIPTIONS: Record<RequestTypeName, string> = {
  OneFace: "Apenas um painel",
  TwoFaces: "Painel frente/verso",
  SimpleShowcase: `Montra de ${ORDER_TYPE_CONFIG.SimpleShowcase.length} faces`,
  LeftShowcase: `Montra de ${ORDER_TYPE_CONFIG.LeftShowcase.length} faces, lateral à esquerda`,
  RightShowcase: `Montra de ${ORDER_TYPE_CONFIG.RightShowcase.length} faces, lateral à direita`,
};

const SLOT_SHORT_LABELS: Record<string, string> = {
  cover: "Frente",
  back: "Verso",
  top: "Topo",
  bottom: "Fundo",
  left: "Esq.",
  right: "Dir.",
  side: "Lat.",
};

function slotShortLabel(key: string) {
  return SLOT_SHORT_LABELS[key] ?? key;
}

function typeFaces(type: RequestTypeName) {
  return ORDER_TYPE_CONFIG[type].map((slot) => slotShortLabel(slot.key));
}

const nonObsoleteMaterials = computed(() => materialStore.materials.filter((m) => !m.obsolete));

const plafondInfo = computed(() => {
  if (!authStore.logged?.credits) return [];
  return authStore.logged.credits;
});

const currentPlafond = computed(() => {
  const brandName = wizard.brand?.name;
  if (!brandName) return null;
  const found = plafondInfo.value.find((p) => p.brand === brandName);
  return found ? found.amount : null;
});

const currentSlotConfig = computed(() => {
  return wizard.slotConfigs[slotStep.value - 1] ?? null;
});

function isSlotValid(key: string) {
  const slot = wizard.slots[key];
  if (!slot || !slot.material || !slot.image) return false;
  if (slot.width <= 0 || slot.height <= 0) return false;
  return mandatoryGroupIssues(slot).length === 0;
}

const allSlotsValid = computed(() => {
  for (const config of wizard.slotConfigs) {
    if (!isSlotValid(config.key)) return false;
  }
  return true;
});

watch(selectedClient, (client) => {
  if (client) {
    wizard.setClient(client);
  } else {
    wizard.clearClient();
  }
});

onMounted(async () => {
  wizard.reset();

  await Promise.all([
    brandStore.getBrands(),
    materialStore.getMaterials(),
    finishingGroupStore.getFinishingGroups(),
    finishingStore.getFinishings(),
  ]);

  if (route.query.id) {
    await loadForEdit(Number(route.query.id));
  }
});

async function loadForEdit(id: number) {
  await requestStore.getAllRequests();
  const existing = requestStore.requests.find((r) => r.id === id);
  if (!existing) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Pedido não encontrado",
      life: 10000,
    });
    return;
  }

  if (!canEdit(existing)) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não tem permissão para editar este pedido",
      life: 10000,
    });
    return;
  }

  wizard.initFromRequest(existing);
  slotStep.value = 1;
  selectedClient.value = existing.client;
}

function selectType(t: RequestTypeName) {
  wizard.setType(t);
  slotStep.value = 1;
}

function onBrandSelect(b: Brand) {
  wizard.setBrand(b);
}

function goToStep(target: number) {
  wizard.step = target;

  if (target === 3) {
    estimatePrice();
  }
}

async function estimatePrice() {
  estimatedPrice.value = null;

  // POST /request/price is restricted to COMMERCIAL/ADMIN — CDR and MANAGER reach
  // the wizard through the edit action, so skip the call instead of getting a 403.
  if (!canEstimatePrice.value) return;

  const request = wizard.buildRequest();
  const response = await requestStore.checkPrice(request);
  if (response.success) {
    estimatedPrice.value = response.content as number;
  } else {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: (response.content as string) || "Não foi possível calcular o custo",
      life: 10000,
    });
  }
}

async function submit() {
  wizard.submitting = true;
  const request = wizard.buildRequest();

  let response;
  if (wizard.isEditing) {
    response = await requestStore.editRequest(wizard.editingId!, request);
  } else {
    response = await requestStore.addRequest(request);
  }

  wizard.submitting = false;

  if (response.success) {
    toast.add({
      severity: "success",
      summary: submitTitle.value,
      detail: wizard.isEditing ? "Pedido editado com sucesso" : "Pedido criado com sucesso",
      life: 10000,
    });
    successDialog.value = true;
  } else {
    toast.add({
      severity: "error",
      summary: submitTitle.value,
      detail: (response.content as string) || "Ocorreu um erro ao submeter o pedido",
      life: 10000,
    });
    console.error(response);
  }
}
</script>
