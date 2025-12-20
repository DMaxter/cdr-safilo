<template>
  <div class="flex flex-col justify-around items-center h-full">
    <img :src="CDRLogo" class="object-contain m-w-[350px]" />
    <P-Card class="max-w-[500px] w-1/2 max-h-[330px] h-1/2">
      <template #title>Iniciar Sessão</template>
      <template #content>
        <P-Form @submit="login" class="flex flex-col">
          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-InputText fluid size="large" id="email" type="text" v-model="auth.email" />
            <label for="email">Email</label>
          </P-FloatLabel>

          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-InputText fluid size="large" id="password" type="password" v-model="auth.password" />
            <label for="password">Palavra-passe</label>
          </P-FloatLabel>

          <P-Button text @click="showRecover()" class="mt-[30px]">
            Esqueci-me da palavra-passe
          </P-Button>

          <P-Button fluid class="my-[10px]" type="submit">Entrar </P-Button>
        </P-Form>
      </template>
    </P-Card>
  </div>
  <RecoveryCode v-model="recover" />
</template>

<!-- TODO: Validations -->

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";
import { Login } from "@router/backend/services/auth/types";
import CDRLogo from "@/assets/logo.png";

const auth = ref(new Login());
const router = useRouter();

const recover = ref(false);

const authStore = useAuthStore();
const toast = useToast();

async function login() {
  const response = await authStore.login(auth.value);

  if (response.success) {
    router.push("profile");
  } else {
    toast.add({
      severity: "error",
      summary: "Erro de autenticação",
      detail: "O nome de utilizador ou a palavra-passe estão incorretos",
      life: 10000,
    });
    console.error(response);
  }
}

function showRecover() {
  recover.value = true;
}
</script>
