<template>
  <P-Menubar :model="items">
    <template #item="{ item, props }">
      <Router-Link v-slot="{ href, navigate }" :to="{ name: item.route }" custom>
        <a
          v-if="item.display !== undefined ? item.display! : true"
          :href="href"
          v-bind="props.action"
          :class="{
            'rounded-md bg-primary !text-white hover:bg-primary-emphasis': isActive(item),
          }"
          :aria-current="isActive(item) ? 'page' : undefined"
          @click="navigate"
        >
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
import { computed, ref, type ComputedRef } from "vue";
import { useRoute, useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";

type MenuItem = {
  label: string;
  icon: string;
  route: string;
  matches: string[];
  display?: ComputedRef<boolean>;
};

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const items = ref<MenuItem[]>([
  {
    label: "Perfil",
    icon: "account_circle",
    route: "profile",
    matches: ["profile"],
  },
  {
    label: "Clientes",
    icon: "people",
    route: "clients",
    matches: ["clients", "client"],
  },
  {
    label: "Procurar",
    icon: "search",
    route: "search",
    matches: ["search", "request"],
  },
  {
    label: "Configurar",
    icon: "settings",
    route: "configure",
    matches: ["configure"],
    display: computed(() => authStore.isSafilo() || authStore.isAdmin() || authStore.isCdr()),
  },
  {
    label: "Novo Pedido",
    icon: "playlist_add",
    route: "order",
    matches: ["order"],
    display: computed(() => authStore.isCommercial() || authStore.isAdmin()),
  },
]);

function isActive(item: MenuItem) {
  return item.matches.includes(route.name as string);
}

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
