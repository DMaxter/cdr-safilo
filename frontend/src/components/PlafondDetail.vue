<template>
  <P-Dialog modal class="max-w-95/100 w-[500px]" v-model:visible="enabled">
    <template #header>Plafond Detalhado</template>
    <P-DataTable removeableSort :value="credits">
      <template #empty>Não existe plafond registado para nenhuma marca</template>
      <P-Column sortable field="brand" header="Marca" />
      <P-Column sortable field="plafond" header="Plafond" />
      <P-Column v-if="manageSafilo">
        <template #body="{ data }">
          <Icon icon="edit" @click="openPlafondManagement(data, ManageMode.Edit)" />
        </template>
      </P-Column>
    </P-DataTable>
    <template #footer>
      <P-Button
        text
        v-if="manageSafilo"
        @click="openPlafondManagement(new Plafond(), ManageMode.Add)"
        >Adicionar</P-Button
      >
      <P-Button text @click="close()">Fechar</P-Button>
    </template>
  </P-Dialog>
  <PlafondManagement
    v-if="manageSafilo"
    v-model="managePlafond"
    :plafond="selectedPlafond"
    :user="props.user"
  />
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import { Plafond } from "@router/backend/services/user/types";
import { useAuthStore } from "@stores/auth";
import { ManageMode } from "@/utils";

const authStore = useAuthStore();

const enabled = defineModel<boolean>();
const managePlafond = ref<ManageMode>(ManageMode.None);
const selectedPlafond = ref<Plafond>(new Plafond());

const props = defineProps<{
  credits?: Plafond[];
  user?: string;
}>();

const manageSafilo = authStore.isSafilo() || authStore.isAdmin();

const credits = computed(() =>
  props.credits.map((c) => {
    c.plafond = c.amount.toFixed(2);

    return c;
  }),
);

function openPlafondManagement(plafond: Plafond, mode: ManageMode) {
  managePlafond.value = mode;
  selectedPlafond.value = plafond;
}

function close() {
  enabled.value = false;
}
</script>
