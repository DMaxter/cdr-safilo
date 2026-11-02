<template>
  <P-Dialog v-model:visible="enabled" modal class="w-[95vw] max-w-[700px]">
    <template #header>Imagens</template>
    <ItemGroup v-model="selected" :multiple="props.multiple" :options="props.images">
      <template #empty>Não existem imagens associadas</template>
      <template #option="{ option, selected: selectedOption }">
        <div class="img-wrapper">
          <img
            :class="[
              selectedOption ? 'selected' : '',
              option.obsolete ? 'obsolete' : '',
              'image-slot',
            ]"
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
      <div class="flex justify-end gap-2 flex-wrap">
        <P-Button label="Voltar" severity="secondary" text @click="close" />
        <P-Button
          v-if="props.uploadAction || props.addAction"
          label="Adicionar"
          severity="primary"
          @click="add"
        />
        <P-Button
          v-if="props.obsoleteAction"
          label="Marcar obsoleta"
          severity="warn"
          :disabled="!isSelected"
          @click="obsolete"
        />
        <P-Button
          v-if="props.deleteAction"
          label="Apagar"
          severity="danger"
          :disabled="!isSelected"
          @click="del"
        />
      </div>
    </template>
    <FileUpload
      v-if="props.uploadAction"
      v-model="uploading"
      accept="image/*"
      title="Carregar Imagens"
      :multiple="true"
      :uploader="handleUpload"
    />
    <P-Dialog v-model:visible="adding" modal class="w-[95vw] max-w-[500px]">
      <template #header>Adicionar Imagem</template>
      <P-FloatLabel class="field" variant="on">
        <P-Textarea id="link" ref="link" v-model="imageLink" rows="4" fluid />
        <label for="link">Links (um por linha)</label>
      </P-FloatLabel>
      <template #footer>
        <div class="flex justify-end gap-2">
          <P-Button label="Voltar" severity="secondary" text @click="closeLinkAdd" />
          <P-Button label="Adicionar" severity="primary" @click="handleAdd" />
        </div>
      </template>
    </P-Dialog>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import { Image } from "@router/backend/services/image/types";

const enabled = defineModel<boolean>();

const props = defineProps<{
  images: Image[];
  multiple?: boolean;
  obsoleteAction?: (images: Image[]) => void;
  deleteAction?: (images: Image[]) => void;
  uploadAction?: (files: File[]) => void;
  addAction?: (link: string) => void;
}>();

const selected = ref<Image | Image[] | undefined>(undefined);

const selectedImages = computed<Image[]>(() => {
  if (!selected.value) {
    return [];
  }

  return Array.isArray(selected.value) ? selected.value : [selected.value];
});

const isSelected = computed(() => selectedImages.value.length > 0);

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
  if (props.deleteAction && isSelected.value) {
    await props.deleteAction(selectedImages.value);
  }
  selected.value = undefined;
}

async function obsolete() {
  if (props.obsoleteAction && isSelected.value) {
    await props.obsoleteAction(selectedImages.value);
  }
  selected.value = undefined;
}

async function handleAdd() {
  if (props.addAction) {
    const links = imageLink.value
      .split("\n")
      .map((link) => link.trim())
      .filter((link) => link !== "");

    for (const link of links) {
      await props.addAction(link);
    }
  }
  imageLink.value = "";
}

async function handleUpload(files: File[]) {
  if (props.uploadAction) {
    await props.uploadAction(files);
  }
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
