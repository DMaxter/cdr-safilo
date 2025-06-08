<template>
  <div class="bg-black" style="height: 3x; width: 100%"></div>
  <p class="text-h6" align="center">{{ props.name }}</p>
  <div class="d-flex justify-space-evenly">
    <v-img
      :src="props.slot.image.link"
      max-width="30%"
      style="border: 2px solid black; margin-bottom: 5px"
    ></v-img>
    <div class="d-flex flex-column align-start justify-center">
      <p><b>Material: </b>{{ props.slot.material.name }}</p>
      <p><b>Acabamentos: </b></p>
      <ul
        v-if="props.slot.finishings.length != 0"
        v-for="finishing in props.slot.finishings"
        style="padding-left: 40px"
      >
        <li>{{ finishing.name }}</li>
      </ul>
      <span v-else>Sem acabamentos</span>
      <p><b>Altura: </b>{{ props.slot.measurements.height }} cm</p>
      <p><b>Largura: </b>{{ props.slot.measurements.width }} cm</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import RequestSlot from "@models/RequestSlotDto";

const props = defineProps({
  slot: {
    type: RequestSlot,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
});
</script>
