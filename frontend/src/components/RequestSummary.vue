<template>
  <P-Dialog v-model:visible="enabled" modal class="w-8/10 max-w-[1000px]">
    <template #header>
      <b>Pedido {{ props.request.id }}</b>
    </template>

    <div class="max-h-[70vh] overflow-y-auto">
      <RequestDetails :request="props.request" />
    </div>

    <template #footer>
      <RequestActions :request="props.request" @back="close" @refresh="emit('opened')">
        <template #extra>
          <P-Button label="Ver pedido" @click="openDetails()" />
        </template>
      </RequestActions>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";

import type { Request } from "@router/backend/services/request/types";

const router = useRouter();

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "opened"): void;
}>();

const enabled = defineModel<boolean>();

function openDetails() {
  router.push({ name: "request", params: { id: props.request.id } });
}

function close() {
  enabled.value = false;
}
</script>
