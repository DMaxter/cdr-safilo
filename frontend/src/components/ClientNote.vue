<template>
  <P-Dialog modal class="max-w-95/100 w-[500px]" v-model:visible="enabled">
    <template #header>Nota do cliente</template>
    <P-Textarea autoResize fluid v-model="props.client.note" />
    <template #footer>
      <P-Button text @click="close()">Voltar</P-Button>
      <P-Button text @click="edit()">Editar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";

import { Client } from "@router/backend/services/client/types";
import { useClientStore } from "@stores/clients";

const enabled = defineModel<boolean>();

const props = defineProps<{
  client: Client;
}>();

const TITLE = "Edição de nota de cliente";

const clientStore = useClientStore();
const toast = useToast();

async function edit() {
  const response = await clientStore.editNote(props.client.id, props.client.note);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Nota editada com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro a editar a nota",
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  enabled.value = false;
}
</script>
