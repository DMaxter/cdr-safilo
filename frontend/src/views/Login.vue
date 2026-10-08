<template>
  <div class="flex flex-col justify-around items-center h-full">
    <img :src="CDRLogo" class="object-contain m-w-[350px]" />
    <P-Card class="w-1/2 max-w-[500px]">
      <template #title>Iniciar Sessão</template>
      <template #content>
        <P-Form class="flex flex-col gap-3" @submit="login">
          <P-FloatLabel variant="on">
            <P-InputText
              id="email"
              v-model="auth.email"
              fluid
              size="large"
              type="text"
              :invalid="touched.email && !!errors.email"
              @blur="touched.email = true"
            />
            <label for="email">Email</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.email && errors.email"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.email }}
          </P-Message>

          <P-FloatLabel variant="on">
            <P-InputText
              id="password"
              v-model="auth.password"
              fluid
              size="large"
              type="password"
              :invalid="touched.password && !!errors.password"
              @blur="touched.password = true"
            />
            <label for="password">Palavra-passe</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.password && errors.password"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.password }}
          </P-Message>

          <P-Button text class="self-center mt-1" @click="showRecover()"
            >Esqueci-me da palavra-passe</P-Button
          >
          <P-Button fluid type="submit">Entrar</P-Button>
        </P-Form>
      </template>
    </P-Card>
  </div>
  <RecoveryCode v-model="recover" />
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";
import { Login } from "@router/backend/services/auth/types";
import { required, validateField } from "@/rules";
import CDRLogo from "@/assets/logo.png";

const auth = ref(new Login());
const router = useRouter();

const recover = ref(false);
const touched = ref({ email: false, password: false });

const authStore = useAuthStore();
const toast = useToast();

const emailRules = [required];
const passwordRules = [required];

const errors = computed(() => ({
  email: validateField(auth.value.email, emailRules),
  password: validateField(auth.value.password, passwordRules),
}));

async function login() {
  touched.value.email = true;
  touched.value.password = true;

  const firstError = Object.values(errors.value).find((error) => error !== null);

  if (firstError) {
    toast.add({
      severity: "warn",
      summary: "Iniciar Sessão",
      detail: firstError,
      life: 10000,
    });
    return;
  }

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
