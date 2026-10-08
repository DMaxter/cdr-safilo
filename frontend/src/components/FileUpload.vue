<template>
  <P-Dialog modal class="max-w-95/100 w-[500px]" v-model:visible="enabled">
    <template #header>{{ props.title }}</template>
    <P-FileUpload
      ref="fileUpload"
      customUpload
      :accept="props.accept"
      :multiple="props.multiple"
      :disabled="busy"
      :fileLimit="props.maxFiles"
      :showCancelButton="false"
      @uploader="handleUpload"
    />
    <P-Message v-if="busy" severity="info" size="small" variant="simple" class="mt-2">
      A carregar, aguarde...
    </P-Message>
    <template #footer>
      <P-Button text @click="close" :disabled="busy">Voltar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { ref, useTemplateRef } from "vue";
import type { FileUploadUploaderEvent } from "primevue/fileupload";
import type FileUpload from "primevue/fileupload";

const props = defineProps<{
  title: string;
  multiple: boolean;
  accept: string;
  maxFiles?: number;
  uploader: (f: File | File[]) => Promise<void>;
}>();

const enabled = defineModel<boolean>();

const busy = ref(false);

const fileUpload = useTemplateRef<InstanceType<typeof FileUpload>>("fileUpload");

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