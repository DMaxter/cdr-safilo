<template>
  <v-dialog v-model="enabled" persistent content-class="rounded-0" max-width="500px">
    <v-card>
      <v-card-title>Plafond Detalhado</v-card-title>
      <v-card-text>
        <v-container>
          <v-data-table
            height="500px"
            max-height="100%"
            fixed-header
            :headers="headers"
            :items="props.credits"
            hide-default-footer
            disable-pagination
            item-key="brand"
            items-per-page="-1"
            class="elevation-1 my-header-style"
          ></v-data-table>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn hide-details text @click="close()">Fechar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import type { PropType } from "vue";

import Backend from "@/router/backend";
import { PlafondDetail } from "@models/UserDto";

const headers = [
  {
    title: "Marca",
    value: "brand",
    width: "200px",
    align: "left",
    class: "my-header-style",
    sortable: true,
  },
  {
    title: "Plafond",
    width: "200px",
    align: "right",
    key: "amount",
    class: "my-header-style",
    sortable: true,
    value: (item) => item.amount.toFixed(2),
  },
];

const enabled = defineModel<boolean>();

const props = defineProps({
  credits: {
    required: true,
    type: Array<PlafondDetail>,
  },
});

function close() {
  enabled.value = false;
}
</script>

<style lang="scss" scoped>
.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}
</style>
