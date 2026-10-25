<template>
  <P-Dialog v-model:visible="enabled" modal class="w-3/10">
    <template #header>Recuperar palavra-passe</template>
    <P-FloatLabel variant="on" class="mt-[10px]">
      <P-InputText
        id="email"
        v-model="email"
        fluid
        type="text"
        :invalid="touched && !!emailError"
        @blur="touched = true"
      />
      <label for="email">Email</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched && emailError"
      severity="error"
      size="small"
      variant="simple"
      class="mt-1"
    >
      {{ emailError }}
    </P-Message>
    <template #footer>
      <P-Button @click="recover()">Recuperar </P-Button>
      <P-Button text @click="close()">Voltar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";

import { useAuthStore } from "@stores/auth";
import { required, validateField } from "@/rules";

const TITLE = "Recuperação de palavra-passe";

const enabled = defineModel<boolean>();

const email = ref("");
const touched = ref(false);

const authStore = useAuthStore();
const toast = useToast();

const emailRules = [required, (value: string) => /\S+@\S+\.\S+/.test(value) || "Email inválido"];
const emailError = computed(() => validateField(email.value.trim(), emailRules));

async function recover() {
  touched.value = true;
  if (emailError.value) {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: emailError.value,
      life: 10000,
    });
    return;
  }

  const response = await authStore.sendRecoveryEmail(email.value.trim());

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: `Foi enviado um email para ${email.value.trim()} com um link para alterar a palavra-passe`,
      life: 10000,
    });
    enabled.value = false;
    email.value = "";
    touched.value = false;
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail:
        (response.content as string) || "Ocorreu um erro ao pedir para alterar a palavra-passe",
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  enabled.value = false;
}
</script>
