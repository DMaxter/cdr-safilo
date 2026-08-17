<template>
  <P-Dialog v-model:visible="dialog1" modal header="Registar Comercial" class="w-[500px]">
    <div v-if="added">Comercial registado com sucesso!</div>
    <div v-if="failed">Ocorreu um erro a registar o comercial</div>
    <div v-if="!added && !failed" class="flex flex-col items-center space-y-4">
      <P-InputText v-model="comercialEmail" placeholder="Email" class="w-4/5" />
      <P-InputText v-model="comercialName" placeholder="Nome" class="w-4/5" />
      <P-InputText v-model="password1" placeholder="Palavra Passe" type="password" class="w-4/5" />
      <P-InputText
        v-model="password2"
        placeholder="Confirmar Palavra Passe"
        type="password"
        class="w-4/5"
      />
    </div>
    <template #footer>
      <P-Button label="Voltar" text @click="close()" />
      <P-Button v-if="!added && !failed" label="Adicionar" text @click="addComercial()" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";

import { API } from "@router/backend";
import { Register } from "@router/backend/services/auth/types";

const dialog1 = defineModel<boolean>();

const comercialEmail = ref("");
const comercialName = ref("");
const password1 = ref("");
const password2 = ref("");
const added = ref(false);
const failed = ref(false);

async function addComercial() {
  try {
    const { status } = await API.auth.register({
      email: comercialEmail.value,
      name: comercialName.value,
      password: password1.value,
    } as Register);
    if (status === 200) {
      added.value = true;
    } else {
      failed.value = true;
    }
  } catch {
    failed.value = true;
  }
}

function close() {
  dialog1.value = false;
  added.value = false;
  failed.value = false;
}
</script>
