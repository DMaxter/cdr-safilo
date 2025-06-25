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
        items-per-page-text="Clientes por página:"
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
          <v-icon @click="openClientInfo(item)" v-tooltip="'Ver cliente'">visibility</v-icon>
          <v-icon v-if="canEdit" @click="editClient(item)" v-tooltip="'Editar cliente'"
            >edit</v-icon
          >
          <v-icon v-if="canAnnotate" @click="showClientNote(item)" v-tooltip="'Nota do cliente'">sticky_note_2</v-icon>
        </template>
      </v-data-table-virtual>
      <v-row v-if="canEdit" class="flex-row" justify="space-around" style="width: 80%">
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
      <EditClient v-model="editing" :client="selectedClient" @updated="refreshClients" />
      <ClientNote v-model="annotating" :client="selectedClient" @updated="refreshClients" />
    </v-row>
  </Container>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";

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

const canAnnotate = user.isCdr() || user.isAdmin();

const canEdit = user.isSafilo() || user.isAdmin();
const editing: ClientDto = ref(false);

const annotating = ref(false);

const selectedClient = ref(new ClientDto());

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

function showClientNote(client: ClientDto) {
  selectedClient.value = client;
  annotating.value = true;
}

function openClientInfo(client) {
  router.push({ name: "clientInfo" });
}

async function editClient(client: ClientDto) {
  selectedClient.value = client;
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
