<template>
  <Container>
    <div class="flex flex-wrap items-center justify-between gap-3 mb-4">
      <h2 class="font-extrabold text-3xl">Pedido {{ id ?? "" }}</h2>
      <P-Tag v-if="request" rounded :class="statusColorClass">
        <Icon :icon="statusIcon" />
        <span>{{ statusLabel }}</span>
      </P-Tag>
    </div>

    <div v-if="loading" class="py-10 text-center text-gray-500">A carregar pedido...</div>

    <P-Message v-else-if="!request" severity="warn" :closable="false">
      Não foi possível encontrar o pedido {{ id }}.
    </P-Message>

    <div v-else class="flex flex-col gap-4">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div>
          <label>Tipo:</label>
          <div class="font-bold">{{ typeName }}</div>
        </div>
        <div>
          <label>Marca:</label>
          <div class="font-bold">{{ request.brand?.name ?? "—" }}</div>
        </div>
        <div>
          <label>Quantidade:</label>
          <div class="font-bold">{{ request.amount }}</div>
        </div>
        <div>
          <label>Aplicação:</label>
          <div class="font-bold">{{ request.application ? "Sim" : "Não" }}</div>
        </div>

        <div>
          <label>Custo:</label>
          <div class="font-bold">{{ request.cost.toFixed(2) }} Créditos</div>
        </div>
        <div>
          <label>Comercial:</label>
          <div class="font-bold">{{ request.user }}</div>
        </div>
        <div>
          <label>Data de criação:</label>
          <div class="font-bold">{{ formatDate(request.created) }}</div>
        </div>
        <div>
          <label>Última atualização:</label>
          <div class="font-bold">{{ formatDate(request.lastUpdate) }}</div>
        </div>

        <div v-if="isCdr && request.status !== Status.Cancelled" class="sm:col-span-2">
          <label>Carta de Porte:</label>
          <div class="font-bold">
            {{ request.trackingCode !== null ? request.trackingCode : "Não existe" }}
          </div>
        </div>
      </div>

      <div class="border rounded p-3">
        <label>Cliente:</label>
        <div class="font-bold">
          <router-link :to="{ name: 'client', query: { id: request.client?.id } }">
            {{ request.client?.name ?? "Desconhecido" }}<Icon icon="open_in_new" />
          </router-link>
        </div>
        <div v-if="request.client" class="text-sm">
          {{ request.client.banner ? "Banner " + request.client.banner + " | " : ""
          }}{{ request.client.address }}, {{ request.client.postalCode }} {{ request.client.city }}
        </div>
      </div>

      <div class="flex flex-col gap-3">
        <div
          v-for="entry in slots"
          :key="entry.key"
          class="flex items-center gap-3 border rounded p-3"
        >
          <img
            v-if="entry.slot?.image"
            :src="entry.slot.image.link"
            class="h-20 w-20 object-contain rounded border bg-gray-50 shrink-0"
            alt="Imagem da zona"
          />
          <div
            v-else
            class="h-20 w-20 flex items-center justify-center text-gray-300 rounded border bg-gray-50 shrink-0"
          >
            <Icon icon="image" class="text-3xl" />
          </div>
          <div class="min-w-0">
            <div class="font-bold">{{ entry.label }}</div>
            <div class="text-sm">
              Material: {{ entry.slot?.material?.name ?? "—" }} | Medidas:
              {{ entry.slot?.measurements?.height ?? 0 }}x{{ entry.slot?.measurements?.width ?? 0 }}
              cm
            </div>
            <div class="flex flex-wrap gap-1 mt-1">
              <P-Chip
                v-for="finishing in entry.slot?.finishings ?? []"
                :key="finishing.id"
                :label="finishing.name"
              />
              <span
                v-if="(entry.slot?.finishings ?? []).length === 0"
                class="text-sm text-gray-500"
              >
                Sem acabamentos
              </span>
            </div>
          </div>
        </div>
      </div>

      <div>
        <label>Observações:</label>
        <div class="font-bold">{{ request.observations || "Sem observações" }}</div>
      </div>
    </div>

    <template #actions v-if="request">
      <P-Button v-if="canCancel" label="Cancelar" severity="danger" @click="confirmCancel()" />
      <P-Button label="Voltar" severity="secondary" @click="router.back()" />
      <P-Button label="Imprimir" severity="info" @click="print()">
        <template #icon><Icon icon="print" /></template>
      </P-Button>
      <P-Button
        v-if="isCdr && request.status !== Status.Cancelled"
        label="Carta de Porte"
        severity="secondary"
        @click="waybill = true"
      >
        <template #icon><Icon icon="delivery_truck_speed" /></template>
      </P-Button>
    </template>

    <PrintRequest v-if="request" class="only-print" ref="printer" :request="request" />
    <Waybill v-if="request" v-model="waybill" :request="request" @opened="refreshRequest" />
  </Container>
