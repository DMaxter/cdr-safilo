<template>
  <P-Dialog v-model="enabled" transition="dialog-bottom-transition" max-width="80%">
    <v-card>
      <v-card-text>
        <div class="text-h6 pt-12">
          {{ props.message }}
        </div>
      </v-card-text>
      <v-card-actions class="justify-end">
        <P-Button text @click="close()">Voltar</P-Button>
        <P-Button
          text
          v-if="props.action"
          @click="
            props.action();
            close();
          "
          >Confirmar</P-Button
        >
      </v-card-actions>
    </v-card>
  </P-Dialog>
</template>

<script lang="ts" setup>
import type { PropType } from "vue";

type Callback = () => void;

const props = defineProps({
  message: {
    type: String,
    required: true,
  },
  action: {
    type: Function as PropType<Callback>,
    required: false,
  },
});

const enabled = defineModel<boolean>();

function close() {
  enabled.value = false;
}
</script>
