<template>
  <v-dialog v-model="enabled" persistent content-class="rounded-0" max-width="500px">
    <template v-slot:activator="{ props }">
      <v-btn height="50" width="150" class="mt-11 customGradient text-white" v-bind="props">
        <span style="font-size: 11px">Carregar Clientes </span>
      </v-btn>
    </template>
    <v-card tile>
      <v-card-title class="justify-center">
        <span>Carregar Clientes</span>
      </v-card-title>
      <div align="center">
        <Message v-model="success" message="Clientes carregados com sucesso!" />
        <Message v-model="failure" message="Ocorreu um erro ao carregar o ficheiro de clientes" />
        <v-icon size="100" style="cursor: pointer" @click="handleFileImport">$upload</v-icon>
        <input
          ref="uploader"
          class="d-none"
          type="file"
          name="file"
          accept="text/csv"
          @change="onFileChanged"
        />
        <p v-if="!isSelected">Selecione um ficheiro</p>
        <p v-if="isSelected">Ficheiro: {{ selectedFile.name }}</p>
      </div>

      <v-card-actions class="justify-center">
        <v-btn color="blue darken-1" text @click="close()"> Voltar </v-btn>
        <v-btn color="blue darken-1" text v-if="isSelected" @click="addClients()">
          Adicionar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, reactive, ref, useTemplateRef } from "vue";

import Backend from "@/router/backend";

const emit = defineEmits(["updated"]);

const enabled = ref(false);
const failure = ref(false);
const success = ref(false);

const uploader = useTemplateRef("uploader");
const selectedFile = reactive({ name: "", size: 0 });

const isSelected = computed(() => selectedFile.size != 0);

async function addClients() {
  try {
    await Backend.addClients(selectedFile);
    success.value = true;
    emit("updated");
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}

function handleFileImport() {
  uploader.value.click();
}

function close() {
  selectedFile.name = "";
  selectedFile.size = 0;

  enabled.value = false;
}

function onFileChanged(e) {
  const file = e.target.files[0];
  selectedFile.name = file.name;
  selectedFile.size = file.size;
}
</script>

<style lang="scss" scoped></style>
