import { Status } from "@models/dto/RequestStatus";

export const statusItems = [
  { name: "Encomendado", value: Status.Ordered },
  { name: "Finalizado", value: Status.Done },
  { name: "Cancelado", value: Status.Cancelled },
];
