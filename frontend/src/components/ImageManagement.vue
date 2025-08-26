<template>
  <v-dialog v-model="enabled" persistent>
    <v-card>
      <v-card-title>Imagens</v-card-title>
      <v-card-text style="overflow: scroll">
        <span v-if="props.images.length == 0">Não existem imagens associadas</span>
        <v-item-group multiple selected-class="selected" v-model="selected">
          <v-row class="justify-center" style="gap: 5px">
            <v-item
              v-slot="{ isSelected, selectedClass, toggle }"
              v-for="image in props.images"
              :value="image.id"
            >
              <v-img
                :class="[selectedClass, 'image-slot']"
                key="id"
                :src="image.link!!"
                height="150"
                max-height="150"
                width="150"
                max-width="150"
              />
            </v-item>
          </v-row>
        </v-item-group>
      </v-card-text>
      <v-card-actions>
        <v-btn text @click="upload">Adicionar</v-btn>
        <v-btn
          :disabled="!isSelected"
          v-if="props.deletable"
          color="red darken-1"
          text
          @click="emit('delete', selected)"
          >Apagar</v-btn
        >
        <v-btn v-if="!props.deletable" text @click="emit('obsolete', selected)">Obsoleta</v-btn>
        <v-btn color="blue darken-1" text @click="close()">Voltar</v-btn>
      </v-card-actions>
    </v-card>
    <FileUpload
      v-if="props.uploadable"
      v-model="uploading"
      accept="image/*"
      title="Carregar Imagens"
      :multiple="true"
      @upload="handleUpload"
    />
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, ref, type PropType } from "vue";

import ImageDto from "@models/dto/ImageDto";

const enabled = defineModel<boolean>();

const props = defineProps({
  images: {
    required: true,
    type: Array<ImageDto>,
  },
  deletable: {
    required: true,
    type: Boolean,
  },
  uploadable: {
    required: true,
    type: Boolean,
  },
});

const selected = ref(undefined);

const emit = defineEmits(["add", "delete", "obsolete"]);

const isSelected = computed(() => selected.value !== undefined);

const uploading = ref(false);

function handleUpload(files: File[]) {
  emit("add", files);
}

function upload() {
  uploading.value = true;
}

function close() {
  enabled.value = false;
}
</script>

<style lang="scss" scoped>
.selected {
  border: 2px solid red;
}

.image-slot {
  background-color: lightgray;
}
</style>
