<template>
  <Container>
    <div class="grid grid-cols-2 gap-x-10 gap-y-5 mt-[40px]">
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="openPlafondManagement"
        >Plafond</P-Button
      >
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="openBrandList">Marcas</P-Button>
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="">Utilizadores</P-Button>
      <P-Button v-if="manageCdr" class="h-[60px]" @click="">Materiais</P-Button>
      <P-Button v-if="manageCdr" class="h-[60px]" @click="">Preços</P-Button>
    </div>
    <BrandList v-model="brandList" />
    <PlafondList v-if="manageSafilo" v-model="managePlafond" />
  </Container>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";

import { useAuthStore } from "@stores/auth";

const authStore = useAuthStore();

const manageCdr = authStore.isCdr() || authStore.isAdmin();
const manageSafilo = authStore.isSafilo() || authStore.isAdmin();

const managePlafond = ref(false);

const brandList = ref(false);

function openBrandList() {
  brandList.value = true;
}

function openPlafondManagement() {
  managePlafond.value = true;
}
</script>
