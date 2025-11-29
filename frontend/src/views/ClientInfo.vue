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
      <P-Button @click="router.push({ name: 'search', query: { client: client.id } })">Histórico</P-Button>
      <ImageManagement
        v-model="managing"
        :uploadAction="addImages"
        :deleteAction="deleteImages"
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
    let id = new Number(route.query.id);

    if (isNaN(id)) {
      throw Error();
    }

    let response = await clientStore.getClient(id.valueOf())!;
    if (response.success) {
      client.value = response.content;
    } else {
      toast.add({
        severity: "error",
        summary: TITLE,
        detail: ((response.status === 404) ? "Cliente não existente" : response.content),
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
    await clientStore.uploadImages(client.value.id, files);
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${(files.length > 1) ? 'Imagens carregadas' : 'Imagem carregada'} com sucesso`,
      life: 10000
    });
  } catch (error) {
    console.log(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao carregar ${(files.length > 1) ? 'as imagens' : 'a imagem'}`,
      life: 10000
    });
  }
}

async function deleteImages(images: Image[]) {
  try {
    await clientStore.deleteImages(client.value.id, images.map((i) => i.id));
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${(images.length > 1) ? 'Imagens eliminadas' : 'Imagem eliminada'} com sucesso`,
      life: 10000
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao eliminar ${(images.length > 1) ? 'as imagens' : 'a imagem'}`,
      life: 10000
    });
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
