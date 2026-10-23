<template>
  <P-Dialog v-model:visible="dialog1" modal header="Registar Comercial" class="w-[500px]">
    <div v-if="added">Comercial registado com sucesso!</div>
    <div v-if="failed">Ocorreu um erro a registar o comercial</div>
    <div v-if="!added && !failed" class="flex flex-col items-center space-y-4">
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
      <P-Button v-if="!added && !failed" label="Adicionar" text @click="addComercial()" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, ref } from "vue";

import { API } from "@router/backend";
import { Register } from "@router/backend/services/auth/types";
import { required, validateField } from "@/rules";

const TITLE = "Registo de Comercial";

const dialog1 = defineModel<boolean>();

const comercialEmail = ref("");
const comercialName = ref("");
const password1 = ref("");
const password2 = ref("");
const added = ref(false);
const failed = ref(false);

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

  try {
    const { status, data } = await API.auth.register({
      email: comercialEmail.value.trim(),
      name: comercialName.value.trim(),
      password: password1.value,
    } as Register);
    if (status === 200) {
      toast.add({
        severity: "success",
        summary: TITLE,
        detail: "Comercial registado com sucesso",
        life: 10000,
      });
      added.value = true;
    } else {
      toast.add({
        severity: "error",
        summary: TITLE,
        detail: typeof data === "string" ? data : "Ocorreu um erro ao registar o comercial",
        life: 10000,
      });
      failed.value = true;
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao registar o comercial",
      life: 10000,
    });
    failed.value = true;
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
  added.value = false;
  failed.value = false;
  reset();
}
</script>
