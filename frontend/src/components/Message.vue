<template>
  <v-dialog v-model="enabled" transition="dialog-bottom-transition" max-width="80%">
    <v-card>
      <v-card-text>
        <div class="text-h6 pt-12">
          {{ props.message }}
        </div>
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn text @click="disable()">Voltar </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
const props = defineProps({
  message: {
    type: String,
    required: true,
  },
});

const enabled = defineModel<boolean>();

function disable() {
  enabled.value = false;
}
</script>
