<template>
  <P-Dialog v-model:visible="enabled" modal :style="{ width: '50vw' }">
    <template #header>
      <b>Pedido {{ props.request.id }}</b>
    </template>
    <div class="grid p-fluid">
      <div class="col-12 md:col-4">
        <label>Tipo:</label>
        <div class="font-bold">
          {{
            requestTypes.find((type) => type.name === props.request.type!!.type)?.value ||
            "Tipo desconhecido"
          }}
        </div>
      </div>
      <div class="col-12 md:col-4">
        <label>Marca:</label>
        <div class="font-bold">{{ props.request.brand!!.name }}</div>
      </div>
      <div class="col-12 md:col-4">
        <label>Estado:</label>
        <br />
        <P-Tag rounded :class="statusColorClass">
          <Icon :icon="statusIcon" />
          <span>{{ statusLabel }}</span>
        </P-Tag>
      </div>
      <div class="col-12 md:col-8" v-if="isCdr && props.request.status !== Status.Cancelled">
        <label>Carta de Porte:</label>
        <div class="font-bold">
          {{ props.request.trackingCode !== null ? props.request.trackingCode : "Não existe" }}
        </div>
      </div>
      <div class="col-12 md:col-4">
        <label>Cliente:</label>
        <div class="font-bold">
          <router-link :to="{ name: 'client', query: { id: props.request.client!!.id } }"
            >{{ props.request.client!!.name }}<Icon icon="open_in_new"
          /></router-link>
        </div>
      </div>
      <div class="col-12 md:col-8">
        <label>Morada:</label>
        <div class="font-bold">
          {{ props.request.client!!.address }}, {{ props.request.client!!.city }}
        </div>
      </div>
      <div class="col-12 md:col-4">
        <label>Comercial:</label>
        <div class="font-bold">{{ props.request.user }}</div>
      </div>
      <div class="col-12 md:col-4">
        <label>Data de criação:</label>
        <div class="font-bold">{{ new Date(props.request.created!!).toLocaleString("pt-PT") }}</div>
      </div>
      <div class="col-12 md:col-4">
        <label>Última atualização:</label>
        <div class="font-bold">
          {{ new Date(props.request.lastUpdate!!).toLocaleString("pt-PT") }}
        </div>
      </div>
      <div class="col-12 md:col-4">
        <label>Custo:</label>
        <div class="font-bold">{{ props.request.cost!!.toFixed(2) }} Créditos</div>
      </div>
      <div class="col-12 md:col-4">
        <label>Materiais:</label>
        <br />
        <P-Chip
          v-for="material in materials"
          :label="material"
          :pt="{
            label: { style: 'font-weight: bold' },
          }"
        />
      </div>
      <div class="col-12 md:col-4">
        <label>Acabamentos:</label>
        <br />
        <P-Chip
          v-if="finishings.length > 0"
          v-for="finishing in finishings"
          :label="finishing"
          :pt="{
            label: { style: 'font-weight: bold' },
          }"
        />
        <b v-if="finishings.length === 0">Não existem acabamentos</b>
      </div>
      <div class="col-12 md:col-4">
        <label>Medidas (altura x largura - cm):</label>
        <div class="font-bold">{{ processMeasurements(props.request.getMeasurements()) }}</div>
      </div>
      <div class="col-12">
        <label>Observações:</label>
        <div class="font-bold">
          {{ props.request.observations ? props.request.observations : "Sem observações" }}
        </div>
      </div>
    </div>

    <template #footer>
      <P-Button
        label="Cancelar"
        @click="confirmCancel()"
        class="p-button-danger mr-2"
        v-if="
          (canManipulate || (user.isCommercial() && request.user == user.user.name)) &&
          props.request.status == Status.Ordered
        "
      ></P-Button>
      <P-Button label="Voltar" @click="close()" class="p-button-secondary mr-2"></P-Button>
      <P-Button @click="print()" label="Imprimir" class="p-button-info mr-2"
        ><template #icon><Icon icon="print" /></template
      ></P-Button>
      <P-Button
        label="Carta de Porte"
        @click="openWaybillDialog()"
        class="p-button-secondary mr-2"
        v-if="isCdr && props.request.status !== Status.Cancelled"
        ><template #icon><Icon icon="delivery_truck_speed" /></template
      ></P-Button>
      <P-Button label="Ver pedido" @click="openDetails()" class="p-button-primary"></P-Button>

      <PrintRequest class="only-print" ref="printer" :request="props.request" />
      <Waybill v-model="waybill" :request="props.request" @opened="refreshRequest" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { computed, ref, useTemplateRef } from "vue";
import { useRouter } from "vue-router";

import { useAuthStore } from "@stores/auth";
import { useRequestStore } from "@stores/requests";
import { Status } from "@router/backend/services/request/types";
import type { Request } from "@router/backend/services/request/types";
import PrintRequestComponent from "@components/PrintRequest.vue";
import { requestTypes, statusItems } from "@/maps";
import { getStatusClass, getStatusIcon } from "@/utils";

const confirm = useConfirm();
const toast = useToast();

const TITLE = "Cancelamento de Pedido";

const router = useRouter();

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "opened"): void;
}>();

const enabled = defineModel<boolean>();

const authStore = useAuthStore();
const requestStore = useRequestStore();

const canManipulate = authStore.isSafilo() || authStore.isCdr() || authStore.isAdmin();
const isCdr = authStore.isCdr() || authStore.isAdmin();

const printer = useTemplateRef<typeof PrintRequestComponent>("printer");

const waybill = ref(false);

const states = statusItems;

const finishings = computed(() => props.request.getFinishings());
const materials = computed(() => props.request.getMaterials());

const statusIcon = computed(() => getStatusIcon(props.request.status));
const statusColorClass = computed(() => getStatusClass(props.request.status));
const statusLabel = computed(
  () => states.find((s) => s.value === props.request.status)?.name || "Tipo desconhecido",
);

function refreshRequest() {
  emit("opened");
}

function print() {
  printer.value!!.handlePrint();
}

function openWaybillDialog() {
  waybill.value = true;
}

function openDetails() {
  router.push({ name: "request", params: { id: props.request.id } });
}

function close() {
  enabled.value = false;
}

function confirmCancel() {
  confirm.require({
    message: `Tem a certeza que pretende cancelar o pedido ${props.request.id} efetuado por ${props.request.user} para o cliente ${props.request.client.name}?`,
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
    emit("opened");
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

// Helper functions
function processMeasurements(measurements: number[][]) {
  return measurements.map((m) => m.join("x")).join(", ");
}
</script>
