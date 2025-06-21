<template>
  <v-dialog v-model="enabled" width="20%" min-width="300px">
    <v-card>
      <v-card-title>Descarregar carta de porte</v-card-title>
      <v-card-text>
        <v-select
          ref="formatRef"
          :rules="[required]"
          variant="underlined"
          :items="labels"
          label="Formato"
          v-model="format"
        ></v-select>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="close()"><v-icon>$prev</v-icon>Voltar</v-btn>
        <v-btn :disabled="!canDownload" @click="download()"
          ><v-icon>$download</v-icon>Descarregar</v-btn
        >
      </v-card-actions>
    </v-card>
    <Message v-model="downloadFailure" message="Ocorreu um erro ao descarregar a carta de porte" />
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, ref, useTemplateRef } from "vue";

import Backend from "@/router/backend";
import { checkAllRefsValid, required } from "@/rules";

const props = defineProps({
  request: {
    required: true,
    type: Number,
  },
});

const labels = await Backend.getLabelFormats();

const format = ref("");
const formatRef = useTemplateRef("formatRef");

const enabled = defineModel<boolean>();

const downloadFailure = ref(false);

const canDownload = computed(() => checkAllRefsValid([formatRef]));

async function download() {
  try {
    await Backend.downloadWaybill(props.request, format.value);
  } catch (error) {
    downloadFailure.value = true;
    console.log(error);
    throw Error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>
