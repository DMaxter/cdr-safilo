<template>
  <div class="flex flex-col items-center justify-center max-h-97/100 h-full">
    <P-Card class="max-w-9/10 w-[1500px] max-h-9/10">
      <template #content>
        <div class="h-9/10">
          <Menu class="mb-[10px]" />
          <slot />
        </div>
      </template>
      <template #footer v-if="slots.actions">
        <div class="flex justify-around">
          <slot name="actions" />
        </div>
      </template>
    </P-Card>
    <div class="absolute bottom-0 right-0">
      <img :src="CDRLogo" class="object-contain h-[180px] w-[180px]" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useSlots } from "vue";

import CDRLogo from "@/assets/logo-slogan-white.png";

const slots = useSlots();
</script>
