<template>
  <v-container ref="printable">
    <p v-if="Status.Cancelled === props.request.status!!" align="center" class="bg-red">
      <b class="text-h2 text-white">PEDIDO CANCELADO</b><br />
    </p>
    <p v-if="Status.Done === props.request.status!!" align="center" class="bg-red">
      <b class="text-h2 text-white">PEDIDO FINALIZADO</b><br />
    </p>
    <p align="center">
      <b class="text-h5">Pedido {{ props.request.id }} - {{ props.request.type!!.type }}</b>
    </p>
    <p>Cliente: {{ props.request.client!!.name }}</p>
    <p>Morada: {{ props.request.client!!.address }}</p>
    <p>
      Observações: {{ props.request.observations ? props.request.observations : "Sem observações" }}
    </p>
    <p>Quantidade: {{ props.request.amount }}</p>
    <p>Data do pedido: {{ props.request.created!!.toLocaleString("pt-PT") }}</p>
    <p>Data de última modificação: {{ props.request.lastUpdate!!.toLocaleString("pt-PT") }}</p>
    <p>Marca: {{ props.request.brand!!.name }}</p>
    <PrintSlot
      v-if="[RequestType.OneFace, RequestType.TwoFaces].includes(props.request.type!!.type!!)"
      :slot="(props.request.type!! as OneFace).cover"
      name="Frente"
    />
    <PrintSlot
      v-if="props.request.type!!.type === RequestType.TwoFaces"
      :slot="(props.request.type!! as TwoFaces).back"
      name="Verso"
    />
    <PrintSlot
      v-if="
        [RequestType.SimpleShowcase, RequestType.LeftShowcase, RequestType.RightShowcase].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).top"
      name="Topo"
    />
    <PrintSlot
      v-if="
        [RequestType.SimpleShowcase, RequestType.LeftShowcase, RequestType.RightShowcase].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).bottom"
      name="Fundo"
    />
    <PrintSlot
      v-if="
        [RequestType.SimpleShowcase, RequestType.LeftShowcase, RequestType.RightShowcase].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).left"
      name="Esquerda"
    />
    <PrintSlot
      v-if="
        [RequestType.SimpleShowcase, RequestType.LeftShowcase, RequestType.RightShowcase].includes(
          props.request.type!!.type!!,
        )
      "
      :slot="(props.request.type!! as SimpleShowcase).right"
      name="Direita"
    />
    <PrintSlot
      v-if="
        [RequestType.LeftShowcase, RequestType.RightShowcase].includes(props.request.type!!.type!!)
      "
      :slot="(props.request.type!! as LeftShowcase).side"
      name="Lateral"
    />
  </v-container>
</template>

<script lang="ts" setup>
import { useVueToPrint } from "vue-to-print";
import { useTemplateRef, type ShallowRef } from "vue";

import RequestDto from "@models/dto/RequestDto";
import { RequestType } from "@models/dto/RequestType";
import {
  OneFace,
  TwoFaces,
  SimpleShowcase,
  LeftShowcase,
  RightShowcase,
} from "@models/dto/RequestTypeDto";
import { Status } from "@models/dto/RequestStatus";

const props = defineProps({
  request: {
    type: RequestDto,
    required: true,
  },
});

const printable = useTemplateRef<HTMLElement>("printable");

const { handlePrint } = useVueToPrint({
  content: printable as ShallowRef<HTMLElement>,
  documentTitle: `Pedido_Safilo_${props.request.id}`,
});

defineExpose({ handlePrint });
</script>
