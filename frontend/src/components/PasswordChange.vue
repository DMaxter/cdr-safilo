<template>
  <P-Dialog modal class="max-w-95/100" v-model:visible="enabled">
    <template #header>Alterar Palavra-passe</template>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        fluid
        id="current"
        type="password"
        ref="current"
        v-model="currentPassword"
      />
      <label for="current">Palavra-passe atual</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        fluid
        id="new"
        type="password"
        ref="new"
        v-model="newPassword"
      />
      <label for="new">Nova palavra-passe</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        fluid
        id="repeat"
        label="Repetir nova palavra-passe"
        type="password"
        ref="repeat"
        v-model="repeatNewPassword"
      />
      <label for="repeat">Repetir nova palavra-passe</label>
    </P-FloatLabel>
    <template #footer>
      <P-Button class="flex flex-row" @click="close">
        <Icon icon="prev" />
        Voltar
      </P-Button>
      <P-Button
        class="flex flex-row"
        @click="changePassword">
        <!--:disabled="!canChangePassword"-->
        Confirmar
        <Icon icon="next" />
      </P-Button>
    </template>
    <!--<Message v-model="success" message="A palavra-passe foi alterada com sucesso" />-->
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref, useTemplateRef } from "vue";

import { useAuthStore } from "@stores/auth";
import { checkAllRefsValid, required } from "@/rules";

const enabled = defineModel<boolean>();
const success = ref(false);
const failure = ref(false);

const TITLE = "Alteração de palavra-passe";

const authStore = useAuthStore();
const toast = useToast();

// TODO: Validation rules
const currentPassword = ref("");
const newPassword = ref("");
const repeatNewPassword = ref("");

const newPasswordRules = [
  required,
  (value: string) =>
    value != currentPassword.value || "Palavra-passe atual e a nova têm de ser diferentes",
];
const repeatPasswordRules = [
  required,
  (value: string) => value == newPassword.value || "Palavras-passe não são iguais!",
];

const currentRef = useTemplateRef<string>("current");
const newRef = useTemplateRef<string>("new");
const repeatRef = useTemplateRef<string>("repeat");

const canChangePassword = computed(() => checkAllRefsValid([currentRef, newRef, repeatRef]));

async function changePassword() {
  if (canChangePassword) {
    if (newPassword.value != "" && repeatNewPassword.value != "") {
      if (newPassword.value == repeatNewPassword.value) {
        const response = await authStore.changePassword(currentPassword.value, newPassword.value);

        if (response.success) {
          toast.add({
            severity: "success",
            summary: TITLE,
            detail: "A palavra-passe foi alterada com sucesso"
          });
        } else {
          toast.add({
            severity: "error",
            summary: TITLE,
            detail: "Não foi possível alterar a palavra-passe",
            life: 10000
          });
          console.error(response);
        }
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

<style lang="scss" scoped>
.field {
  margin-top: 10px;
}
</style>
