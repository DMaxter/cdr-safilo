<template>
  <P-Dialog modal v-model:visible="enabled" class="w-3/10">
    <template #header>Recuperar palavra-passe</template>
    <P-FloatLabel variant="on" class="mt-[10px]">
      <P-InputText fluid id="email" type="text" v-model="email" />
      <label for="email">Email</label>
    </P-FloatLabel>
    <template #footer>
      <P-Button @click="recover()">Recuperar </P-Button>
      <P-Button text @click="close()">Voltar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { ref } from "vue";

import { useAuthStore } from "@stores/auth";

const TITLE = "Recuperação de palavra-passe";

const enabled = defineModel<boolean>();

const email = ref("");
const success = ref(false);
const failure = ref(false);

const authStore = useAuthStore();
const toast = useToast();

async function recover() {
  const response = await authStore.sendRecoveryEmail(email.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: `Foi enviado um email para ${email.value} com um link para alterar a palavra-passe`,
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao pedir para alterar a palavra-passe",
      life: 10000,
    });
    console.log(response);
  }
}

function close() {
  enabled.value = false;
}
</script>
