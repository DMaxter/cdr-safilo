<template>
  <v-dialog @afterLeave="emit('filter')">
    <template v-slot:activator="{ props }">
      <v-btn variant="plain" width="24" min-width="24" height="24" style="padding: 0" v-bind="props"
        ><v-icon>$filter</v-icon></v-btn
      >
    </template>
    <template v-slot:default="{ isActive }">
      <v-container fluid class="d-flex flex-column align-center justify-center fill-height">
        <v-card width="600" max-width="100%">
          <v-card-text class="d-flex justify-center">
            <v-date-picker
              show-adjacent-months
              v-if="props.type === 'date'"
              multiple="range"
              label="Filtro"
              v-model="filter"
            ></v-date-picker>
            <v-autocomplete
              v-if="props.type !== 'date'"
              clearable
              multiple
              :items="props.items"
              :item-title="props.itemTitle"
              :item-value="props.itemValue"
              label="Filtro"
              v-model="filter"
            ></v-autocomplete>
          </v-card-text>
          <v-card-actions class="justify-end">
            <v-btn text @click="isActive.value = false">Fechar</v-btn>
          </v-card-actions>
        </v-card>
      </v-container>
    </template>
  </v-dialog>
</template>

<script lang="ts" setup>
const props = defineProps({
  type: {
    type: String,
    required: false,
  },
  items: {
    type: Object,
    required: false,
  },
  itemTitle: {
    type: String,
    required: false,
  },
  itemValue: {
    type: String,
    required: false,
  },
});

const filter = defineModel();

const emit = defineEmits(["filter"]);
</script>

<style lang="scss" scoped></style>
