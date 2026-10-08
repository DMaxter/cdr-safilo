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
              id="service"
              ref="service"
              v-model="waybill.service"
              fluid
              :options="services"
              option-label="name"
              :invalid="touched.service && !!errors.service"
              @blur="touch('service')"
            />
            <label for="service">Serviço</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.service && errors.service"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.service }}
          </P-Message>
        </div>
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              id="amount"
              ref="amount"
              v-model="waybill.items"
              fluid
              :min="1"
              :invalid="touched.items && !!errors.items"
              @blur="touch('items')"
            />
            <label for="amount">Número de pacotes</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.items && errors.items"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.items }}
          </P-Message>
        </div>
      </div>
      <!-- Row 2: 2 fields -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              id="package"
              ref="package"
              v-model="waybill.packaging"
              fluid
              :options="packages"
              option-label="name"
              :invalid="touched.packaging && !!errors.packaging"
              @blur="touch('packaging')"
            />
            <label for="package">Tipo de Encomenda</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.packaging && errors.packaging"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.packaging }}
          </P-Message>
        </div>
        <div class="col-span-6">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              id="weight"
              ref="weight"
              v-model="waybill.totalWeight"
              fluid
              :min="0"
              :step="0.01"
              :invalid="touched.totalWeight && !!errors.totalWeight"
              @blur="touch('totalWeight')"
            />
            <label for="weight">Peso Total (kg)</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.totalWeight && errors.totalWeight"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.totalWeight }}
          </P-Message>
        </div>
      </div>
      <!-- Row 3: 1 field -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-12">
          <P-FloatLabel class="field" variant="on">
            <P-InputText
              id="description"
              ref="description"
              v-model="waybill.description"
              fluid
              :invalid="touched.description && !!errors.description"
              @blur="touch('description')"
            />
            <label for="description">Descrição</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.description && errors.description"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.description }}
          </P-Message>
        </div>
      </div>
      <!-- Row 4: 4 fields -->
      <div class="grid grid-cols-12 gap-4">
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-Select
              id="format"
              ref="format"
              v-model="waybill.labelFormat"
              fluid
              :options="labels"
              :invalid="touched.labelFormat && !!errors.labelFormat"
              @blur="touch('labelFormat')"
            />
            <label for="format">Formato</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.labelFormat && errors.labelFormat"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.labelFormat }}
          </P-Message>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              id="height"
              ref="height"
              v-model="waybill.dimensions.height"
              fluid
              :min="0"
              :step="0.01"
              :invalid="touched.height && !!errors.height"
              @blur="touch('height')"
            />
            <label for="height">Altura (m)</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.height && errors.height"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.height }}
          </P-Message>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              id="width"
              ref="width"
              v-model="waybill.dimensions.width"
              fluid
              :min="0"
              :step="0.01"
              :invalid="touched.width && !!errors.width"
              @blur="touch('width')"
            />
            <label for="width">Largura (m)</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.width && errors.width"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.width }}
          </P-Message>
        </div>
        <div class="col-span-3">
          <P-FloatLabel class="field" variant="on">
            <P-InputNumber
              id="length"
              ref="length"
              v-model="waybill.dimensions.length"
              fluid
              :min="0"
              :step="0.01"
              :invalid="touched.length && !!errors.length"
              @blur="touch('length')"
            />
            <label for="length">Comprimento (m)</label>
          </P-FloatLabel>
          <P-Message
            v-if="touched.length && errors.length"
            class="mt-2"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ errors.length }}
          </P-Message>
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
                  <P-InputText id="phone" v-model="waybill.destination.phone" fluid />
                  <label for="phone">Número de Telefone</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-4">
                <P-FloatLabel class="field" variant="on">
                  <P-Select
                    id="country"
                    v-model="waybill.destination.address.country"
                    fluid
                    :options="countries"
                  />
                  <label for="country">País</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-4">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText id="city" v-model="waybill.destination.address.city" fluid />
                  <label for="city">Cidade</label>
                </P-FloatLabel>
              </div>
              <!-- Row 2: 2 fields -->
              <div class="col-span-6">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText
                    id="postalcode"
                    v-model="waybill.destination.address.postalCode"
                    fluid
                  />
                  <label for="postalcode">Código Postal</label>
                </P-FloatLabel>
              </div>
              <div class="col-span-6">
                <P-FloatLabel class="field" variant="on">
                  <P-InputText id="address" v-model="waybill.destination.address.address" fluid />
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
              id="downloadFormat"
              v-model="selectedDownloadFormat"
              fluid
              :options="labels"
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
      <P-Button
        v-if="!openWaybill"
        label="Cancelar Carta de Porte"
        severity="danger"
        outlined
        :loading="cancelling"
        @click="confirmCancel()"
        ><template #icon><Icon icon="cancel" /></template
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
import { required, validateField } from "@/rules";
import {
  Waybill,
  Contact,
  Address,
  Service,
  type LabelFormat,
} from "@router/backend/services/waybill/types";

