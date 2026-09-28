<template>
  <div class="border rounded p-4">
    <div class="flex items-center gap-2 mb-3">
      <img
        v-if="draft.image"
        :src="draft.image.link"
        alt="Imagem selecionada"
        class="h-12 w-12 sm:h-14 sm:w-14 shrink-0 object-contain border rounded bg-gray-50"
      />
      <div
        v-else
        class="h-12 w-12 sm:h-14 sm:w-14 shrink-0 flex items-center justify-center border-2 border-dashed rounded bg-gray-50 text-gray-400"
      >
        <Icon icon="image" />
      </div>
      <P-Button
        :label="draft.image ? 'Alterar' : 'Selecionar'"
        size="small"
        :disabled="!brand"
        class="shrink-0"
        @click="imagePickerOpen = true"
        ><template #icon><Icon icon="image" /></template
      ></P-Button>
      <P-Button
        v-if="draft.image"
        icon="close"
        size="small"
        text
        severity="danger"
        class="shrink-0"
        @click="draft.image = null"
      />
      <span class="font-semibold min-w-0 truncate ml-auto">{{ props.slotLabel }}</span>
    </div>

    <div class="grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-4">
      <div class="col-span-2">
        <P-FloatLabel variant="on" class="w-full">
          <P-Select
            :modelValue="draft.material"
            :options="materials"
            optionLabel="name"
            fluid
            :invalid="!draft.material"
            @update:modelValue="onMaterialSelect"
          />
          <label>Material</label>
        </P-FloatLabel>
      </div>

      <div>
        <P-FloatLabel variant="on" class="w-full">
          <P-InputNumber v-model="draft.height" :min="0" fluid />
          <label>Altura (cm)</label>
        </P-FloatLabel>
      </div>

      <div>
        <P-FloatLabel variant="on" class="w-full">
          <P-InputNumber v-model="draft.width" :min="0" fluid />
          <label>Largura (cm)</label>
        </P-FloatLabel>
      </div>

      <div v-for="group in mandatoryGroups" :key="group.id" class="col-span-2 flex items-end gap-2">
        <P-FloatLabel variant="on" class="w-full">
          <P-Select
            :modelValue="groupSelection(group)"
            :options="group.finishings"
            optionLabel="name"
            placeholder="Selecione"
            fluid
            @update:modelValue="setGroupFinishing(group, $event)"
          />
          <label>{{ group.name }}</label>
        </P-FloatLabel>
        <Icon
          v-if="groupSelection(group)"
          icon="check_circle"
          class="text-green-600 shrink-0 mb-2.5"
        />
      </div>

      <div v-if="additionalFinishings.length" class="col-span-2">
        <P-FloatLabel variant="on" class="w-full">
          <P-MultiSelect
            v-model="selectedAdditional"
            :options="additionalFinishings"
            optionLabel="name"
            placeholder="Selecione"
            fluid
          >
            <template #option="{ option }">{{ option.name }}</template>
          </P-MultiSelect>
          <label>Acabamentos adicionais (opcional)</label>
        </P-FloatLabel>
      </div>
    </div>

    <p v-if="unfilledGroupNames.length" class="text-xs text-amber-700 mt-2">
      Falta escolher (um por grupo): {{ unfilledGroupNames.join(", ") }}
    </p>

    <ImagePickerDialog v-model="imagePickerOpen" :images="brandImages" @select="onImageSelect" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";

import type { Brand } from "@router/backend/services/brand/types";
import type { Finishing } from "@router/backend/services/finishing/types";
import type { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import type { Image } from "@router/backend/services/image/types";
import type { Material } from "@router/backend/services/material/types";
import { useOrderWizardStore, type DraftSlot } from "@stores/orderWizard";
import { useFinishingGroupStore } from "@stores/finishingGroups";
import { useFinishingStore } from "@stores/finishings";

const props = defineProps<{
  slotKey: string;
  slotLabel: string;
  draft: DraftSlot;
  materials: Material[];
  brand: Brand | null;
}>();

const wizard = useOrderWizardStore();
const groupStore = useFinishingGroupStore();
const finishingStore = useFinishingStore();

const imagePickerOpen = ref(false);

const brandImages = computed(() => {
  if (!props.brand) return [];
  return props.brand.images.filter((img) => !img.obsolete);
});

const mandatoryGroups = computed<FinishingGroup[]>(() => {
  if (!props.draft.material) return [];

  return props.draft.material.mandatoryFinishings
    .map((group) => {
      const fetched = groupStore.finishingGroups.find((g) => g.id === group.id);
      const resolved = fetched ?? group;
      return {
        ...resolved,
        finishings: resolved.finishings.filter((f) => !f.obsolete),
      };
    })
    .filter((group) => group.finishings.length > 0);
});

const additionalFinishings = computed<Finishing[]>(() => {
  if (!props.draft.material) return [];

  return props.draft.material.additionalFinishings
    .map((f) => finishingStore.finishings.find((candidate) => candidate.id === f.id) ?? f)
    .filter((f) => !f.obsolete);
});

const additionalIds = computed(() => {
  const set = new Set<number>();
  for (const f of additionalFinishings.value) set.add(f.id);
  return set;
});

const selectedByGroup = computed(() => {
  const map = new Map<number, Finishing>();
  for (const group of mandatoryGroups.value) {
    const match = props.draft.finishings.find((f) => group.finishings.some((gf) => gf.id === f.id));
    if (match) map.set(group.id, match);
  }
  return map;
});

function groupSelection(group: FinishingGroup): Finishing | null {
  return selectedByGroup.value.get(group.id) ?? null;
}

const duplicateSelections = computed(() => {
  const byFinishing = new Map<number, number[]>();
  for (const [groupId, finishing] of selectedByGroup.value) {
    const list = byFinishing.get(finishing.id) ?? [];
    list.push(groupId);
    byFinishing.set(finishing.id, list);
  }

  const duplicated = new Set<number>();
  for (const groupIds of byFinishing.values()) {
    if (groupIds.length > 1) for (const id of groupIds) duplicated.add(id);
  }
  return duplicated;
});

function setGroupFinishing(group: FinishingGroup, finishing: Finishing | null) {
  const ids = new Set(group.finishings.map((f) => f.id));
  const kept = props.draft.finishings.filter((f) => !ids.has(f.id));
  props.draft.finishings = finishing ? [...kept, finishing] : kept;
}

const unfilledGroupNames = computed(() =>
  mandatoryGroups.value
    .filter((g) => !selectedByGroup.value.has(g.id) || duplicateSelections.value.has(g.id))
    .map((g) => g.name),
);

const selectedAdditional = computed<Finishing[]>({
  get: () => props.draft.finishings.filter((f) => additionalIds.value.has(f.id)),
  set: (val) => {
    const mandatory = props.draft.finishings.filter((f) => !additionalIds.value.has(f.id));
    props.draft.finishings = [...val, ...mandatory];
  },
});

function onImageSelect(image: Image) {
  props.draft.image = image;
}

function onMaterialSelect(material: Material | null) {
  wizard.setSlotMaterial(props.slotKey, material);
}
</script>
