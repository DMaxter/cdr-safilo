<template>
  <P-Dialog v-model:visible="enabled" modal class="w-[800px] h-[75vh]">
    <template #header>Plafonds</template>
    <P-DataTable
      v-model:filters="filters"
      paginator
      scrollable
      removable-sort
      class="max-w-98/100"
      scroll-height="flex"
      filter-display="row"
      :value="rows"
      :rows="10"
      :rows-per-page-options="[5, 10, 20, 50, 100]"
      table-style="table-layout: fixed; width: 100%"
    >
      <template #empty>Não existem plafonds</template>

      <P-Column sortable class="w-35/100" field="name" header="Nome">
        <template #filter="{ filterModel, filterCallback }">
          <P-InputText v-model="filterModel.value" placeholder="Nome" @input="filterCallback()" />
        </template>
      </P-Column>
      <P-Column sortable class="w-35/100" field="email" header="Email">
        <template #filter="{ filterModel, filterCallback }">
          <P-InputText v-model="filterModel.value" placeholder="Email" @input="filterCallback()" />
        </template>
      </P-Column>
      <P-Column sortable class="w-2/10 text-right!" field="totalPlafond" header="Plafond" />
      <P-Column style="width: 10%">
        <template #body="{ data }">
          <Icon v-tooltip="'Alterar Plafond'" icon="euro" @click="openPlafondDetail(data)" />
        </template>
      </P-Column>
    </P-DataTable>
    <template #footer>
      <P-Button label="Voltar" text @click="close" />
    </template>
  </P-Dialog>
  <PlafondDetail
    v-model="managingPlafond"
    :credits="selectedUser.credits"
    :user="selectedUser.email"
  />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref } from "vue";

import { User } from "@router/backend/services/user/types";
import { useUserStore } from "@stores/users";

const TITLE = "Lista de Plafonds";

const enabled = defineModel<boolean>();
const managingPlafond = ref(false);
const selectedUser = ref<User>(new User());

const userStore = useUserStore();
const toast = useToast();

const filters = ref({
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
  email: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

async function refresh() {
  const response = await userStore.getUsers();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de utilizadores",
      life: 10000,
    });
  }
}

function sumPlafonds(user: User) {
  return user.credits
    .map((c) => c.amount)
    .reduce((acc, c) => acc + c, 0)
    .toFixed(2);
}

const rows = computed(() =>
  userStore.users.map((user) => ({ ...user, totalPlafond: sumPlafonds(user) })),
);

function openPlafondDetail(user: User) {
  managingPlafond.value = true;
  selectedUser.value = user;
}

function close() {
  enabled.value = false;
}
</script>
