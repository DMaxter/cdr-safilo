<template>
  <div class="flex justify-center items-center rounded-full customGradient" :style="style">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  size: number;
}>();

const style = computed(() => ({
  height: `${props.size}px`,
  width: `${props.size}px`,
}));
</script>
