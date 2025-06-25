<template>
  <v-dialog v-model="enabled" persistent max-width="500px">
    <v-card>
      <v-card-title>Nota do cliente</v-card-title>
      <v-card-text>
        <v-textarea v-model="props.client.note" />
      </v-card-text>
      <v-card-actions>
        <v-btn hide-details color="blue darken-1" text @click="close()">Voltar</v-btn>
        <v-btn hide-details color="blue darken-1" text @click="edit()">Editar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
    <Message v-model="success" message="Nota editada com sucesso" />
    <Message v-model="failure" message="Ocorreu um erro ao editar a nota" />
</template>

<script lang="ts" setup>
import { ref, type PropType } from "vue";

import { useUserStore } from "@stores/user";
import Backend from "@/router/backend";

const emit = defineEmits(["updated"]);

const props = defineProps({
  client: {
    required: true,
    type: Object as PropType<ClientDto>,
  },
});

const user = useUserStore();
await user.init();

const enabled = defineModel<boolean>();

const success = ref(false);
const failure = ref(false);

async function edit() {
  try {
    await Backend.editNote(props.client.id, props.client.note);
    success.value = true;
    emit("updated");
    close();
  } catch (error) {
    failure.value = true;
    console.error(error);
    throw Error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>
