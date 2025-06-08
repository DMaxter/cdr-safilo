<template>
  <v-dialog v-model="enabled" align="center" max-width="60%">
    <OpenWaybill v-model="enabled" v-if="!props.request.trackingCode" :request="props.request" />
  </v-dialog>
</template>

<script lang="ts" setup>
import RequestDto from "@models/RequestDto";

const props = defineProps({
  request: {
    type: RequestDto,
    required: true,
  },
});

const enabled = defineModel<boolean>();
</script>
