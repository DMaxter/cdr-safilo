<template>
  <P-Dialog v-model:visible="enabled" modal>
    <template #header
      ><b>{{ openWaybill ? "Abrir Carta de Porte" : "Descarregar Carta de Porte" }}</b></template
    >
    <div v-if="openWaybill">
      <!-- Row 1: 2 fields -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              fluid
              id="service"
              ref="service"
              :options="services"
              optionLabel="name"
              v-model="waybill.service"
            />
            <label for="service">Serviço</label>
          </P-FloatLabel>
        </div>
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber fluid id="amount" ref="amount" :min="1" v-model="waybill.items" />
            <label for="amount">Número de pacotes</label>
          </P-FloatLabel>
        </div>
      </div>
      <!-- Row 2: 2 fields -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              fluid
              id="package"
              ref="package"
              :options="packages"
              optionLabel="name"
              v-model="waybill.packaging"
            />
            <label for="package">Tipo de Encomenda</label>
          </P-FloatLabel>
        </div>
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              fluid
              id="weight"
              ref="weight"
              :min="0"
              :step="0.01"
              v-model="waybill.totalWeight"
            />
            <label for="weight">Peso Total (kg)</label>
          </P-FloatLabel>
        </div>
      </div>
      <!-- Row 3: 1 field -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-12">
          <P-FloatLabel class="field" variant="on">
            <P-InputText fluid id="description" ref="description" v-model="waybill.description" />
            <label for="description">Descrição</label>
          </P-FloatLabel>
        </div>
      </div>
      <!-- Row 4: 4 fields -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              fluid
              id="format"
              ref="format"
              :options="labels"
              v-model="waybill.labelFormat"
            />
            <label for="format">Formato</label>
          </P-FloatLabel>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              fluid
              id="height"
              ref="height"
              :min="0"
              :step="0.01"
              v-model="waybill.dimensions.height"
            />
            <label for="height">Altura (m)</label>
          </P-FloatLabel>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              fluid
              id="width"
              ref="width"
              :min="0"
              :step="0.01"
              v-model="waybill.dimensions.width"
            />
            <label for="width">Largura (m)</label>
          </P-FloatLabel>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              fluid
              id="length"
              ref="length"
              label="Comprimento (m)*"
              :min="0"
              :step="0.01"
              v-model="waybill.dimensions.length"
            />
            <label for="length">Comprimento (m)</label>
          </P-FloatLabel>
        </div>
      </div>
      <P-Accordion>
        <P-AccordionPanel value="">
          <P-AccordionHeader>Dados do Cliente (opcional)</P-AccordionHeader>
          <P-AccordionContent fluid>
            <div class="grid grid-cols-12 gap-4">
              <!-- Row 1: 3 fields -->
              <div class="col-span-4">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText fluid id="phone" v-model="waybill.destination.phone" />
                  <label for="phone">Número de Telefone</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-4">
                <P-FloatLabel class="field" variant="on">
                  <P-Select
                    fluid
                    id="country"
                    :options="countries"
                    v-model="waybill.destination.address.country"
                  />
                  <label for="country">País</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-4">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText fluid id="city" v-model="waybill.destination.address.city" />
                  <label for="city">Cidade</label>
                </P-FloatLabel>
              </div>
              <!-- Row 2: 2 fields -->
              <div class="col-span-6">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText
                    fluid
                    id="postalcode"
                    v-model="waybill.destination.address.postalCode"
                  />
                  <label for="postalcode">Código Postal</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-6">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText fluid id="address" v-model="waybill.destination.address.address" />
                  <label for="address">Morada</label>
                </P-FloatLabel>
              </div>
            </div>
          </P-AccordionContent>
        </P-AccordionPanel>
      </P-Accordion>
    </div>
    <div v-else>
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-12">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              fluid
              id="downloadFormat"
              :options="labels"
              v-model="selectedDownloadFormat"
            />
            <label for="downloadFormat">Formato para Descarregar</label>
          </P-FloatLabel>
        </div>
      </div>
    </div>
    <template #footer>
      <P-Button class="p-button-secondary mr-2" label="Voltar" @click="close()" />
      <P-Button v-if="openWaybill" label="Abrir" @click="createWaybill()"
        ><template #icon><Icon icon="file_open" /></template
      ></P-Button>
      <P-Button
        v-if="!openWaybill"
        label="Descarregar"
        :loading="downloading"
        @click="showDownload()"
        ><template #icon><Icon icon="download" /></template
      ></P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { computed, ref, watch } from "vue";

import { API } from "@router/backend";
import type { Request } from "@router/backend/services/request/types";
import { Waybill, Service, type LabelFormat } from "@router/backend/services/waybill/types";

// TODO: Implement all validations

const confirm = useConfirm();
const toast = useToast();

