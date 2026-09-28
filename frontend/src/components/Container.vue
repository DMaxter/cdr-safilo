<template>
  <div class="flex flex-col items-center justify-center max-h-97/100 h-full">
    <P-Card class="max-w-9/10 w-[1500px] max-h-9/10 overflow-y-auto overflow-x-hidden">
      <template #content>
        <Menu class="mb-[10px]" />
        <slot />
      </template>
      <template #footer v-if="slots.actions">
        <div class="flex justify-around">
          <slot name="actions" />
        </div>
      </template>
    </P-Card>
  </div>
  <div class="absolute bottom-0 right-0">
    <img
      :src="CDRLogo"
      class="pointer-events-none object-contain h-[90px] w-[90px] sm:h-[180px] sm:w-[180px]"
    />
  </div>
</template>

<script lang="ts" setup>
import { useSlots } from "vue";

import CDRLogo from "@/assets/logo-slogan-white.png";

const slots = useSlots();
</script>
