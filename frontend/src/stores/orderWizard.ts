import { defineStore } from "pinia";
import { computed, ref } from "vue";

import {
  Request,
  type OneFace,
  type TwoFaces,
  type Showcase,
  type RequestType,
  RequestSlot,
  Measurements,
  OneFace as OneFaceClass,
  TwoFaces as TwoFacesClass,
  SimpleShowcase,
  LeftShowcase,
  RightShowcase,
} from "@router/backend/services/request/types";
import type { Client } from "@router/backend/services/client/types";
import type { Brand } from "@router/backend/services/brand/types";
import type { Image } from "@router/backend/services/image/types";
import type { Material } from "@router/backend/services/material/types";
import type { Finishing } from "@router/backend/services/finishing/types";
import type { FinishingGroup } from "@router/backend/services/finishingGroup/types";

export interface DraftSlot {
  image: Image | null;
  material: Material | null;
  finishings: Finishing[];
  width: number;
  height: number;
}

export interface SlotConfig {
  key: string;
  label: string;
}

export type RequestTypeName =
  | "OneFace"
  | "TwoFaces"
  | "SimpleShowcase"
  | "LeftShowcase"
  | "RightShowcase";

export const ORDER_TYPE_CONFIG: Record<RequestTypeName, SlotConfig[]> = {
  OneFace: [{ key: "cover", label: "Frente" }],
  TwoFaces: [
    { key: "cover", label: "Frente" },
    { key: "back", label: "Verso" },
  ],
  SimpleShowcase: [
    { key: "top", label: "Topo" },
    { key: "bottom", label: "Fundo" },
    { key: "left", label: "Esquerda" },
    { key: "right", label: "Direita" },
  ],
  LeftShowcase: [
    { key: "top", label: "Topo" },
    { key: "bottom", label: "Fundo" },
    { key: "left", label: "Esquerda" },
    { key: "right", label: "Direita" },
    { key: "side", label: "Lateral" },
  ],
  RightShowcase: [
    { key: "top", label: "Topo" },
    { key: "bottom", label: "Fundo" },
    { key: "left", label: "Esquerda" },
    { key: "right", label: "Direita" },
    { key: "side", label: "Lateral" },
  ],
};

const REQUEST_TYPE_DISPLAY: Record<RequestTypeName, string> = {
  OneFace: "Uma face",
  TwoFaces: "Duas faces",
  SimpleShowcase: "Montra simples",
  LeftShowcase: "Montra esquerda",
  RightShowcase: "Montra direita",
};

function createEmptyDraftSlot(): DraftSlot {
  return {
    image: null,
    material: null,
    finishings: [],
    width: 0,
    height: 0,
  };
}

export function mandatoryGroupIssues(slot: DraftSlot | null | undefined): FinishingGroup[] {
  if (!slot?.material) return [];

  const remaining = new Set(slot.finishings.map((f) => f.id));
  const issues: FinishingGroup[] = [];

  for (const group of slot.material.mandatoryFinishings) {
    if (group.finishings.length === 0) continue;

    const match = group.finishings.find((f) => remaining.has(f.id));
    if (match) {
      remaining.delete(match.id);
    } else {
      issues.push(group);
    }
  }

  return issues;
}

