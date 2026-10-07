<template>
  <div class="flex flex-col gap-4">
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
        <label>Estado:</label>
        <div>
          <P-Tag rounded :class="statusColorClass">
            <Icon :icon="statusIcon" />
            <span>{{ statusLabel }}</span>
          </P-Tag>
        </div>
      </div>
      <div v-if="canSeeWaybill(request)">
        <label>Carta de Porte:</label>
        <div class="font-bold">{{ request.trackingCode ?? "Não existe" }}</div>
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
        <div class="font-bold">{{ request.cost?.toFixed(2) ?? "—" }} Créditos</div>
      </div>
      <div>
        <label>Comercial:</label>
        <div class="font-bold">{{ request.user ?? "—" }}</div>
      </div>
      <div>
        <label>Data de criação:</label>
        <div class="font-bold">{{ formatDate(request.created) }}</div>
      </div>
      <div>
        <label>Última atualização:</label>
        <div class="font-bold">{{ formatDate(request.lastUpdate) }}</div>
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

    <div v-if="slots.length > 0" class="flex flex-col gap-3">
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
            <span v-if="(entry.slot?.finishings ?? []).length === 0" class="text-sm text-gray-500">
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
</template>

<script lang="ts" setup>
import { computed } from "vue";

import { Status, type Request, type RequestSlot } from "@router/backend/services/request/types";
import { requestTypes, statusItems } from "@/maps";
import { ORDER_TYPE_CONFIG, type RequestTypeName } from "@stores/orderWizard";
import { useRequestAccess } from "@/composables/useRequestAccess";
import { getStatusClass, getStatusIcon } from "@/utils";

const props = defineProps<{
  request: Request;
}>();

const { canSeeWaybill } = useRequestAccess();

const typeName = computed(
  () =>
    requestTypes.find((type) => type.name === props.request.type?.type)?.value ??
    "Tipo desconhecido",
);

// Slot cards are derived from the same ORDER_TYPE_CONFIG the order wizard uses,
// so labels here and in the wizard can never drift apart.
const slots = computed(() => {
  const type = props.request.type;

  if (!type) {
    return [];
  }

  const configs = ORDER_TYPE_CONFIG[type.type as RequestTypeName] ?? [];
  const holder = type as unknown as Record<string, RequestSlot | null>;

  return configs
    .map((config) => ({ ...config, slot: holder[config.key] ?? null }))
    .filter((entry) => entry.slot !== null);
});

const statusIcon = computed(() => getStatusIcon(props.request.status ?? Status.Ordered));
const statusColorClass = computed(() => getStatusClass(props.request.status ?? Status.Ordered));
const statusLabel = computed(
  () =>
    statusItems.find((item) => item.value === props.request.status)?.name ?? "Estado desconhecido",
);

function formatDate(value: Date | null) {
  return value ? new Date(value).toLocaleString("pt-PT") : "—";
}
</script>
