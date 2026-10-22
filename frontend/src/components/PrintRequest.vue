<template>
  <div ref="printable">
    <p
      v-if="props.request.status === Status.Cancelled"
      class="bg-red-500 text-center font-bold text-8xl text-white"
    >
      PEDIDO CANCELADO
    </p>
    <p
      v-if="props.request.status === Status.Done"
      class="bg-green-500 text-center font-bold text-8xl text-white"
    >
      PEDIDO FINALIZADO
    </p>
    <p class="text-center text-xl font-bold">
      Pedido {{ props.request.id }} -
      {{ requestTypeLabel }}
    </p>
    <p><b>Cliente:</b> {{ props.request.client?.name ?? "Desconhecido" }}</p>
    <p><b>Morada:</b> {{ props.request.client?.address ?? "—" }}</p>
    <p>
      <b>Observações:</b>
      {{ props.request.observations ? props.request.observations : "Sem observações" }}
    </p>
    <p><b>Quantidade:</b> {{ props.request.amount }}</p>
    <p><b>Data do pedido:</b> {{ props.request.created?.toLocaleString("pt-PT") ?? "—" }}</p>
    <p>
      <b>Data da última modificação:</b>
      {{ props.request.lastUpdate?.toLocaleString("pt-PT") ?? "—" }}
    </p>
    <p><b>Marca:</b> {{ props.request.brand?.name ?? "—" }}</p>
    <PrintSlot v-if="coverSlot" :slot-data="coverSlot" name="Frente" />
    <PrintSlot v-if="backSlot" :slot-data="backSlot" name="Verso" />
    <PrintSlot v-if="topSlot" :slot-data="topSlot" name="Topo" />
    <PrintSlot v-if="bottomSlot" :slot-data="bottomSlot" name="Fundo" />
    <PrintSlot v-if="leftSlot" :slot-data="leftSlot" name="Esquerda" />
    <PrintSlot v-if="rightSlot" :slot-data="rightSlot" name="Direita" />
    <PrintSlot v-if="sideSlot" :slot-data="sideSlot" name="Lateral" />
  </div>
</template>

<script lang="ts" setup>
import { useVueToPrint } from "vue-to-print";
import { computed, useTemplateRef, type ShallowRef } from "vue";

import { requestTypes } from "@/maps";
import {
  Request,
  RequestSlot,
  Showcase,
  Status,
  TwoFaces,
  OneFace,
} from "@router/backend/services/request/types";

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "printed"): void;
}>();

const printable = useTemplateRef<HTMLElement>("printable");

const { handlePrint } = useVueToPrint({
  content: printable as ShallowRef<HTMLElement>,
  documentTitle: `Pedido_Safilo_${props.request.id}`,
  onAfterPrint: () => emit("printed"),
});

const requestTypeLabel = computed(
  () =>
    requestTypes.find((type) => type.name === props.request.type?.type)?.value ||
    "Tipo desconhecido",
);

function slotOf(slot: RequestSlot | null | undefined): RequestSlot | undefined {
  return slot ?? undefined;
}

const coverSlot = computed(() => {
  const type = props.request.type;

  if (type instanceof OneFace || type instanceof TwoFaces) {
    return slotOf(type.cover);
  }

  return undefined;
});

const backSlot = computed(() => {
  const type = props.request.type;

  return type instanceof TwoFaces ? slotOf(type.back) : undefined;
});

const topSlot = computed(() =>
  props.request.type instanceof Showcase ? slotOf(props.request.type.top) : undefined,
);

const bottomSlot = computed(() =>
  props.request.type instanceof Showcase ? slotOf(props.request.type.bottom) : undefined,
);

const leftSlot = computed(() =>
  props.request.type instanceof Showcase ? slotOf(props.request.type.left) : undefined,
);

const rightSlot = computed(() =>
  props.request.type instanceof Showcase ? slotOf(props.request.type.right) : undefined,
);

const sideSlot = computed(() =>
  props.request.type instanceof Showcase ? slotOf(props.request.type.side) : undefined,
);

defineExpose({ handlePrint });
</script>