const confirm = useConfirm();
const toast = useToast();

const SERVICES_TITLE = "Serviços de Carta de Porte";
const PACKAGES_TITLE = "Tipos de Embalagem";
const LABELS_TITLE = "Tamanhos de Etiqueta";
const WAYBILL_TITLE = "Carta de Porte";

const props = defineProps<{
  request: Request;
}>();

const emit = defineEmits<{
  (e: "opened"): void;
}>();

const enabled = defineModel<boolean>();

const waybill = ref<Waybill>(new Waybill());
const downloading = ref(false);
const cancelling = ref(false);
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
      prefillDestination();
      if (props.request.id !== 0) {
        await loadServices();
      } else {
        services.value = [];
      }
    }
  },
  { immediate: true },
);

function prefillDestination() {
  const client = props.request.client;

  if (!client) {
    return;
  }

  waybill.value.destination = new Contact({
    name: client.name,
    phone: client.phone,
    address: new Address({
      address: client.address,
      city: client.city,
      postalCode: client.postalCode,
      country: client.country,
    }),
  });
}

const touched = ref<Record<string, boolean>>({});

function touch(field: string) {
  touched.value[field] = true;
}

const positive = (value: number) => value > 0 || "O valor tem de ser maior que zero";

const errors = computed(() => ({
  service: waybill.value.service ? null : "Selecione um serviço",
  packaging: waybill.value.packaging ? null : "Selecione um tipo de encomenda",
  items: validateField(waybill.value.items, [positive]),
  totalWeight: validateField(waybill.value.totalWeight, [positive]),
  labelFormat: waybill.value.labelFormat ? null : "Selecione um formato",
  description: validateField(waybill.value.description.trim(), [required]),
  height: validateField(waybill.value.dimensions.height, [positive]),
  width: validateField(waybill.value.dimensions.width, [positive]),
  length: validateField(waybill.value.dimensions.length, [positive]),
}));

const errorList = computed(() =>
  Object.values(errors.value).filter((error): error is string => error !== null),
);
const isValid = computed(() => errorList.value.length === 0);

function orNull(value: string | null): string | null {
  const trimmed = value?.trim();
  return trimmed ? trimmed : null;
}

function confirmCancel() {
  confirm.require({
    message: `Tem a certeza que pretende cancelar a carta de porte do pedido ${props.request.id}?`,
    header: "Confirmar cancelamento de carta de porte",
    rejectProps: {
      label: "Voltar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Cancelar carta de porte",
      severity: "danger",
    },
    accept: cancelWaybill,
  });
}

async function loadServices() {
  if (props.request.id !== 0) {
    try {
      const { status, data } = await API.waybill.getShippingServices(props.request.id);
      if (status === 200) {
        services.value = data as Service[];
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

async function cancelWaybill() {
  cancelling.value = true;

  try {
    const { status } = await API.waybill.cancelWaybill(props.request.id);

    if (status === 200 || status === 204) {
      toast.add({
        severity: "success",
        summary: WAYBILL_TITLE,
        detail: "Carta de porte cancelada com sucesso",
        life: 5000,
      });
      close();
      emit("opened");
    } else {
      toast.add({
        severity: "error",
        summary: WAYBILL_TITLE,
        detail: "Ocorreu um erro ao cancelar a carta de porte",
        life: 10000,
      });
    }
  } catch (error) {
    toast.add({
      severity: "error",
      summary: WAYBILL_TITLE,
      detail: "Ocorreu um erro ao cancelar a carta de porte",
      life: 10000,
    });
    console.error(error);
  } finally {
    cancelling.value = false;
  }
}

function buildPayload(): Waybill {
  const payload = new Waybill(waybill.value);

  payload.destination = new Contact({
    name: orNull(waybill.value.destination.name),
    phone: orNull(waybill.value.destination.phone),
    address: new Address({
      address: orNull(waybill.value.destination.address.address),
      city: orNull(waybill.value.destination.address.city),
      postalCode: orNull(waybill.value.destination.address.postalCode),
      country: orNull(waybill.value.destination.address.country),
    }),
  });

  return payload;
}

async function createWaybill() {
  for (const field of Object.keys(errors.value)) {
    touched.value[field] = true;
  }

  if (!isValid.value) {
    toast.add({
      severity: "warn",
      summary: WAYBILL_TITLE,
      detail: errorList.value[0],
      life: 10000,
    });
    return;
  }

  try {
    const { status, data } = await API.waybill.openWaybill(props.request.id, buildPayload());

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
        summary: WAYBILL_TITLE,
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