</template>

<script lang="ts" setup>
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, useTemplateRef, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";

import PrintRequestComponent from "@components/PrintRequest.vue";
import { Status, type Request, type RequestSlot } from "@router/backend/services/request/types";
import { requestTypes, statusItems } from "@/maps";
import { ORDER_TYPE_CONFIG, type RequestTypeName } from "@stores/orderWizard";
import { useAuthStore } from "@stores/auth";
import { useRequestStore } from "@stores/requests";
import { getStatusClass, getStatusIcon } from "@/utils";

const TITLE = "Pedido";

const route = useRoute();
const router = useRouter();

const confirm = useConfirm();
const toast = useToast();

const authStore = useAuthStore();
const requestStore = useRequestStore();

const isCdr = authStore.isCdr() || authStore.isAdmin();
const canManipulate = authStore.isSafilo() || authStore.isCdr() || authStore.isAdmin();

const request = ref<Request | null>(null);
const loading = ref(true);
const waybill = ref(false);

const printer = useTemplateRef<typeof PrintRequestComponent>("printer");

const id = computed(() => {
  const raw = route.params.id;
  const parsed = Number(Array.isArray(raw) ? raw[0] : raw);
  return isNaN(parsed) ? null : parsed;
});

const request_ = computed(() =>
  id.value === null ? null : (requestStore.requests.find((r) => r.id === id.value) ?? null),
);

watchEffect(() => {
  request.value = request_.value;
});

const typeName = computed(
  () =>
    requestTypes.find((t) => t.name === request.value?.type?.type)?.value ?? "Tipo desconhecido",
);

const slots = computed(() => {
  const type = request.value?.type;

  if (!type) {
    return [];
  }

  const configs = ORDER_TYPE_CONFIG[type.type as RequestTypeName] ?? [];
  const holder = type as unknown as Record<string, RequestSlot | null>;

  return configs
    .map((config) => ({ ...config, slot: holder[config.key] ?? null }))
    .filter((entry) => entry.slot !== null);
});

const statusIcon = computed(() => getStatusIcon(request.value?.status ?? Status.Ordered));
const statusColorClass = computed(() => getStatusClass(request.value?.status ?? Status.Ordered));
const statusLabel = computed(
  () => statusItems.find((s) => s.value === request.value?.status)?.name ?? "Estado desconhecido",
);

const canCancel = computed(
  () =>
    request.value !== null &&
    request.value.status === Status.Ordered &&
    (canManipulate || (authStore.isCommercial() && request.value.user === authStore.logged?.name)),
);

function formatDate(value: Date | null) {
  return value ? new Date(value).toLocaleString("pt-PT") : "—";
}

function print() {
  printer.value?.handlePrint();
}

function refreshRequest() {
  requestStore.getAllRequests(true);
}

function confirmCancel() {
  if (!request.value) {
    return;
  }

  const current = request.value;

  confirm.require({
    message: `Tem a certeza que pretende cancelar o pedido ${current.id} efetuado por ${current.user} para o cliente ${current.client?.name ?? "Desconhecido"}?`,
    header: "Confirmar cancelamento de pedido",
    rejectProps: {
      label: "Abortar cancelamento",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Confirmar cancelamento",
      severity: "danger",
    },
    accept: cancelRequest,
  });
}

async function cancelRequest() {
  if (!request.value) {
    return;
  }

  const response = await requestStore.cancelRequest(request.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Pedido cancelado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao cancelar o pedido",
      life: 10000,
    });
    console.error(response);
  }
}

onMounted(async () => {
  if (id.value === null) {
    loading.value = false;
    return;
  }

  if (request_.value) {
    loading.value = false;
    return;
  }

  const response = await requestStore.getAllRequests(true);

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: response.content ?? "Ocorreu um erro ao carregar o pedido",
      life: 10000,
    });
    console.error(response);
  }

  loading.value = false;
});
</script>
