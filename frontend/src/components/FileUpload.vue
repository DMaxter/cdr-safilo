<template>
  <P-Dialog v-model:visible="enabled" modal class="max-w-95/100 w-[500px]">
    <template #header>{{ props.title }}</template>
    <P-FileUpload
      ref="fileUpload"
      custom-upload
      :accept="props.accept"
      :multiple="props.multiple"
      :disabled="busy"
      :file-limit="props.maxFiles"
      :show-cancel-button="false"
      @uploader="handleUpload"
    />
    <P-Message v-if="busy" severity="info" size="small" variant="simple" class="mt-2">
      A carregar, aguarde...
    </P-Message>
    <template #footer>
      <P-Button text :disabled="busy" @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { ref, useTemplateRef } from "vue";
import type { FileUploadUploaderEvent } from "primevue/fileupload";

const props = defineProps<{
  title: string;
  multiple: boolean;
  accept: string;
  maxFiles?: number;
  uploader: (f: File | File[]) => Promise<void>;
}>();

const enabled = defineModel<boolean>();

const busy = ref(false);

const fileUpload = useTemplateRef<{ clear: () => void }>("fileUpload");

async function handleUpload(event: FileUploadUploaderEvent) {
  busy.value = true;
  try {
    await props.uploader(event.files);
  } finally {
    busy.value = false;
    // P-FileUpload keeps the selected files in its internal queue after a custom
    // upload; clear them so the same file can be picked again on the next run.
    fileUpload.value?.clear();
  }
}

function close() {
  if (busy.value) {
    return;
  }
  enabled.value = false;
}
</script>
