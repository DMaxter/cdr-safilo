import { RequestType } from "@models/RequestType";

export enum Status {
  Ordered = "Encomendado",
  Done = "Finalizado",
  Cancelled = "Cancelado",
}

export const statusMap = {
  ORDERED: Status.Ordered,
  DONE: Status.Done,
  CANCELLED: Status.Cancelled,
};

export const typeMap = {
  OneFace: RequestType.OneFace,
  TwoFaces: RequestType.TwoFaces,
  SimpleShowcase: RequestType.SimpleShowcase,
  LeftShowcase: RequestType.LeftShowcase,
  RightShowcase: RequestType.RightShowcase,
};
