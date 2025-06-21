<template>
  <v-dialog v-model="enabled" align="center" :width="props.request.trackingCode ? '30%' : '60%'">
    <OpenWaybill
      v-model="enabled"
      v-if="!props.request.trackingCode"
      :request="props.request"
      @created="updated()"
    />
    <v-card v-else>
      <v-card-title>Carta de Porte</v-card-title>
      <v-card-text>
        <p>Uma carta de porte já foi aberta</p>
      </v-card-text>
      <v-card-actions justify="center">
        <v-btn @click="close()"><v-icon>$prev</v-icon>Voltar</v-btn>
        <v-btn @click="showDownload()"><v-icon>$download</v-icon>Descarregar</v-btn>
        <v-btn color="red" @click="showCancel()"><v-icon>$cancel</v-icon>Cancelar</v-btn>
      </v-card-actions>
      <Message v-model="cancelling" :message="cancelMessage()" :action="performCancel" />
      <Message v-model="cancelSuccess" message="Carta de porte cancelada com sucesso" />
      <Message v-model="cancelFailure" message="Ocorreu um erro ao cancelar a carta de porte" />
      <DownloadWaybill v-model="downloading" :request="props.request.id" />
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";

import Backend from "@/router/backend";
import RequestDto from "@models/RequestDto";

const props = defineProps({
  request: {
    type: RequestDto,
    required: true,
  },
});

const emit = defineEmits(["updated"]);

const enabled = defineModel<boolean>();

const cancelling = ref(false);
const cancelFailure = ref(false);
const cancelSuccess = ref(false);

const downloading = ref(false);

function showCancel() {
  cancelling.value = true;
}

async function performCancel() {
  try {
    await Backend.cancelWaybill(props.request.id);
    cancelSuccess.value = true;
    cancelling.value = false;
  } catch (error) {
    cancelFailure.value = true;
    cancelling.value = false;
    console.error(error);
    throw Error(error);
  }
}

function cancelMessage() {
  return "Tem a certeza que pretende cancelar a carta de porte do pedido " + props.request.id + "?";
}

function showDownload() {
  downloading.value = true;
}

function updated() {
  emit("updated");
}

function close() {
  enabled.value = false;
}
</script>
