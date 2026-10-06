<template>
  <Container>
    <h2 class="font-extrabold text-3xl">Informação do cliente</h2>
    <div class="text-left">
      <p><b>Código: </b>{{ client.id }}</p>
      <p><b>Nome: </b>{{ client.name }}</p>
      <p><b>Banner: </b>{{ client.banner ? client.banner : "Não pertence a nenhum banner" }}</p>
      <h3 class="font-bold text-xl">Morada</h3>
      <p><b>Código Postal: </b>{{ client.postalCode }}</p>
      <p><b>Morada: </b>{{ client.address }}</p>
      <p><b>Cidade: </b>{{ client.city }}</p>
      <p><b>País: </b>{{ client.country }}</p>
      <h3 class="font-bold text-xl">Contacto</h3>
      <p><b>Email: </b>{{ client.email }}</p>
      <p><b>Número de telefone: </b>{{ client.phone }}</p>
      <h3 class="font-bold text-xl" v-if="canViewNote && client.note">Nota de cliente</h3>
      <p v-if="canViewNote && client.note">{{ client.note }}</p>
    </div>
    <template #actions>
      <P-Button v-if="canManageImages" @click="openImageManagement()">Imagens</P-Button>
      <P-Button @click="router.push({ name: 'search', query: { client: client.id } })"
        >Histórico</P-Button
      >
      <ImageManagement
        multiple
        v-model="managing"
        :uploadAction="addImages"
        :deleteAction="deleteImages"
        :obsoleteAction="obsoleteImages"
        :images="client.images"
      />
    </template>
  </Container>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { Client } from "@router/backend/services/client/types";
import type { Image } from "@router/backend/services/image/types";
import { useAuthStore } from "@stores/auth";
import { useClientStore } from "@stores/clients";

const TITLE = "Informação de Cliente";
const IMAGE_TITLE = "Alteração de Imagens de Cliente";

const managing = ref(false);

const route = useRoute();
const router = useRouter();

const authStore = useAuthStore();
const clientStore = useClientStore();

const canViewNote = authStore.isCdr() || authStore.isAdmin();
const canManageImages = authStore.isSafilo() || authStore.isAdmin();

const toast = useToast();

const client = ref<Client>(new Client());

if (route.query.id) {
  try {
    let id = Number(route.query.id);

    if (isNaN(id)) {
      throw Error();
    }

    const response = await clientStore.getClient(id);
    if (response.success) {
      client.value = response.content as Client;
    } else {
      toast.add({
        severity: "error",
        summary: TITLE,
        detail: response.status === 404 ? "Cliente não existente" : response.content,
        life: 10000,
      });
      router.push("clients");
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Cliente inválido",
      life: 10000,
    });
    console.error(error);
    router.push("clients");
  }
} else {
  router.push("clients");
}

async function addImages(files: File[]) {
  try {
    const response = await clientStore.uploadImages(client.value.id as number, files);
    if (!response.success) {
      throw Error(response.content as string);
    }

    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${files.length > 1 ? "Imagens carregadas" : "Imagem carregada"} com sucesso`,
      life: 10000,
    });
  } catch (error) {
    console.log(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao carregar ${files.length > 1 ? "as imagens" : "a imagem"}`,
      life: 10000,
    });
  }
}

async function deleteImages(images: Image[]) {
  const imageIds = images.map((i) => i.id).filter((id) => typeof id === "number" && id > 0);

  if (imageIds.length === 0) {
    return;
  }

  try {
    const response = await clientStore.deleteImages(client.value.id as number, imageIds);
    if (!response.success) {
      throw Error(response.content as string);
    }
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${images.length > 1 ? "Imagens eliminadas" : "Imagem eliminada"} com sucesso`,
      life: 10000,
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao eliminar ${images.length > 1 ? "as imagens" : "a imagem"}`,
      life: 10000,
    });
  }
}

async function obsoleteImages(images: Image[]) {
  const imageIds = images.map((i) => i.id).filter((id) => typeof id === "number" && id > 0);

  if (imageIds.length === 0) {
    return;
  }

  const plural = imageIds.length > 1;
  const errors: string[] = [];

  for (const imageId of imageIds) {
    const response = await clientStore.makeImageObsolete(client.value.id as number, imageId);
    if (!response.success) {
      errors.push(response.content ?? "Erro desconhecido");
    }
  }

  if (errors.length === 0) {
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${plural ? "Imagens marcadas como obsoletas" : "Imagem marcada como obsoleta"} com sucesso`,
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao marcar ${plural ? "as imagens" : "a imagem"} como ${plural ? "obsoletas" : "obsoleta"}`,
      life: 10000,
    });
    console.error(errors);
  }
}

function openImageManagement() {
  managing.value = true;
}
</script>

<style lang="scss" scoped>
p {
  margin-left: 10px;
}
</style>
