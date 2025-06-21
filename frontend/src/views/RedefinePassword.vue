<template>
  <v-container fluid class="d-flex flex-column align-center justify-center fill-height">
    <v-row justify="center" align="center">
      <v-img :src="CDRLogo" contain width="350px"></v-img>
    </v-row>
    <v-row justify="center" align="center">
      <v-col cols="auto">
        <v-card elevation="12" outlined height="400" width="450" style="border-radius: 15px">
          <v-card-title>Redefinir Palavra Passe</v-card-title>
          <v-form @submit.prevent="changePassword">
            <v-card-text>
              <v-text-field
                ref="passwordRef"
                class="mt-8"
                label="Nova Palavra Passe"
                type="password"
                :rules="[required]"
                rounded
                variant="outlined"
                v-model="password"
              ></v-text-field>
              <v-text-field
                ref="repeatPasswordRef"
                class="mt-8"
                label="Confirmar Palavra Passe"
                type="password"
                :rules="repeatPasswordRules"
                rounded
                variant="outlined"
                v-model="repeatPassword"
              ></v-text-field>
            </v-card-text>
          </v-form>
          <v-card-actions class="justify-center">
            <v-btn
              :disabled="!canChange"
              width="33%"
              class="customGradient"
              type="submit"
              @click="changePassword()"
            >
              Confirmar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <Message v-model="success" message="Palavra-passe alterada com sucesso!" />
    <Message v-model="failure" message="Ocorreu um erro ao alterar a palavra-passe" />
  </v-container>
</template>

<script lang="ts" setup>
import { computed, ref, useTemplateRef } from "vue";
import { useRoute, useRouter } from "vue-router";

import Backend from "@/router/backend";
import CDRLogo from "@/assets/logo.png";
import { checkAllRefsValid, required } from "@/rules";

const router = useRouter();
const route = useRoute();

const password = ref("");
const repeatPassword = ref("");

const success = ref(false);
const failure = ref(false);

const repeatPasswordRules = [
  required,
  (value) => value == password.value || "Palavras-passe não são iguais!",
];

const passwordRef = useTemplateRef("passwordRef");
const repeatPasswordRef = useTemplateRef("repeatPasswordRef");

const canChange = computed(() => checkAllRefsValid([passwordRef, repeatPasswordRef]));

async function changePassword() {
  if (password.value === repeatPassword.value) {
    try {
      await Backend.changePasswordWithToken(route.query.email, password.value, route.query.token);
      setTimeout(() => router.push({ name: "profile" }), 4000);
      success.value = true;
    } catch (error) {
      failure.value = true;
      console.log(error);
      throw Error(error);
    }
  }
}
</script>
