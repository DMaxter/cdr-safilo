<template>
  <v-card>
    <v-card-title>Abrir Carta de Porte</v-card-title>
    <v-card-text>
      <Message v-model="success" message="Carta de porte criada com sucesso!" />
      <Message v-model="failure" :message="`Ocorreu um erro a criar a carta de porte: ${error}`" />
      <v-row justify="center">
        <v-col cols="6">
          <v-select
            label="Serviço*"
            compact
            :items="services"
            item-title="name"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.service"
          ></v-select>
        </v-col>
        <v-col cols="6">
          <v-text-field
            label="Número de pacotes*"
            type="number"
            min="1"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.items"
          ></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-select
            label="Tipo de encomenda*"
            :items="packages"
            item-title="name"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.packaging"
          ></v-select>
        </v-col>
        <v-col cols="6">
          <v-text-field
            label="Peso Total (kg)*"
            type="number"
            min="0"
            step="0.01"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.totalWeight"
          ></v-text-field>
        </v-col>
        <v-col cols="12">
          <v-text-field
            label="Descrição*"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.description"
          ></v-text-field>
        </v-col>
        <v-col cols="3">
          <v-select
            label="Formato*"
            :items="labels"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.labelFormat"
          ></v-select>
        </v-col>
        <v-col cols="3">
          <v-text-field
            label="Altura (m)*"
            type="number"
            min="0"
            step="0.01"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.dimensions.height"
          ></v-text-field>
        </v-col>
        <v-col cols="3">
          <v-text-field
            label="Largura (m)*"
            type="number"
            min="0"
            step="0.01"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.dimensions.width"
          ></v-text-field>
        </v-col>
        <v-col cols="3">
          <v-text-field
            label="Comprimento (m)*"
            type="number"
            min="0"
            step="0.01"
            :rules="[required]"
            variant="underlined"
            v-model="waybill.dimensions.length"
          ></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-text-field
            label="Nr. de Telefone"
            clearable
            variant="underlined"
            v-model="waybill.destination.phone"
          ></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-select
            label="País"
            clearable
            :items="countries"
            variant="underlined"
            v-model="waybill.destination.address.country"
          ></v-select>
        </v-col>
        <v-col cols="4">
          <v-text-field
            label="Cidade"
            clearable
            variant="underlined"
            v-model="waybill.destination.address.city"
          ></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-text-field
            label="Morada"
            clearable
            variant="underlined"
            v-model="waybill.destination.address.address"
          >
          </v-text-field>
        </v-col>
        <v-col cols="6">
          <v-text-field
            label="Cód. Postal"
            clearable
            variant="underlined"
            v-model="waybill.destination.address.postalCode"
          >
          </v-text-field>
        </v-col>
      </v-row>
    </v-card-text>
    <v-card-actions>
      <v-btn text @click="close()"> Voltar </v-btn>
      <v-btn color="blue" text @click="openWaybill()">Abrir</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts" setup>
import { reactive, ref, watch, type PropType } from "vue";

import Backend from "@/router/backend";
import RequestDto from "@models/RequestDto";
import WaybillDto from "@models/fema/WaybillDto";
import LabelFormat from "@models/fema/LabelFormat";
import PackageType from "@models/fema/PackageType";

const emit = defineEmits(["updated"]);

const enabled = defineModel<boolean>();
const success = ref(false);
const failure = ref(false);
const error = ref("");
const waybill = reactive(new WaybillDto());

const required = (value) => !!value || "Campo obrigatório";

const services = reactive([]);
const labels = await Backend.getLabelFormats();
const packages = await Backend.getFemaPackages();
const countries = ["PT", "ES"];

const props = defineProps({
  request: {
    required: true,
    type: Object as PropType<RequestDto>,
  },
});

await init();

async function init() {
  await getServices();

  waybill.destination.address.country = props.request.client.country;
  waybill.destination.address.city = props.request.client.city;
  waybill.destination.address.postalCode = props.request.client.postalCode;
  waybill.destination.address.address = props.request.client.address;
  waybill.destination.phone = props.request.client.phone;
}

async function getServices() {
  services.length = 0;

  services.unshift(...(await Backend.getFemaServices(props.request.id)));
}

async function openWaybill() {
  try {
    success.value = true;
    emit("created");
  } catch (error) {
    error.value = error;
    failure.value = true;
    console.error(error);
  }
}

function close() {
  enabled.value = false;
}
</script>
