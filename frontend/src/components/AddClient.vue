<template>
  <v-dialog v-model="enabled" persistent max-width="500px">
    <template v-slot:activator="{ props }">
      <v-btn
        height="50"
        width="145"
        class="mb-3 mt-11 ml-3 customGradient text-white"
        v-bind="props"
      >
        <span style="font-size: 11px">Adicionar Cliente </span></v-btn
      >
    </template>
    <v-card>
      <v-card-title class="justify-center pt-2 pb-1">
        <span class="text-h5 pt-1 pb-1"> Adicionar Cliente </span>
      </v-card-title>
      <v-card-text class="pb-0 pt-0">
        <v-container>
          <v-row justify="center">
            <Message v-model="success" message="Cliente adicionado com sucesso!" />
            <Message v-model="failure" message="Ocorreu um erro a adicionar o cliente" />
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field label="Código do cliente" required v-model="client.id"></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field label="Nome do cliente" required v-model="client.name"></v-text-field>
            </v-col>
            <v-col cols="12" class="pb-0 pt-0">
              <v-text-field label="Email do cliente" required v-model="client.email"></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field
                label="NIF do cliente"
                required
                v-model="client.fiscalNumber"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field
                label="Número de telefone do cliente"
                required
                v-model="client.phone"
              ></v-text-field>
            </v-col>
            <v-col cols="12" class="pb-0 pt-0">
              <v-text-field
                label="Endereço do cliente"
                required
                v-model="client.address"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field
                label="Código postal do cliente"
                required
                v-model="client.postalCode"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field label="Banner do cliente" v-model="client.banner"></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field label="Cidade do cliente" required v-model="client.city"></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0 pt-0">
              <v-text-field
                label="País do cliente"
                required
                v-model="client.country"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn @click="close()"><v-icon>$prev</v-icon>Voltar</v-btn>
        <v-btn color="blue darken-1" @click="addClient()">Adicionar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";

import Backend from "@/router/backend";
import ClientDto from "@models/ClientDto";

const emit = defineEmits(["updated"]);

const enabled = ref(false);
const success = ref(false);
const failure = ref(false);

const client = reactive(new ClientDto());

async function addClient() {
  try {
    await Backend.addClient(client);

    success.value = true;
    emit("updated");
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}

function close() {
  client.clear();

  enabled.value = false;
}
</script>
