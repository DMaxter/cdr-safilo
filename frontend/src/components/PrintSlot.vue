<template>
  <div class="bg-black h-[3px] w-full"></div>
  <p class="text-center font-bold text-xl">{{ props.name }}</p>
  <div class="flex justify-evenly">
    <img
      class="max-w-3/10 border-[2px] border-solid border-black mb-[5px]"
      :src="props.slotData.image?.link ?? ''"
      style="border: 2px solid black; margin-bottom: 5px"
    />
    <div class="flex flex-col items-start justify-center">
      <p><b>Material: </b>{{ props.slotData.material?.name ?? "" }}</p>
      <p><b>Acabamentos: </b></p>
      <ul v-if="props.slotData.finishings?.length" style="padding-left: 40px">
        <li v-for="finishing in props.slotData.finishings" :key="finishing.id">
          {{ finishing.name }}
        </li>
      </ul>
      <span v-else>Sem acabamentos</span>
      <p><b>Altura: </b>{{ props.slotData.measurements?.height }} cm</p>
      <p><b>Largura: </b>{{ props.slotData.measurements?.width }} cm</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { RequestSlot } from "@router/backend/services/request/types";

const props = defineProps<{
  slotData: RequestSlot;
  name: string;
}>();
</script>
