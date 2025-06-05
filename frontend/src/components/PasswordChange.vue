<template>
  <v-dialog v-model="enabled" persistent content-class="rounded-0" max-width="500px">
    <v-card>
      <v-card-title>Alterar Palavra-passe</v-card-title>
      <v-card-text>
        <v-text-field
          class="mt-2"
          label="Palavra-passe"
          type="password"
          ref="current"
          :rules="[required]"
          outlined
          hide-details="auto"
          v-model="currentPassword"
        ></v-text-field>
        <v-text-field
          class="mt-2"
          label="Nova palavra-passe"
          type="password"
          ref="new"
          :rules="newPasswordRules"
          outlined
          hide-details="auto"
          v-model="newPassword"
        ></v-text-field>
        <v-text-field
          class="mt-2"
          label="Repetir nova palavra-passe"
          type="password"
          ref="repeat"
          :rules="repeatPasswordRules"
          outlined
          hide-details="auto"
          v-model="repeatNewPassword"
        ></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-btn class="d-flex flex-row customGradient" small tile dark @click="close">
          <v-icon>$prev</v-icon>
          Voltar
        </v-btn>
        <v-btn class="d-flex flex-column customGradient" small tile dark :disabled="!canChangePassword" @click="changePassword">
          Confirmar<v-icon>$next</v-icon>
        </v-btn>
      </v-card-actions>
    </v-card>
    <Message v-model="success" message="A palavra-passe foi alterada com sucesso" />
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, onUpdated, ref, useTemplateRef } from "vue";

import Backend from "@/router/backend";

const enabled = defineModel<boolean>();
const success = ref(false);
const failure = ref(false);

const currentPassword = ref("");
const newPassword = ref("");
const repeatNewPassword = ref("");

const required = (value => !!value || "Campo obrigatório");
const newPasswordRules = [required, value => value != currentPassword.value || "Palavra-passe atual e a nova têm de ser diferentes"];
const repeatPasswordRules = [required, value => value == newPassword.value || "Palavras-passe não são iguais!"];

const currentRef = useTemplateRef("current");
const newRef = useTemplateRef("new");
const repeatRef = useTemplateRef("repeat");

onUpdated(() => {
  if (enabled.value) {
    currentRef.value.focus();
  }
})

const canChangePassword = computed(() => {
  try {
    return currentRef.value.isValid && newRef.value.isValid && repeatRef.value.isValid
  } catch (error) {
    return false;
  }
});

async function changePassword() {
  if (newPassword.value != "" && repeatNewPassword.value != "") {
    if (newPassword.value == repeatNewPassword.value) {
      try {
        await Backend.changePassword(currentPassword, newPassword);
      } catch (error) {
        console.error(error);
        throw Error(error)
      }
    }
  }
}

function close() {
  currentPassword.value = "";
  newPassword.value = "";
  repeatNewPassword.value = "";
  enabled.value = false;
}
</script>
