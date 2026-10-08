<template>
  <P-Dialog v-model:visible="enabled" modal header="Selecionar Imagem" :style="{ width: '600px' }">
    <div v-if="images.length === 0" class="text-center p-4">Não existem imagens disponíveis</div>
    <div v-else class="grid grid-cols-3 gap-3 p-2">
      <div
        v-for="image in images"
        :key="image.id"
        class="border rounded cursor-pointer overflow-hidden hover:border-primary transition-colors"
        :class="{ 'border-primary ring-2 ring-primary': selectedId === image.id }"
        @click="select(image)"
      >
        <img :src="image.link" :alt="`Imagem ${image.id}`" class="w-full h-32 object-cover" />
      </div>
    </div>
    <template #footer>
      <P-Button text label="Cancelar" @click="close()" />
      <P-Button label="Confirmar" :disabled="!selectedId" @click="confirm()" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import type { Image } from "@router/backend/services/image/types";

const enabled = defineModel<boolean>();

const props = defineProps<{
  images: Image[];
}>();

const emit = defineEmits<{
  (e: "select", image: Image): void;
}>();

const selectedId = ref<number | null>(null);

const selectedImage = computed(() => {
  if (!selectedId.value) return null;
  return props.images.find((img) => img.id === selectedId.value) ?? null;
});

function select(image: Image) {
  selectedId.value = image.id;
}

function confirm() {
  if (selectedImage.value) {
    emit("select", selectedImage.value);
    close();
  }
}

function close() {
  selectedId.value = null;
  enabled.value = false;
}
</script>
