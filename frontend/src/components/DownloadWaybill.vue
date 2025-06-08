<template>
  <v-dialog v-model="enabled">
    <v-card>
      <v-select variant="underline">Formato</v-select>
    </v-card>
    <Message v-model="downloadFailure" message="Ocorreu um erro ao descarregar a carta de porte" />
  </v-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";

const enabled = defineModel<boolean>();

const downloadFailure = ref(false);

async function download() {
  try {
    await Backend.downloadWaybill()
  } catch (error) {
    downloadFailure.value = true;
    console.log(error);
    throw Error(error);
  }
}
</script>
