<template>
  <div class="flex flex-wrap items-center gap-2">
    <P-Button v-if="canEdit(props.request)" label="Editar" severity="secondary" @click="edit()">
      <template #icon><Icon icon="edit" /></template>
    </P-Button>
    <P-Button
      v-if="canCancel(props.request)"
      label="Cancelar"
      severity="danger"
      @click="confirmCancel()"
    />
    <P-Button label="Voltar" severity="secondary" @click="emit('back')" />
    <P-Button label="Imprimir" severity="info" @click="print()">
      <template #icon><Icon icon="print" /></template>
    </P-Button>
    <P-Button
      v-if="canSeeWaybill(props.request)"
      label="Carta de Porte"
      severity="secondary"
      @click="waybill = true"
    >
      <template #icon><Icon icon="delivery_truck_speed" /></template>
    </P-Button>

    <slot name="extra" />

    <PrintRequest class="only-print" ref="printer" :request="props.request" />
    <Waybill v-model="waybill" :request="props.request" @opened="emit('refresh')" />
  </div>
</template>

<script lang="ts" setup>
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { ref, useTemplateRef } from "vue";
import { useRouter } from "vue-router";

import PrintRequestComponent from "@components/PrintRequest.vue";
import type { Request } from "@router/backend/services/request/types";
import { useRequestAccess } from "@/composables/useRequestAccess";
import { useRequestStore } from "@stores/requests";

const TITLE = "Cancelamento de Pedido";

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "back"): void;
  (e: "refresh"): void;
}>();

const confirm = useConfirm();
const toast = useToast();

const router = useRouter();

const requestStore = useRequestStore();
const { canCancel, canEdit, canSeeWaybill } = useRequestAccess();

const printer = useTemplateRef<typeof PrintRequestComponent>("printer");
const waybill = ref(false);

function edit() {
  router.push({ name: "order", query: { id: props.request.id } });
}

function print() {
  printer.value?.handlePrint();
}

function confirmCancel() {
  confirm.require({
    message: `Tem a certeza que pretende cancelar o pedido ${props.request.id} efetuado por ${props.request.user ?? "Desconhecido"} para o cliente ${props.request.client?.name ?? "Desconhecido"}?`,
    header: "Confirmar cancelamento de pedido",
    rejectProps: {
      label: "Abortar cancelamento",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Confirmar cancelamento",
      severity: "danger",
    },
    accept: cancelRequest,
  });
}

async function cancelRequest() {
  const response = await requestStore.cancelRequest(props.request.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Pedido cancelado com sucesso",
      life: 10000,
    });

    // The parent re-syncs the store so every view (dialog, page, list) sees the new status.
    emit("refresh");
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro ao cancelar o pedido",
      life: 10000,
    });
    console.error(response);
  }
}
</script>
