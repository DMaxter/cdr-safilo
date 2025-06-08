<template>
  <Message v-model="enabled" :message="cancelMessage()" :action="performCancel" />
  <Message v-model="cancelSuccess" message="Pedido cancelado com sucesso" />
  <Message v-model="cancelFailure" message="Ocorreu um erro ao cancelar o pedido" />
</template>

<script lang="ts" setup>
import { ref } from "vue";

import Backend from "@/router/backend";

const emit = defineEmits(["cancelled"]);

const props = defineProps({
  request: {
    type: Number,
    required: true,
  },
});

const enabled = defineModel<boolean>();

const cancelSuccess = ref(false);
const cancelFailure = ref(false);

function cancelMessage() {
  return "Tem a certeza que pretende cancelar o pedido " + props.request + "?";
}

async function performCancel() {
  try {
    await Backend.cancelRequest(props.request);
    cancelSuccess.value = true;
    close();

    emit("cancelled");
  } catch (error) {
    cancelSuccess.value = false;
    close();

    throw Error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>
