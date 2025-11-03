<template>
  <P-Dialog modal class="max-w-95/100 w-[500px]" v-model:visible="enabled">
    <template #header>Plafond Detalhaho</template>
    <P-DataTable removeableSort :value="credits">
      <P-Column sortable field="brand" header="Marca" />
      <P-Column sortable field="plafond" header="Plafond" />
    </P-DataTable>
    <template #footer>
      <P-Button text @click="close()">Fechar</P-Button>
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
import { computed } from "vue";

import Backend from "@/router/backend";
import { Plafond } from "@router/backend/services/user/types";

const enabled = defineModel<boolean>();

const props = defineProps<{
  credits?: PlafondDetail[],
}>();

const credits = computed(() => props.credits.map((c) => {
  c.plafond = c.amount.toFixed(2);

  return c;
}));

function close() {
  enabled.value = false;
}
</script>
