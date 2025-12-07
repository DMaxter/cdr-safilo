<template>
  <P-Dialog modal v-model:visible="enabled" class="w-[700px]">
    <template #header>Imagens</template>
    <ItemGroup :multiple="props.multiple" :options="props.images" v-model="selected">
      <template #empty>Não existem imagens associadas</template>
      <template #option="{ option, selected }">
        <div class="img-wrapper">
          <img
            :class="[selected ? 'selected' : '', option.obsolete ? 'obsolete' : '', 'image-slot']"
            :src="option.link!!"
            height="150"
            max-height="150"
            width="150"
            max-width="150"
          />
        </div>
      </template>
    </ItemGroup>
    <template #footer>
      <P-Button text @click="add">Adicionar</P-Button>
      <P-Button :disabled="!isSelected" v-if="props.deleteAction" text @click="del"
        >Apagar</P-Button
      >
      <P-Button v-if="props.obsoleteAction" text @click="obsolete">Marcar obsoleta</P-Button>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
    <FileUpload
      v-if="props.uploadAction"
      v-model="uploading"
      accept="image/*"
      title="Carregar Imagens"
      :multiple="true"
      :uploader="handleUpload"
    />
    <P-Dialog modal v-model:visible="adding" class="w-[500px]">
      <template #header>Adicionar Imagem</template>
      <P-FloatLabel class="field" variant="on">
        <P-InputText fluid id="link" ref="link" v-model="imageLink" />
        <label for="link">Link</label>
      </P-FloatLabel>
      <template #footer>
        <P-Button @click="handleAdd">Adicionar</P-Button>
        <P-Button @click="closeLinkAdd">Voltar</P-Button>
      </template>
    </P-Dialog>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import type Image from "@router/backend/services/image/types";

const enabled = defineModel<boolean>();

const props = defineProps<{
  images: Image[];
  multiple?: boolean;
  obsoleteAction?: (images: Image[]) => void;
  deleteAction?: (images: Image[]) => void;
  uploadAction?: (files: File[]) => void;
  addAction?: (link: string) => void;
}>();

const selected = ref(undefined);
const isSelected = computed(() => selected.value !== undefined);

const uploading = ref(false);
const adding = ref(false);

const imageLink = ref("");

async function add() {
  if (props.uploadAction) {
    uploading.value = true;
  } else {
    adding.value = true;
  }
}

async function del() {
  await props.deleteAction(selected.value);
  selected.value = undefined;
}

async function obsolete() {
  await props.obsoleteAction(selected.value);
  selected.value = undefined;
}

async function handleAdd() {
  await props.addAction(imageLink.value);
  imageLink.value = "";
}

async function handleUpload(files: File[]) {
  await props.uploadAction(files);
}

function close() {
  enabled.value = false;
  selected.value = undefined;
}

function closeLinkAdd() {
  adding.value = false;
  imageLink.value = "";
}
</script>

<style lang="scss" scoped>
.img-wrapper {
  position: relative;
}

.selected {
  border: 4px solid var(--color-pink-400);
}

.image-slot {
  background-color: lightgray;
}

// Apply overlay over obsolete images
.img-wrapper:has(> .obsolete)::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(90deg, hsla(0, 0%, 55%, 0.5), hsla(0, 0%, 55%, 0.5)),
    linear-gradient(
      to top right,
      transparent,
      transparent calc(50% - 3px),
      #000 50%,
      transparent calc(50% + 3px),
      transparent
    );
}
</style>
