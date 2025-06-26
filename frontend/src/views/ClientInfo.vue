<template>
  <Container>
    <Message v-model="failure" message="Não foi possível obter a informação do cliente" />
    <v-row align="start" justify="start" class="flex-column ml-12 mt-3 mb-1">
      <h2>Informação do cliente</h2>
      <p><b>Código: </b>{{ client.id }}</p>
      <p><b>Nome: </b>{{ client.name }}</p>
      <p><b>Banner: </b>{{ client.banner ? client.banner : "Não pertence a nenhum banner" }}</p>
      <h3>Morada</h3>
      <p><b>Código Postal: </b>{{ client.postalCode }}</p>
      <p><b>Morada: </b>{{ client.address }}</p>
      <p><b>Cidade: </b>{{ client.city }}</p>
      <p><b>País: </b>{{ client.country }}</p>
      <h3>Contacto</h3>
      <p><b>Email: </b>{{ client.email }}</p>
      <p><b>Número de telefone: </b>{{ client.phone }}</p>
      <h3 v-if="canViewNote && client.note">Nota de cliente</h3>
      <p v-if="canViewNote && client.note">{{ client.note }}</p>
    </v-row>
    <template v-slot:actions>
      <v-row justify="space-around">
        <v-btn v-if="canManageImages" @click="openImageManagement()" v-tooltip="'Imagens'">Imagens</v-btn>
        <v-btn @click="router.push({ name: 'search', query: { client: client.id } })"
          >Histórico</v-btn
        >
        <ImageManagement
          v-model="managing"
          :deletable="true"
          :uploadable="true"
          :images="client.images"
          @add="addImages"
          @delete="deleteImages"
        />
      </v-row>
      <Message v-model="show" :message="message" />
    </template>
  </Container>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useUserStore } from "@/stores/user";
import Backend from "@/router/backend";
import ClientDto from "@models/ClientDto";

const managing = ref(false);
const failure = ref(false);

const route = useRoute();
const router = useRouter();

const user = useUserStore();
await user.init();

const canViewNote = user.isCdr() || user.isAdmin();
const canManageImages = user.isSafilo() || user.isAdmin();

const show = ref(false);
const message = ref("");

const client = reactive(new ClientDto());

if (route.query.id) {
  try {
    new Number(route.query.id);
    await refreshClient();
  } catch (error) {
    console.error(error);
    failure.value = true;
    router.push("clients");
  }
} else {
  router.push("clients");
}

async function refreshClient() {
  Object.assign(client, await Backend.getClient(route.query.id));
}

async function addImages(files) {
  try {
    await Backend.addClientImage(route.query.id, files);
    showUploadMessage(true);
    await refreshClient();
  } catch (error) {
    console.log(error);
    showUploadMessage(false);
  }
}

async function deleteImages(images) {
  console.log(images.value);
  try {
    await Backend.deleteClientImages(images);
    showDeleteMessage(true);
    await refreshClient();
  } catch (error) {
    console.error(error);
    showDeleteMessage(false);
  }
}

function openImageManagement() {
  managing.value = true;
}

function showUploadMessage(success) {
  show.value = true;

  if (success) {
    message.value = "Imagens carregadas com sucesso!";
  } else {
    message.value = "Ocorreu um erro ao carregar as imagens";
  }
}

function showDeleteMessage(success) {
  show.value = true;

  if (success) {
    message.value = "Imagens eliminadas com sucesso!";
  } else {
    message.value = "Ocorreu um erro ao eliminar as imagens";
  }
}
</script>

<style lang="scss" scoped>
p {
  margin-left: 10px;
}
</style>
