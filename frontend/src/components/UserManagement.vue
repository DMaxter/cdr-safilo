<template>
  <P-Dialog
    v-model:visible="dialog1"
    modal
    header="Registar Comercial"
    class="w-[500px]"
    @hide="reset"
  >
    <div class="flex flex-col items-center space-y-4">
      <div class="w-4/5">
        <P-InputText
          v-model="comercialEmail"
          placeholder="Email"
          fluid
          :invalid="touched.email && !!errors.email"
          @blur="touch('email')"
        />
        <P-Message
          v-if="touched.email && errors.email"
          class="mt-2"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ errors.email }}
        </P-Message>
      </div>
      <div class="w-4/5">
        <P-InputText
          v-model="comercialName"
          placeholder="Nome"
          fluid
          :invalid="touched.name && !!errors.name"
          @blur="touch('name')"
        />
        <P-Message
          v-if="touched.name && errors.name"
          class="mt-2"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ errors.name }}
        </P-Message>
      </div>
      <div class="w-4/5">
        <P-InputText
          v-model="password1"
          placeholder="Palavra Passe"
          type="password"
          fluid
          :invalid="touched.password1 && !!errors.password1"
          @blur="touch('password1')"
        />
        <P-Message
          v-if="touched.password1 && errors.password1"
          class="mt-2"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ errors.password1 }}
        </P-Message>
      </div>
      <div class="w-4/5">
        <P-InputText
          v-model="password2"
          placeholder="Confirmar Palavra Passe"
          type="password"
          fluid
          :invalid="touched.password2 && !!errors.password2"
          @blur="touch('password2')"
        />
        <P-Message
          v-if="touched.password2 && errors.password2"
          class="mt-2"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ errors.password2 }}
        </P-Message>
      </div>
    </div>
    <template #footer>
      <P-Button label="Voltar" text @click="close()" />
      <P-Button label="Adicionar" text @click="addComercial()" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";

import { Register } from "@router/backend/services/auth/types";
import { useUserStore } from "@stores/users";
import { required, validateField } from "@/rules";

const TITLE = "Registo de Comercial";

const dialog1 = defineModel<boolean>();

const comercialEmail = ref("");
const comercialName = ref("");
const password1 = ref("");
const password2 = ref("");

const userStore = useUserStore();
const toast = useToast();

const touched = ref<Record<string, boolean>>({});

const emailRules = [required, (value: string) => /\S+@\S+\.\S+/.test(value) || "Email inválido"];

const errors = computed(() => ({
  email: validateField(comercialEmail.value.trim(), emailRules),
  name: validateField(comercialName.value.trim(), [required]),
  password1: validateField(password1.value, [required]),
  password2: validateField(password2.value, [
    required,
    (value: string) => value === password1.value || "As palavras-passe não coincidem",
  ]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);

function touch(field: string) {
  touched.value[field] = true;
}

async function addComercial() {
  for (const field of Object.keys(errors.value)) {
    touched.value[field] = true;
  }

  if (errorList.value.length > 0) {
    toast.add({
      severity: "warn",
      summary: TITLE,
      detail: errorList.value[0],
      life: 10000,
    });
    return;
  }

  const response = await userStore.register({
    email: comercialEmail.value.trim(),
    name: comercialName.value.trim(),
    password: password1.value,
  } as Register);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Comercial registado com sucesso",
      life: 10000,
    });
    close();
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: response.content || "Ocorreu um erro ao registar o comercial",
      life: 10000,
    });
  }
}

function reset() {
  comercialEmail.value = "";
  comercialName.value = "";
  password1.value = "";
  password2.value = "";
  touched.value = {};
}

function close() {
  dialog1.value = false;
}
</script>