export const useOrderWizardStore = defineStore("orderWizard", () => {
  const step = ref(1);
  const clientId = ref<number | null>(null);
  const client = ref<Client | null>(null);
  const type = ref<RequestTypeName | null>(null);
  const brand = ref<Brand | null>(null);
  const slots = ref<Record<string, DraftSlot>>({});
  const amount = ref(1);
  const application = ref(false);
  const observations = ref("");
  const editingId = ref<number | null>(null);
  const submitting = ref(false);

  const slotConfigs = computed<SlotConfig[]>(() => {
    if (!type.value) return [];
    return ORDER_TYPE_CONFIG[type.value];
  });

  const typeName = computed(() => {
    if (!type.value) return "";
    return REQUEST_TYPE_DISPLAY[type.value];
  });

  const isShowcase = computed(() => {
    return (
      type.value === "SimpleShowcase" ||
      type.value === "LeftShowcase" ||
      type.value === "RightShowcase"
    );
  });

  const isEditing = computed(() => editingId.value !== null);

  function reset() {
    step.value = 1;
    clientId.value = null;
    client.value = null;
    type.value = null;
    brand.value = null;
    slots.value = {};
    amount.value = 1;
    application.value = false;
    observations.value = "";
    editingId.value = null;
    submitting.value = false;
  }

  function initSlots() {
    const newSlots: Record<string, DraftSlot> = {};
    for (const config of slotConfigs.value) {
      if (slots.value[config.key]) {
        newSlots[config.key] = slots.value[config.key];
      } else {
        newSlots[config.key] = createEmptyDraftSlot();
      }
    }
    slots.value = newSlots;
  }

  function initFromRequest(request: Request) {
    reset();
    editingId.value = request.id;
    clientId.value = request.client?.id as number;
    client.value = request.client;
    brand.value = request.brand;
    amount.value = request.amount || 1;
    application.value = request.application;
    observations.value = request.observations;

    if (request.type) {
      type.value = request.type.type as RequestTypeName;
      initSlots();

      if (request.type.type === "OneFace") {
        const t = request.type as OneFace;
        if (t.cover) hydrateSlot("cover", t.cover);
      } else if (request.type.type === "TwoFaces") {
        const t = request.type as TwoFaces;
        if (t.cover) hydrateSlot("cover", t.cover);
        if (t.back) hydrateSlot("back", t.back);
      } else {
        const t = request.type as Showcase;
        if (t.top) hydrateSlot("top", t.top);
        if (t.bottom) hydrateSlot("bottom", t.bottom);
        if (t.left) hydrateSlot("left", t.left);
        if (t.right) hydrateSlot("right", t.right);
        if (t.side) hydrateSlot("side", t.side);
      }
    }
  }

  function hydrateSlot(key: string, slot: RequestSlot) {
    if (!slots.value[key]) return;
    slots.value[key] = {
      image: slot.image,
      material: slot.material,
      finishings: [...slot.finishings],
      width: slot.measurements?.width ?? 0,
      height: slot.measurements?.height ?? 0,
    };
  }

  function buildRequestType(): RequestType {
    if (!type.value) {
      return new OneFaceClass();
    }

    switch (type.value) {
      case "OneFace": {
        const of = new OneFaceClass();
        of.cover = buildSlot("cover");
        return of;
      }
      case "TwoFaces": {
        const tf = new TwoFacesClass();
        tf.cover = buildSlot("cover");
        tf.back = buildSlot("back");
        return tf;
      }
      case "SimpleShowcase": {
        const ss = new SimpleShowcase();
        ss.top = buildSlot("top");
        ss.bottom = buildSlot("bottom");
        ss.left = buildSlot("left");
        ss.right = buildSlot("right");
        return ss;
      }
      case "LeftShowcase": {
        const ls = new LeftShowcase();
        ls.top = buildSlot("top");
        ls.bottom = buildSlot("bottom");
        ls.left = buildSlot("left");
        ls.right = buildSlot("right");
        ls.side = buildSlot("side");
        return ls;
      }
      case "RightShowcase": {
        const rs = new RightShowcase();
        rs.top = buildSlot("top");
        rs.bottom = buildSlot("bottom");
        rs.left = buildSlot("left");
        rs.right = buildSlot("right");
        rs.side = buildSlot("side");
        return rs;
      }
      default:
        return new OneFaceClass();
    }
  }

  function buildSlot(key: string): RequestSlot | null {
    const draft = slots.value[key];
    if (!draft || !draft.material) return null;

    const slot = new RequestSlot();
    slot.image = draft.image;
    slot.material = draft.material;
    slot.finishings = [...draft.finishings];
    slot.measurements = new Measurements();
    slot.measurements.width = draft.width;
    slot.measurements.height = draft.height;
    return slot;
  }

  function buildRequest(): Request {
    const request = new Request();
    request.id = editingId.value ?? 0;
    request.client = client.value;
    request.brand = brand.value;
    request.type = buildRequestType();
    request.amount = amount.value;
    request.application = application.value;
    request.observations = observations.value;
    return request;
  }

  function setClient(c: Client) {
    clientId.value = c.id as number;
    client.value = c;
  }

  function clearClient() {
    clientId.value = null;
    client.value = null;
  }

  function setType(t: RequestTypeName) {
    type.value = t;
    initSlots();
  }

  function setBrand(b: Brand) {
    brand.value = b;
  }

  function setSlotMaterial(key: string, material: Material | null) {
    const slot = slots.value[key];
    if (!slot) return;

    slot.material = material;

    if (!material) {
      slot.finishings = [];
      return;
    }

    const validIds = new Set<number>();
    for (const group of material.mandatoryFinishings) {
      for (const f of group.finishings) validIds.add(f.id);
    }
    for (const f of material.additionalFinishings) validIds.add(f.id);

    slot.finishings = slot.finishings.filter((f) => validIds.has(f.id));
  }

  return {
    step,
    clientId,
    client,
    type,
    brand,
    slots,
    amount,
    application,
    observations,
    editingId,
    submitting,
    slotConfigs,
    typeName,
    isShowcase,
    isEditing,
    reset,
    initSlots,
    initFromRequest,
    buildRequest,
    setClient,
    clearClient,
    setType,
    setBrand,
    setSlotMaterial,
  };
});
