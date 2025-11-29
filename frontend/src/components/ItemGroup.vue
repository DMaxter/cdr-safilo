<template>
  <!--FIXME: Grid not working -->
  <P-ListBox v-model="model" :options="props.options" :multiple="props.multiple"> <!--:virtualScrollerOptions="{ itemSize: [50,100] }"-->
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
</script>
