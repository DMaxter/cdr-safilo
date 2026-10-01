<template>
  <Container>
    <div class="grid grid-cols-2 gap-x-10 gap-y-5 mt-[40px]">
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="openPlafondManagement"
        >Plafond</P-Button
      >
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="openBrandList">Marcas</P-Button>
      <P-Button v-if="manageSafilo" class="h-[60px]" @click="openUserManagement">Utilizadores</P-Button>
      <P-Button v-if="manageCdr" class="h-[60px]" @click="openMaterialList">Materiais</P-Button>
      <P-Button v-if="manageCdr" class="h-[60px]" @click="openPriceList">Preços</P-Button>
      <!-- END TODO: -->
    </div>
    <BrandList v-model="brandList" />
    <MaterialList v-model="materialList" />
    <PriceList v-if="manageCdr" v-model="priceList" />
    <PlafondList v-if="manageSafilo" v-model="managePlafond" />
    <UserManagement v-if="manageSafilo" v-model="userManagement" />
  </Container>
</template>

<script setup lang="ts">
import { ref } from "vue";

import { useAuthStore } from "@stores/auth";

const authStore = useAuthStore();

const manageCdr = authStore.isCdr() || authStore.isAdmin();
const manageSafilo = authStore.isSafilo() || authStore.isAdmin();

const managePlafond = ref(false);

const brandList = ref(false);
const materialList = ref(false);
const priceList = ref(false);
const userManagement = ref(false);

function openBrandList() {
  brandList.value = true;
}

function openMaterialList() {
  materialList.value = true;
}

function openPriceList() {
  priceList.value = true;
}

function openPlafondManagement() {
  managePlafond.value = true;
}

function openUserManagement() {
  userManagement.value = true;
}
</script>
