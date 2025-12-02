<template>
  <P-ListBox v-model="model" :options="props.options" :multiple="props.multiple" :pt="ptOptions">
    <template #empty><slot name="empty"></slot></template>
    <template #header><slot name="header"></slot></template>
    <template #option="{ option, selected, index }"><slot name="option" :option="option" :selected="selected" :index="index"></slot></template>
    <template #footer><slot name="footer"></slot></template>
  </P-ListBox>
</template>

<script setup lang="ts">
const props = defineProps<{
  options?: any,
  multiple?: boolean
}>();
  
const model = defineModel<Image>();

const ptOptions = {
  list: {
    class: "grid-list"
  },
  listContainer: {
    class: "max-h-full!"
  }
};
</script>

<style scoped>
:deep(.grid-list) {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-around;
  gap: 0.5rem;
}
</style>
