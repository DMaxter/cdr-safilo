<template>
  <P-Dialog modal class="max-w-95/100 w-[400px]" v-model:visible="enabled">
    <template #header>{{ editing ? "Editar Cliente" : "Adicionar Cliente" }}</template>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="id" :disabled="editing" fluid v-model="draft.id" />
      <label for="id">Código do cliente</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="name"
        required
        fluid
        v-model="draft.name"
        @blur="touch('name')"
        :invalid="touched.name && !!errors.name"
      />
      <label for="name">Nome</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.name && errors.name"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.name }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="email"
        required
        fluid
        v-model="draft.email"
        @blur="touch('email')"
        :invalid="touched.email && !!errors.email"
      />
      <label for="email">Email</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.email && errors.email"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.email }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="fiscal"
        required
        fluid
        v-model="draft.fiscalNumber"
        @blur="touch('fiscalNumber')"
        :invalid="touched.fiscalNumber && !!errors.fiscalNumber"
      />
      <label for="fiscal">NIF</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.fiscalNumber && errors.fiscalNumber"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.fiscalNumber }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="phone"
        required
        fluid
        v-model="draft.phone"
        @blur="touch('phone')"
        :invalid="touched.phone && !!errors.phone"
      />
      <label for="phone">Número de telefone</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.phone && errors.phone"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.phone }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="address"
        required
        fluid
        v-model="draft.address"
        @blur="touch('address')"
        :invalid="touched.address && !!errors.address"
      />
      <label for="address">Endereço</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.address && errors.address"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.address }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="postal"
        required
        fluid
        v-model="draft.postalCode"
        @blur="touch('postalCode')"
        :invalid="touched.postalCode && !!errors.postalCode"
      />
      <label for="postal">Código postal</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.postalCode && errors.postalCode"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.postalCode }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText id="banner" fluid v-model="draft.banner" />
      <label for="banner">Banner</label>
    </P-FloatLabel>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="city"
        required
        fluid
        v-model="draft.city"
        @blur="touch('city')"
        :invalid="touched.city && !!errors.city"
      />
      <label for="city">Cidade</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.city && errors.city"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.city }}
    </P-Message>
    <P-FloatLabel class="field" variant="on">
      <P-InputText
        id="country"
        required
        fluid
        v-model="draft.country"
        @blur="touch('country')"
        :invalid="touched.country && !!errors.country"
      />
      <label for="country">País</label>
    </P-FloatLabel>
    <P-Message
      v-if="touched.country && errors.country"
      class="mt-2"
      severity="error"
      size="small"
      variant="simple"
    >
      {{ errors.country }}
    </P-Message>
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
import { required, validateField } from "@/rules";
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

const touched = ref<Record<string, boolean>>({});

const emailRules = [required, (value: string) => /\S+@\S+\.\S+/.test(value) || "Email inválido"];

const errors = computed(() => ({
  name: validateField(draft.value.name.trim(), [required]),
  email: validateField(draft.value.email.trim(), emailRules),
  fiscalNumber: validateField(draft.value.fiscalNumber.trim(), [required]),
  phone: validateField(draft.value.phone.trim(), [required]),
  address: validateField(draft.value.address.trim(), [required]),
  postalCode: validateField(draft.value.postalCode.trim(), [required]),
  city: validateField(draft.value.city.trim(), [required]),
  country: validateField(draft.value.country.trim(), [required]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);
const isValid = computed(() => errorList.value.length === 0);

function touch(field: string) {
  touched.value[field] = true;
}

async function action() {
  for (const field of Object.keys(errors.value)) {
    touched.value[field] = true;
  }

  if (!isValid.value) {
    toast.add({
      severity: "warn",
      summary: TITLE.value,
      detail: errorList.value[0],
      life: 10000,
    });
    return;
  }

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
