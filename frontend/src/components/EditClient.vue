<template>
  <v-dialog v-model="enabled" persistent content-class="rounded-0" max-width="500px">
    <v-card>
      <v-card-title>Editar Cliente</v-card-title>
      <v-card-text>
        <v-container>
          <v-row justify="center">
            <Message v-model="success" message="Cliente editado com sucesso!" />
            <Message v-model="failure" message="Ocorreu um erro a editar o cliente" />
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="Código do cliente"
                disabled
                v-model="props.client.id"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="Nome do cliente"
                required
                v-model="props.client.name"
              ></v-text-field>
            </v-col>
            <v-col cols="12" class="pb-0">
              <v-text-field
                label="Email do cliente"
                required
                v-model="props.client.email"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="NIF do cliente"
                required
                v-model="props.client.fiscalNumber"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="Número de telefone do cliente"
                required
                v-model="props.client.phone"
              ></v-text-field>
            </v-col>
            <v-col cols="12" class="pb-0">
              <v-text-field
                label="Endereço do cliente"
                required
                v-model="props.client.address"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="Código postal do cliente"
                required
                v-model="props.client.postalCode"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field label="Banner do cliente" v-model="props.client.banner"></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="Cidade do cliente"
                required
                v-model="props.client.city"
              ></v-text-field>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-text-field
                label="País do cliente"
                required
                v-model="client.country"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn hide-details color="blue darken-1" text @click="close()"> Voltar </v-btn>
        <v-btn hide-details color="blue darken-1" text @click="updateClient()">Atualizar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import type { PropType } from "vue";

import Backend from "@/router/backend";
import ClientDto from "@models/ClientDto";

const emit = defineEmits(["updated"]);

const enabled = defineModel<boolean>();
const success = ref(false);
const failure = ref(false);

const props = defineProps({
  client: {
    required: true,
    type: Object as PropType<ClientDto>,
  },
});

async function updateClient() {
  try {
    await Backend.editClient(props.client);

    success.value = true;
    emit("updated");
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>

<style lang="sass" scoped></style>
