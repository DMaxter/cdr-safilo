<template>
  <div class="flex flex-col justify-around items-center h-full">
    <img :src="CDRLogo" class="object-contain m-w-[350px]" />
    <P-Card class="max-w-[500px] w-1/2 max-h-[300px]">
      <template #title>Redefinir Palavra Passe</template>
      <template #content>
        <P-Form @submit="changePassword" class="flex flex-col">
          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-Password fluid inputId="password" v-model="password" :feedback="false" />
            <label for="password">Nova Palavra Passe</label>
          </P-FloatLabel>

          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-Password fluid inputId="repeatPassword" v-model="repeatPassword" :feedback="false" />
            <label for="repeatPassword">Confirmar Palavra Passe</label>
          </P-FloatLabel>

          <P-Button :disabled="!passwordsMatch" fluid class="mt-[30px] mb-[10px]" type="submit">
            Confirmar
          </P-Button>
        </P-Form>
      </template>
    </P-Card>
    <P-Toast />
  </div>
</template>

<!-- TODO: Validations -->

<script lang="ts" setup>
import { ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useToast } from "primevue/usetoast";

import { useAuthStore } from "@stores/auth";
import CDRLogo from "@/assets/logo.png";

const router = useRouter();
const route = useRoute();
const toast = useToast();
const authStore = useAuthStore();

const TITLE = "Recuperação de Palavra Passe";

const password = ref("");
const repeatPassword = ref("");

const passwordsMatch = computed(() => password.value === repeatPassword.value);

if (!route.query.email) {
  toast.add({
    severity: "error",
    summary: TITLE,
    detail: "Email não definido",
    life: 10000,
  });
  router.push({ name: "login" });
}

if (!route.query.token) {
  toast.add({
    severity: "error",
    summary: TITLE,
    detail: "Token não definido",
    life: 10000,
  });
  router.push({ name: "login" });
}

async function changePassword() {
  if (passwordsMatch.value) {
    try {
      await authStore.changePasswordWithToken(route.query.email, password.value, route.query.token);

      toast.add({
        severity: "success",
        summary: TITLE,
        detail: "Palavra-passe alterada com sucesso!",
        life: 10000,
      });

      setTimeout(() => router.push({ name: "login" }), 3000);
    } catch (error: any) {
      toast.add({
        severity: "error",
        summary: TITLE,
        detail: "Ocorreu um erro ao alterar a palavra-passe",
        life: 10000,
      });
      console.error(error);
    }
  } else {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: "As palavras-passe não coincidem.",
      life: 10000,
    });
  }
}
</script>
