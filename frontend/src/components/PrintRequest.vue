<template>
  <div ref="printable">
    <p v-if="Status.Cancelled === props.request.status!!" class="bg-red-500 text-center font-bold text-8xl text-white">PEDIDO CANCELADO</p>
    <p v-if="Status.Done === props.request.status!!" class="bg-green-500 text-center font-bold text-8xl text-white">PEDIDO FINALIZADO</p>
    <p class="text-center text-xl font-bold">Pedido {{ props.request.id }} - {{ requestTypes.find((type) => type.name === props.request.type!!.type)?.value || "Tipo desconhecido" }}</p>
    <p><b>Cliente:</b> {{ props.request.client!!.name }}</p>
    <p><b>Morada:</b> {{ props.request.client!!.address }}</p>
    <p>
      <b>Observações:</b> {{ props.request.observations ? props.request.observations : "Sem observações" }}
    </p>
    <p><b>Quantidade:</b> {{ props.request.amount }}</p>
    <p><b>Data do pedido:</b> {{ props.request.created!!.toLocaleString("pt-PT") }}</p>
    <p><b>Data da última modificação:</b> {{ props.request.lastUpdate!!.toLocaleString("pt-PT") }}</p>
    <p><b>Marca:</b> {{ props.request.brand!!.name }}</p>
    <PrintSlot
      v-if="['OneFace', 'TwoFaces'].includes(props.request.type!!.type!!)"
      :slot="(props.request.type!! as OneFace).cover"
      name="Frente"
    />
    <PrintSlot
      v-if="props.request.type!!.type === 'TwoFaces'"
      :slot="(props.request.type!! as TwoFaces).back"
      name="Verso"
    />
    <PrintSlot
      v-if="
        ['SimpleShowcase', 'LeftShowcase', 'RightShowcase'].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).top"
      name="Topo"
    />
    <PrintSlot
      v-if="
        ['SimpleShowcase', 'LeftShowcase', 'RightShowcase'].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).bottom"
      name="Fundo"
    />
    <PrintSlot
      v-if="
        ['SimpleShowcase', 'LeftShowcase', 'RightShowcase'].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).left"
      name="Esquerda"
    />
    <PrintSlot
      v-if="
        ['SimpleShowcase', 'LeftShowcase', 'RightShowcase'].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).right"
      name="Direita"
    />
    <PrintSlot
      v-if="
        ['LeftShowcase', 'RightShowcase'].includes(props.request.type!!.type!!)
      "
      :slot="(props.request.type!! as LeftShowcase).side"
      name="Lateral"
    />
  </div>
</template>

<script lang="ts" setup>
import { useVueToPrint } from "vue-to-print";
import { useTemplateRef, type ShallowRef } from "vue";

import { requestTypes } from "@/maps";
import {
  LeftShowcase,
  OneFace,
  Request,
  RightShowcase,
  SimpleShowcase,
  Status,
  TwoFaces,
} from "@router/backend/services/request/types";

const props = defineProps<{
  request: Request,
}>();

const printable = useTemplateRef<HTMLElement>("printable");

const { handlePrint } = useVueToPrint({
  content: printable as ShallowRef<HTMLElement>,
  documentTitle: `Pedido_Safilo_${props.request.id}`,
});

defineExpose({ handlePrint });
</script>
