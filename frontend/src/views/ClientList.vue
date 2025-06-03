<template>
  <Container>
    <v-row justify="center" align="center" class="d-flex flex-column mt-2">
      <Message v-model="failure" message="Não foi possível obter a lista de clientes" />
      <v-data-table-virtual
        :headers="headers"
        :items="allClients"
        fixed-header
        hover
        :search="searchValue"
        no-data-text="Não existem clientes registados"
        item-key="id"
        height="500"
        items-per-page="50"
        style="max-width: 98%"
        class="elevation-1 my-header-style"
      >
        <template v-slot:top>
          <v-container fluid>
            <v-row style="margin-left: 0">
              <v-col cols="6">
                <v-row class="pa-2">
                  <v-text-field
                    style="width: 150px"
                    v-model="searchValue"
                    label="Procurar"
                    hide-details
                    variant="outlined"
                    dense
                  ></v-text-field>
                </v-row>
              </v-col>
            </v-row>
          </v-container>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
          <v-icon @click="openClientInfo(item)">visibility</v-icon>
          <v-icon v-if="canEdit" @click="editClient(item)">edit</v-icon>
        </template>
      </v-data-table-virtual>
      <v-row class="flex-row" justify="space-around" style="width: 80%">
        <UploadClients @updated="refreshClients" />
        <v-btn
          height="50"
          width="180"
          class="mt-11 ml-3 customGradient text-white"
          @click="getRequests()"
        >
          <span style="font-size: 11px">Descarregar pedidos</span>
        </v-btn>
        <AddClient @updated="refreshClients" />
      </v-row>
      <EditClient v-model="editing" :client="editingClient" @updated="refreshClients" />
    </v-row>
  </Container>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";

import { store } from "@/store.js";
import { useUserStore } from "@/stores/user";
import Backend from "@/router/backend";
import ClientDto from "@models/ClientDto";

const router = useRouter();
const failure = ref(false);

const user = useUserStore();
await user.init();

const allClients = reactive([]);
const banners = reactive([]);

const searchValue = ref("");

const canEdit = user.isSafilo() || user.isAdmin();
const editingClient = ref(new ClientDto());
const editing: ClientDto = ref(false);

await getClients();

const headers = [
  {
    title: "Código",
    align: "center",
    value: "id",
    class: "my-header-style",
    sortable: true,
  },
  {
    title: "Nome",
    value: "name",
    align: "center",
    class: "my-header-style",
    sortable: true,
  },
  { value: "actions", align: "right", sortable: false, class: "my-header-style" },
];

async function getClients() {
  try {
    allClients.unshift(...(await Backend.getClients()));
    banners.unshift(...new Set(allClients.map((c) => c.banner)));
  } catch (error) {
    console.error(error);
    throw Error(error);
  }
}

async function refreshClients() {
  allClients.length = 0;
  banners.length = 0;

  await getClients();
}

function openClientInfo(client) {
  router.push({ name: "clientInfo" });
}

async function editClient(client: ClientDto) {
  editingClient.value = client;
  editing.value = true;
}

async function getRequests() {
  try {
    await Backend.getAllRequests();
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}
</script>

<style lang="scss">
.my-header-style {
  border-radius: 0px;
  background-color: rgb(241, 241, 241) !important;
}
</style>
