<template>
  <v-row no-gutters justify="space-between" class="pt-2 pl-2 pr-2">
    <v-menu :offset-x="true" tile>
      <template v-slot:activator="{ props }">
        <v-btn height="64" width="100" class="white--text customGradient" v-bind="props">
          Menu
        </v-btn>
      </template>

      <v-btn-toggle dense tile dark borderless style="height: 64px">
        <v-btn
          @click="router.push('profile')"
          height="64"
          width="130"
          class="customGradient text-white"
        >
          <span style="font-size: 12px">Perfil</span>

          <v-icon right>account_circle</v-icon>
        </v-btn>

        <v-btn
          @click="router.push('clients')"
          height="64"
          width="130"
          class="customGradient text-white"
        >
          <span style="font-size: 12px">Clientes</span>

          <v-icon right>people</v-icon>
        </v-btn>

        <v-btn
          @click="router.push('search')"
          height="64"
          width="130"
          class="customGradient text-white"
        >
          <span style="font-size: 12px">Procurar</span>

          <v-icon right>search</v-icon>
        </v-btn>

        <v-btn
          @click="router.push('orderClient')"
          height="64"
          width="130"
          class="customGradient text-white"
          v-if="store.isCommercial() || store.isAdmin()"
        >
          <span style="font-size: 12px">Novo Pedido</span>

          <v-icon right>playlist_add</v-icon>
        </v-btn>

        <v-btn
          @click="router.push('materiais')"
          height="64"
          width="130"
          class="customGradient text-white"
          v-if="store.isCdr() || store.isAdmin()"
        >
          <span style="font-size: 12px">Materiais</span>

          <v-icon right>book</v-icon>
        </v-btn>

        <v-btn
          @click="router.push('configure')"
          height="64"
          width="160"
          class="customGradient text-white"
          v-if="store.isSafilo() || store.isAdmin()"
        >
          <span style="font-size: 12px">Configurar</span>

          <v-icon right>settings</v-icon>
        </v-btn>
      </v-btn-toggle>
    </v-menu>
    <v-btn height="64" width="100" class="white--text customGradient" @click="logout()">
      Logout
    </v-btn>
  </v-row>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import { useRouter } from "vue-router";

import Backend from "@/router/backend";
import { useUserStore } from "@stores/user";

const router = useRouter();
const store = useUserStore();
await store.init();

async function logout() {
  await Backend.logout();
  store.clear();
  router.push("/");
}
</script>

<style lang="scss" scoped></style>
