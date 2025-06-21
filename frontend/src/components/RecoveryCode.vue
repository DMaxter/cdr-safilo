<template>
  <v-dialog v-model="enabled" width="30%">
    <v-card>
      <v-card-title class="justify-center">Recuperar palavra-passe</v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field
            label="Email"
            type="text"
            rounded
            variant="outlined"
            v-model="email"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn @click="close()">Voltar</v-btn>
        <v-btn @click="recover()">Recuperar </v-btn>
      </v-card-actions>
    </v-card>
    <Message
      v-model="success"
      :message="`Foi enviado um email para ${email} com um link para alterar a palavra-passe`"
    />
    <Message v-model="failure" message="Ocorreu um erro ao pedir para alterar a palavra-passe" />
  </v-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";

import Backend from "@/router/backend";

const enabled = defineModel<boolean>();

const email = ref("");
const success = ref(false);
const failure = ref(false);

async function recover() {
  try {
    await Backend.redefineWithToken(email.value);
    success.value = true;
  } catch (error) {
    failure.value = true;
    console.log(error);
    throw Error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>
