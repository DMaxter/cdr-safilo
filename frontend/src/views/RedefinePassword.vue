<template>
  <div class="flex flex-col justify-around items-center h-full">
    <img :src="CDRLogo" class="object-contain m-w-[350px]" />
    <P-Card class="max-w-[500px] w-1/2 max-h-[300px]">
      <template #title>Redefinir Palavra Passe</template>
      <template #content>
        <P-Form class="flex flex-col" @submit="changePassword">
          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-Password
              v-model="password"
              fluid
              input-id="password"
              :feedback="false"
              :invalid="touched.password && !!errors.password"
              @blur="touched.password = true"
            />
            <label for="password">Nova Palavra Passe</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.password && errors.password"
            class="mt-[5px]"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.password }}
          </P-Message>

          <P-FloatLabel variant="on" class="mt-[10px]">
            <P-Password
              v-model="repeatPassword"
              fluid
              input-id="repeatPassword"
              :feedback="false"
              :invalid="touched.repeat && !!errors.repeat"
              @blur="touched.repeat = true"
            />
            <label for="repeatPassword">Confirmar Palavra Passe</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.repeat && errors.repeat"
            class="mt-[5px]"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.repeat }}
          </P-Message>

          <P-Button :disabled="!passwordsMatch" fluid class="mt-[30px] mb-[10px]" type="submit">
            Confirmar
          </P-Button>
        </P-Form>
      </template>
    </P-Card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useToast } from "primevue/usetoast";

import { required, validateField } from "@/rules";
import { useAuthStore } from "@stores/auth";
import CDRLogo from "@/assets/logo.png";

const router = useRouter();
const route = useRoute();
const toast = useToast();
const authStore = useAuthStore();

const TITLE = "Recuperação de Palavra Passe";

const password = ref("");
const repeatPassword = ref("");

const touched = reactive({ password: false, repeat: false });

const passwordsMatch = computed(() => password.value === repeatPassword.value);

const errors = computed(() => ({
  password: validateField(password.value, [required]),
  repeat: validateField(repeatPassword.value, [
    required,
    (value) => value === password.value || "As palavras-passe não coincidem",
  ]),
}));

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
  if (!passwordsMatch.value) {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: "As palavras-passe não coincidem.",
      life: 10000,
    });
    return;
  }

  const response = await authStore.changePasswordWithToken(
    route.query.email as string,
    password.value,
    route.query.token as string,
  );

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Palavra-passe alterada com sucesso!",
      life: 10000,
    });

    setTimeout(() => router.push({ name: "login" }), 3000);
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: (response.content as string) || "Ocorreu um erro ao alterar a palavra-passe",
      life: 10000,
    });
    console.error(response.content);
  }
}
</script>
