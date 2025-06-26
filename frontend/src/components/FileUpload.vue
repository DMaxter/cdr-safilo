<template>
  <v-dialog v-model="enabled" max-width="500px">
    <v-row class="d-flex justify-center">
      <v-card tile>
        <v-card-title>{{ props.title }}</v-card-title>
        <v-card-text>
          <v-file-upload
            clearable
            v-model="selectedFiles"
            :accept="props.accept"
            :multiple="props.multiple"
          ></v-file-upload>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn @click="close">Voltar</v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-if="isSelected"
            @click="emit('upload', selectedFiles)"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

const props = defineProps({
  title: {
    required: true,
    type: String,
  },
  multiple: {
    required: true,
    type: Boolean,
  },
  accept: {
    required: true,
    type: String,
  },
});

const enabled = defineModel<Boolean>();

const emit = defineEmits(["upload"]);

const selectedFiles = ref([]);
const isSelected = computed(() => selectedFiles.value.length != 0);

function handleFileUpload() {
  uploader.value.click();
}

function onFileChanged(e) {
  selectedFiles.length = 0;
  selectedFiles.unshift(...e.target.files);
}

function close() {
  selectedFiles.length = 0;

  enabled.value = false;
}
</script>
