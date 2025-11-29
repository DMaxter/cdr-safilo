<template>
  <P-Dialog modal v-model:visible="enabled" class="w-[500px]">
    <template #header>Imagens</template>
    <ItemGroup multiple :options="props.images" v-model="selected">
        <template #empty>Não existem imagens associadas</template>
        <template #option="{ option, selected }">
          <img
            :class="[selected ? 'selected' : '', 'image-slot']"
            :src="option.link!!"
            height="150"
            max-height="150"
            width="150"
            max-width="150"
          />
        </template>
    </ItemGroup>
    <template #footer>
      <P-Button text @click="add">Adicionar</P-Button>
      <P-Button
        :disabled="!isSelected"
        v-if="props.deleteAction"
        text
        @click="del"
        >Apagar</P-Button
      >
      <P-Button v-if="props.obsoleteAction" text @click="props.obsoleteAction">Obsoleta</P-Button>
      <P-Button text @click="close()">Voltar</P-Button>
    </template>
    <FileUpload
      v-if="props.uploadAction"
      v-model="uploading"
      accept="image/*"
      title="Carregar Imagens"
      :multiple="true"
      :uploader="handleUpload"
    />
    <!-- TODO: Add by link -->
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import type Image from "@router/backend/services/image/types";

const enabled = defineModel<boolean>();

const props = defineProps<{
  images: Image[],
  obsoleteAction?: (images: Image[]) => void,
  deleteAction?: (images: Image[]) => void,
  uploadAction?: (files: File[]) => void,
  addAction?: (link: string) => void,
}>();

const selected = ref(undefined);

const isSelected = computed(() => selected.value !== undefined);

const uploading = ref(false);

async function handleUpload(files: File[]) {
  await props.uploadAction(files)
}

async function add() {
  if (props.uploadAction) {
    uploading.value = true;
  } else {
    console.log("TODO")
    //await props.addAction();
  }
}

async function del() {
  await props.deleteAction(selected.value);
  selected.value = undefined;
}

function close() {
  enabled.value = false;
}
</script>

<style lang="scss" scoped>
.selected {
  border: 4px solid var(--color-pink-400);
}

.image-slot {
  background-color: lightgray;
}
</style>
