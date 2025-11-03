<template>
  <Container>
    <div class="flex flex-col justify-center items-center mb-[10px] mt-[30px]">
      <Circle :size="100">
        <Icon icon="account_circle" :size="80" />
      </Circle>
      {{ authStore.logged.name }}
    </div>
    <div class="my-[30px] flex justify-around">
      <div class="flex justify-start items-center">
        <Circle :size="50">
          <Icon icon="attach_money" :size="50" />
        </Circle>
        <div class="ml-[10px]">
          Plafond Total
          <div class="field">
            {{ credits }}
          </div>
        </div>
        <div class="ml-[10px]">
          <template v-if="hasPlafond">
            <PlafondDetail v-model="plafondDetail" :credits="authStore.logged.credits" />
            <Circle
              class="cursor-pointer"
              :size="30"
              @click="showPlafondDetail()"
            >
              <Icon icon="help_outline" :size="20" />
            </Circle>
          </template>
        </div>
      </div>
      <div class="flex justify-around items-center">
        <Circle :size="50">
          <Icon icon="email" :size="40" />
        </Circle>
        <div class="ml-[10px]">
          Email
          <div class="field">
            {{ authStore.logged.email }}
          </div>
        </div>
      </div>
      <div class="flex justify-around items-center">
        <Circle :size="50">
          <Icon icon="lock" :size="40" />
        </Circle>
        <div class="ml-[10px]">
          Password
          <div class="field">
            **********
          </div>
        </div>
        <P-Button
          class="ml-[10px]"
          @click="showPasswordChange()"
        >
          Alterar
          <Icon icon="settings" :size="15" />
        </P-Button>
      </div>
    </div>
  </Container>
  <PasswordChange v-model="changePassword" />
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

import { useAuthStore } from "@stores/auth";

const authStore = useAuthStore();

const router = useRouter();
const credits = authStore.logged
  .credits!!.map((element) => element.amount)
  .reduce((sum, e) => sum + e, 0)
  .toFixed(2);

const hasPlafond: boolean = authStore.isCommercial() || authStore.isAdmin();
const plafondDetail = ref(false);

const changePassword = ref(false);

function showPlafondDetail() {
  plafondDetail.value = true;
}

function showPasswordChange() {
  changePassword.value = true;
}
</script>

<style lang="scss" scoped>
@use "@styles/variables" as *;

.field {
  background-color: $light-grey;
}

.customGradient2 {
  background-image: linear-gradient(#616161, grey);
  cursor: pointer;
}
</style>
