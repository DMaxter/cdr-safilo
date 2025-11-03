<template>
  <P-Menubar :model="items">
    <template #item="{ item, props }">
      <Router-Link v-slot="{ href, navigate }" :to="item.route" custom>
        <a :href="href" v-bind="props.action" @click="navigate" v-if="item.display !== undefined ? item.display! : true">
          <Icon :icon="item.icon" />
          <span>{{ item.label }}</span>
        </a>
      </Router-Link>
    </template>
    <template #end>
      <div class="h-full p-menubar-item">
        <div id="logout" class="h-full p-menubar-item-content" @click="logout">
          <Icon icon="logout" />
          <span>Logout</span>
        </div>
      </div>
    </template>
  </P-Menubar>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";

const router = useRouter();
const authStore = useAuthStore();

const items = ref([
  {
    label: "Perfil",
    icon: "account_circle",
    route: "profile",
  },
  {
    label: "Clientes",
    icon: "people",
    route: "clients",
  },
  {
    label: "Procurar",
    icon: "search",
    route: "search",
  },
  {
    label: "Configurar",
    icon: "settings",
    route: "configure",
    display: computed(() => authStore.isSafilo() || authStore.isAdmin())
  },
  {
    label: "Novo Pedido",
    icon: "playlist_add",
    route: "order",
    display: computed(() => authStore.isCommercial() || authStore.isAdmin())
  }
]);

async function logout() {
  router.push("/");
  await authStore.logout();
}
</script>

<style lang="scss" scoped>
#logout {
  padding: var(--p-menubar-base-item-padding);
  cursor: pointer;
  display: flex;
  align-items: center;
}
</style>
