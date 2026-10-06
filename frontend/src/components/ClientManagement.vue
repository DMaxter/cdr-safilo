<template>
  <!--TODO: Dialog close button not working-->
  <P-Dialog modal class="max-w-95/100 w-[400px]" v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Cliente" : "Adicionar Cliente" }}</template>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="id" :disabled="editing" fluid v-model="draft.id" />
      <label for="id">Código do cliente</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="name" required fluid v-model="draft.name" />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="email" required fluid v-model="draft.email" />
      <label for="email">Email</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="fiscal" required fluid v-model="draft.fiscalNumber" />
      <label for="fiscal">NIF</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="phone" required fluid v-model="draft.phone" />
      <label for="phone">Número de telefone</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="address" required fluid v-model="draft.address" />
      <label for="address">Endereço</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="postal" required fluid v-model="draft.postalCode" />
      <label for="postal">Código postal</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="banner" fluid v-model="draft.banner" />
      <label for="banner">Banner</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="city" required fluid v-model="draft.city" />
      <label for="city">Cidade</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="country" required fluid v-model="draft.country" />
      <label for="country">País</label>
    </P-FloatLabel>
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
      <P-Button text @click="action">{{ editing ? "Atualizar" : "Adicionar" }}</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref, watch } from "vue";

import { Client } from "@router/backend/services/client/types";
import { useClientStore } from "@stores/clients";
import { ManageMode } from "@/utils";

const mode = defineModel<ManageMode>();
const enabled = computed(() => mode.value !== ManageMode.None);
const editing = computed(() => enabled && mode.value === ManageMode.Edit);

const props = defineProps<{
  client: Client;
}>();

const TITLE = computed(() => (editing.value ? "Edição de cliente" : "Criação de cliente"));

// Work on a copy so edits don't leak into the store until save.
const draft = ref<Client>(new Client());
watch(
  () => [props.client, mode.value] as const,
  () => {
    draft.value = new Client(props.client);
  },
  { immediate: true },
);

const clientStore = useClientStore();
const toast = useToast();

async function action() {
  if (editing.value) {
    await updateClient();
  } else {
    await createClient();
  }
}

async function createClient() {
  const response = await clientStore.addClient(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Cliente adicionado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a adicionar o cliente",
      life: 10000,
    });
    console.error(response);
  }
}

async function updateClient() {
  const response = await clientStore.editClient(draft.value);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE.value,
      detail: "Cliente editado com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE.value,
      detail: "Ocorreu um erro a editar o cliente",
      life: 10000,
    });
    console.error(response);
  }
}

function close() {
  mode.value = ManageMode.None;
}
</script>

<style lang="scss" scoped>
.field {
  margin-top: 10px;
}
</style>
