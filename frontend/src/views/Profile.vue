<template>
  <Container>
    <v-row justify="center" align="center" class="d-flex flex-column mb-10 mt-3">
      <v-avatar color="#808080" size="100" class="mb-6 customGradient">
        <v-icon size="80" dark>account_circle</v-icon>
      </v-avatar>
      {{ user.user.name }}
    </v-row>
    <v-row justify="start" align="start" class="d-flex ml-4">
      <v-col cols="1">
        <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
          <v-icon size="50" dark class="d-flex">attach_money</v-icon>
        </v-avatar>
      </v-col>
      <v-col cols="2" class="d-flex flex-column ml-10" align="left">
        Plafond Total
        <v-main class="text-no-wrap" align="center" style="background-color: #cfcfcf">
          {{ credits }}
        </v-main>
      </v-col>
      <v-col cols="1" align="left" class>
        <template v-if="hasPlafond">
          <PlafondDetail v-model="plafondDetail" :credits="user.user.credits" />
          <v-avatar
            color="#808080"
            size="30"
            class="d-flex customGradient2"
            @click="showPlafondDetail()"
          >
            <v-icon size="20" dark class="d-flex">help_outline</v-icon>
          </v-avatar>
        </template>
      </v-col>
    </v-row>
    <v-row justify="start" align="start" class="d-flex ml-4 pt-7">
      <v-col cols="1">
        <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
          <v-icon size="30" dark class="d-flex">email</v-icon>
        </v-avatar>
      </v-col>
      <v-col cols="4" class="d-flex flex-column ml-10" align="left">
        Email
        <v-main class="text-no-wrap" align="center" style="background-color: #cfcfcf">
          {{ user.user.email }}
        </v-main>
      </v-col>
    </v-row>
    <v-row justify="start" align="start" class="d-flex ml-4 pt-7">
      <v-col cols="1">
        <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
          <v-icon size="35" dark class="d-flex">lock</v-icon>
        </v-avatar>
      </v-col>
      <v-col cols="3" class="d-flex flex-column ml-10" align="left">
        Password
        <v-main class="text-no-wrap" width="" align="center" style="background-color: #cfcfcf">
          ******
        </v-main>
      </v-col>
      <v-btn
        class="d-flex mt-9 text-white customGradient"
        tile
        height="25"
        elevation="2"
        @click="router.push('passwordChange')"
      >
        Alterar
        <v-icon class="pl-1" size="15" dark>settings</v-icon>
      </v-btn>
    </v-row>
  </Container>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { computed, ref } from "vue";

import Backend from "@/router/backend";
import { useUserStore } from "@stores/user";

const user = useUserStore();
await user.init();

const router = useRouter();
const credits = ref(0);

const hasPlafond: boolean = user.isCommercial() || user.isAdmin();
const plafondDetail = ref(false);

function showPlafondDetail() {
  plafondDetail.value = true;
}

credits.value = user.user
  .credits!!.map((element) => element.amount)
  .reduce((sum, e) => sum + e, 0)
  .toFixed(2);
</script>

<style lang="scss" scoped>
.customGradient2 {
  background-image: linear-gradient(#616161, grey);
  cursor: pointer;
}
</style>
