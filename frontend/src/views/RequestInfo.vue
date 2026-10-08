<template>
  <Container>
    <h2 class="font-extrabold text-3xl mb-4">Pedido {{ id ?? "" }}</h2>

    <div v-if="loading" class="py-10 text-center text-gray-500">A carregar pedido...</div>

    <P-Message v-else-if="!request" severity="warn" :closable="false">
      Não foi possível encontrar o pedido {{ id }}.
    </P-Message>

    <RequestDetails v-else :request="request" />

    <template v-if="request" #actions>
      <RequestActions :request="request" @back="router.back()" @refresh="refreshRequest" />
    </template>
  </Container>
</template>

<script lang="ts" setup>
import { useToast } from "primevue/usetoast";
import { computed, onMounted, ref, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";

import type { Request } from "@router/backend/services/request/types";
import { useRequestStore } from "@stores/requests";

const TITLE = "Pedido";

const route = useRoute();
const router = useRouter();

const toast = useToast();

const requestStore = useRequestStore();

const request = ref<Request | null>(null);
const loading = ref(true);

const id = computed(() => {
  const raw = route.params.id;
  const parsed = Number(Array.isArray(raw) ? raw[0] : raw);
  return isNaN(parsed) ? null : parsed;
});

const request_ = computed(() =>
  id.value === null ? null : (requestStore.requests.find((r) => r.id === id.value) ?? null),
);

watchEffect(() => {
  request.value = request_.value;
});

function refreshRequest() {
  // Cancel/waybill actions change the request server-side, so force a re-sync.
  requestStore.getAllRequests(true);
}

onMounted(async () => {
  if (id.value === null) {
    loading.value = false;
    return;
  }

  if (request_.value) {
    loading.value = false;
    return;
  }

  const response = await requestStore.getAllRequests(true);

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: response.content ?? "Ocorreu um erro ao carregar o pedido",
      life: 10000,
    });
    console.error(response);
  }

  loading.value = false;
});
</script>