const SERVICES_TITLE = "Serviços de Carta de Porte";
const PACKAGES_TITLE = "Tipos de Embalagem";
const LABELS_TITLE = "Tamanhos de Etiqueta";

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "opened"): void;
}>();

const enabled = defineModel<boolean>();

const waybill = ref<Waybill>(new Waybill());
const downloading = ref(false);
const selectedDownloadFormat = ref<LabelFormat | null>(null);

const openWaybill = computed(() => props.request.trackingCode === null);

// FIXME: Get countries from Backend - to be implemented
const countries = ["PT", "ES"];
const services = ref<Service[]>([]);
const [labels, packages] = await Promise.all([getLabels(), getPackages()]);

watch(
  () => enabled.value,
  async (newVal) => {
    if (newVal) {
      if (props.request.id !== 0) {
        await loadServices();
      } else {
        services.value = [];
      }
    }
  },
  { immediate: true },
);

function confirmCancel() {}

async function loadServices() {
  if (props.request.id !== 0) {
    try {
      const { status, data } = await API.waybill.getShippingServices(props.request.id);
      if (status === 200) {
        services.value = data;
      } else {
        toast.add({
          severity: "error",
          summary: SERVICES_TITLE,
          detail: "Ocorreu um erro a obter os serviços para a carta de porte",
          life: 10000,
        });
      }
    } catch (error) {
      toast.add({
        severity: "error",
        summary: SERVICES_TITLE,
        detail: "Ocorreu um erro a obter os serviços para a carta de porte",
        life: 10000,
      });
      console.error(error);
    }
  }
}

async function getPackages() {
  try {
    const { status, data } = await API.waybill.getPackageTypes();

    if (status === 200) {
      return data;
    } else {
      toast.add({
        severity: "error",
        summary: PACKAGES_TITLE,
        detail: "Ocorreu um erro a obter os tipos de embalagem para a carte de porte",
        life: 10000,
      });
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: PACKAGES_TITLE,
      detail: "Ocorreu um erro a obter os tipos de embalagem para a carte de porte",
      life: 10000,
    });
    console.error(error);
  }
}

async function getLabels() {
  try {
    const { status, data } = await API.waybill.getLabelFormats();

    if (status === 200) {
      return data;
    } else {
      toast.add({
        severity: "error",
        summary: LABELS_TITLE,
        detail: "Ocorreu um erro a obter o tamanho das etiquetas para a carte de porte",
        life: 10000,
      });
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: LABELS_TITLE,
      detail: "Ocorreu um erro a obter o tamanho das etiquetas para a carte de porte",
      life: 10000,
    });
    console.error(error);
  }
}

async function cancel() {
  try {
    // TODO:
    return (
      "Tem a certeza que pretende cancelar a carta de porte do pedido " + props.request.id + "?"
    );
  } catch (error) {
    console.error(error);
  }
}

async function createWaybill() {
  try {
    const { status, data } = await API.waybill.openWaybill(props.request.id, waybill.value);

    if (status === 200 || status === 201) {
      const blob = data as Blob;
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `waybill_${props.request.id}.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);

      toast.add({
        severity: "success",
        summary: "Carta de Porte",
        detail: "Carta de porte aberta com sucesso",
        life: 5000,
      });
      close();
      emit("opened");
    } else {
      toast.add({
        severity: "error",
        summary: SERVICES_TITLE,
        detail: "Ocorreu um erro ao abrir a carta de porte",
        life: 10000,
      });
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: SERVICES_TITLE,
      detail: "Ocorreu um erro ao abrir a carta de porte",
      life: 10000,
    });
    console.error(error);
  }
}

function cancelMessage() {}

async function showDownload() {
  if (!selectedDownloadFormat.value) {
    toast.add({
      severity: "warn",
      summary: LABELS_TITLE,
      detail: "Por favor selecione um formato para descarregar",
      life: 5000,
    });
    return;
  }

  downloading.value = true;
  try {
    const { status, data } = await API.waybill.getWaybill(
      props.request.id,
      selectedDownloadFormat.value,
    );

    if (status === 200) {
      const blob = data as Blob;
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `waybill_${props.request.id}.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);

      toast.add({
        severity: "success",
        summary: "Descarregar Carta de Porte",
        detail: "Carta de porte descarregada com sucesso",
        life: 5000,
      });
    } else {
      toast.add({
        severity: "error",
        summary: LABELS_TITLE,
        detail: "Ocorreu um erro ao descarregar a carta de porte",
        life: 10000,
      });
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: LABELS_TITLE,
      detail: "Ocorreu um erro ao descarregar a carta de porte",
      life: 10000,
    });
    console.error(error);
  } finally {
    downloading.value = false;
  }
}

function close() {
  enabled.value = false;
  selectedDownloadFormat.value = null;
}
</script>
