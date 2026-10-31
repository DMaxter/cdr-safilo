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

    <PrintRequest ref="printer" class="only-print" :request="props.request" @printed="onPrinted" />
    <Waybill
      v-if="canSeeWaybill(props.request)"
      v-model="waybill"
      :request="props.request"
      @changed="emit('refresh')"
    />
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
const PRODUCTION_TITLE = "Produção de Pedido";

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
const { canCancel, canEdit, canSeeWaybill, canSendToProduction } = useRequestAccess();

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

function onPrinted() {
  if (!canSendToProduction(props.request)) {
    return;
  }

  confirm.require({
    message: `O pedido ${props.request.id} foi impresso? Se sim, será marcado como "Em Produção".`,
    header: "Confirmar impressão do pedido",
    rejectProps: {
      label: "Não",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Sim, marcar em produção",
    },
    accept: sendToProduction,
  });
}

async function sendToProduction() {
  const response = await requestStore.toProduction(props.request.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: PRODUCTION_TITLE,
      detail: "Pedido em produção",
      life: 10000,
    });

    emit("refresh");
  } else {
    toast.add({
      severity: "error",
      summary: PRODUCTION_TITLE,
      detail: "Ocorreu um erro ao marcar o pedido em produção",
      life: 10000,
    });

    console.error(response);
  }
}
</script>
