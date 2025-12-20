import { Status } from "@router/backend/services/request/types";

export const statusItems = [
  { name: "Encomendado", value: Status.Ordered },
  { name: "Finalizado", value: Status.Done },
  { name: "Cancelado", value: Status.Cancelled },
];
