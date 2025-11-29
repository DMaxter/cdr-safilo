<template>
  <P-Dialog modal class="max-w-95/100 w-[500px]" v-model:visible="enabled">
    <template #header>{{ props.title }}</template>
    <P-FileUpload
      customUpload
      :accept="props.accept"
      :multiple="props.multiple"
      :fileLimit="props.maxFiles"
      :showCancelButton="false"
      @uploader="handleUpload"
    />
    <template #footer>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import type { FileUploadUploaderEvent } from "primevue/fileupload";

const props = defineProps<{
  title: string,
  multiple: boolean,
  accept: string,
  maxFiles?: number,
  uploader: (f: File | File[]) => Promise<void>,
}>();

const enabled = defineModel<boolean>();

async function handleUpload(event: FileUploadUploaderEvent) {
  await props.uploader(event.files);
}

function close() {
  enabled.value = false;
}
</script>
