<template>
  <P-Dialog v-model:visible="enabled" modal class="max-w-95/100">
    <template #header>Alterar Palavra-passe</template>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="current"
        v-model="currentPassword"
        fluid
        type="password"
        :invalid="!!errors.current"
      />
      <label for="current">Palavra-passe atual</label>
    </P-FloatLabel>
    <P-Message v-if="errors.current" severity="error" size="small" variant="simple">
      {{ errors.current }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="new" v-model="newPassword" fluid type="password" :invalid="!!errors.new" />
      <label for="new">Nova palavra-passe</label>
    </P-FloatLabel>
    <P-Message v-if="errors.new" severity="error" size="small" variant="simple">
      {{ errors.new }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="repeat"
        v-model="repeatNewPassword"
        fluid
        type="password"
        :invalid="!!errors.repeat"
      />
      <label for="repeat">Repetir nova palavra-passe</label>
    </P-FloatLabel>
    <P-Message v-if="errors.repeat" severity="error" size="small" variant="simple">
      {{ errors.repeat }}
    </P-Message>
    <template #footer>
      <P-Button class="flex flex-row" @click="close">
        <Icon icon="prev" />
        Voltar
      </P-Button>
      <P-Button class="flex flex-row" @click="changePassword">
        Confirmar
        <Icon icon="next" />
      </P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";

import { useAuthStore } from "@stores/auth";
import { required, validateField } from "@/rules";

const enabled = defineModel<boolean>();

const TITLE = "Alteração de palavra-passe";

const authStore = useAuthStore();
const toast = useToast();

const currentPassword = ref("");
const newPassword = ref("");
const repeatNewPassword = ref("");

const currentRules = [required];

const newPasswordRules = [
  required,
  (value: string) =>
    value != currentPassword.value || "Palavra-passe atual e a nova têm de ser diferentes",
];

const repeatPasswordRules = [
  required,
  (value: string) => value == newPassword.value || "Palavras-passe não coincidem!",
];

const errors = computed(() => ({
  current: validateField(currentPassword.value, currentRules),
  new: validateField(newPassword.value, newPasswordRules),
  repeat: validateField(repeatNewPassword.value, repeatPasswordRules),
}));

const canChangePassword = computed(() => Object.values(errors.value).every((e) => e === null));

async function changePassword() {
  if (!canChangePassword.value) {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: Object.values(errors.value).find((e) => e !== null) ?? "Verifique os campos.",
      life: 10000,
    });
    return;
  }

  const response = await authStore.changePassword(currentPassword.value, newPassword.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "A palavra-passe foi alterada com sucesso",
      life: 10000,
    });

    close();
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: (response.content as string) || "Não foi possível alterar a palavra-passe",
      life: 10000,
    });
    console.error(response.content);
  }
}

function close() {
  currentPassword.value = "";
  newPassword.value = "";
  repeatNewPassword.value = "";
  enabled.value = false;
}
</script>
