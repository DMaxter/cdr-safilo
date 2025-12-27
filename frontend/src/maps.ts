import { Status } from "@router/backend/services/request/types";

export const statusItems = [
  { name: "Encomendado", value: Status.Ordered },
  { name: "Finalizado", value: Status.Done },
  { name: "Cancelado", value: Status.Cancelled },
];

export const requestTypes = [
  { name: "OneFace", value: "Uma face" },
  { name: "TwoFaces", value: "Duas faces" },
  { name: "SimpleShowcase", value: "Montra simples" },
  { name: "LeftShowcase", value: "Montra esquerda" },
  { name: "RightShowcase", value: "Montra direita" },
];
