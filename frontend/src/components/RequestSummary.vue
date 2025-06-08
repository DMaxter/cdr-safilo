<template>
  <v-dialog v-model="enabled" transition="dialog-bottom-transition" max-width="50%">
    <v-card :title="`Pedido ${props.request.id}`">
      <v-card-text>
        <p><b>Tipo: </b>{{ props.request.type.type }}</p>
        <p><b>Marca: </b>{{ props.request.brand.name }}</p>
        <p><b>Estado: </b>{{ statusMap[props.request.status] }}</p>
        <p><b>Cliente: </b>{{ props.request.client.name }}</p>
        <!-- TODO: Add link to client details -->
        <p><b>Morada: </b>{{ props.request.client.address }}, {{ props.request.client.city }}</p>
        <p><b>Comercial: </b>{{ props.request.user }}</p>
        <p><b>Data de criação: </b>{{ props.request.created.toLocaleString("pt-PT") }}</p>
        <p><b>Última atualização: </b>{{ props.request.lastUpdate.toLocaleString("pt-PT") }}</p>
        <p><b>Custo: </b>{{ props.request.cost.toFixed(2) }} Créditos</p>
        <p><b>Materiais: </b>{{ processArray(props.request.getMaterials()) }}</p>
        <p><b>Acabamentos: </b>{{ finishings ? finishings : "Sem acabamentos" }}</p>
        <p>
          <b>Medidas (altura x largura - cm): </b
          >{{ processMeasurements(props.request.getMeasurements()) }}
        </p>
        <p>
          <b>Observações: </b
          >{{ props.request.observations ? props.request.observations : "Sem observações" }}
        </p>
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn @click="close()">Voltar </v-btn>
        <v-btn
          v-if="
            (canManipulate || (user.isCommercial() && request.user == user.user.name)) &&
            statusMap[request.status] == Status.Ordered
          "
          @click="cancel()"
          color="red"
          >Cancelar</v-btn
        >
        <v-btn @click="print()" color="blue">Imprimir</v-btn>
        <v-btn v-if="user.isCdr() || user.isAdmin()" @click="openWaybillDialog()"
          >Carta de Porte</v-btn
        >
        <v-btn @click="openDetails()">Ver pedido</v-btn>
      </v-card-actions>
      <CancelRequest
        v-model="cancelling"
        :request="props.request.id"
        @cancelled="
          emit('updated');
          close();
        "
      />
      <PrintRequest class="only-print" ref="printer" :request="props.request" />
      <Waybill v-model="waybill" :request="props.request" />
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { onUpdated, ref, useTemplateRef } from "vue";
import { useRouter } from "vue-router";

import { statusMap, Status } from "@/maps";
import { useUserStore } from "@stores/user";
import RequestDto from "@models/RequestDto";

const router = useRouter();

const emit = defineEmits(["updated"]);

const props = defineProps({
  request: {
    type: RequestDto,
    required: true,
  },
});

const enabled = defineModel<boolean>();

const finishings = ref("");

const user = useUserStore();
await user.init();

const canManipulate = user.isSafilo() || user.isCdr() || user.isAdmin();
const cancelling = ref(false);

const printer = useTemplateRef("printer");

const waybill = ref(false);

onUpdated(() => {
  finishings.value = processArray(props.request.getFinishings().flat());
});

function cancel() {
  cancelling.value = true;
}

function print() {
  printer.value.handlePrint();
}

function openWaybillDialog() {
  waybill.value = true;
}

function openDetails() {
  router.push("request", { id: props.request.id });
}

function close() {
  enabled.value = false;
}

// Helper functions
function processMeasurements(measurements) {
  return measurements.map((m) => m.join("x")).join(", ");
}

function processArray(arr) {
  return Object.entries(countElems(arr))
    .map(([key, val]) => {
      if (val > 1) {
        return `${key} (${val}x)`;
      } else {
        return key;
      }
    })
    .join(", ");
}

function countElems(arr) {
  return arr.reduce((acc, cur) => {
    acc[cur] = (acc[cur] || 0) + 1;

    return acc;
  }, {});
}
</script>
