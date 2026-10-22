import { Image } from "@router/backend/services/image/types";

export class Client {
  id: number | string = "";
  banner: string = "";
  name: string = "";
  fiscalNumber: string = "";
  email: string = "";
  phone: string = "";
  address: string = "";
  postalCode: string = "";
  city: string = "";
  country: string = "";
  note: string = "";
  images: Image[] = [];

  constructor(obj?: Client) {
    if (obj) {
      this.id = obj.id || "";
      this.banner = obj.banner || "";
      this.name = obj.name || "";
      this.fiscalNumber = obj.fiscalNumber || "";
      this.email = obj.email || "";
      this.phone = obj.phone || "";
      this.address = obj.address || "";
      this.postalCode = obj.postalCode || "";
      this.city = obj.city || "";
      this.country = obj.country || "";
      this.note = obj.note || "";
      this.images = obj.images ? obj.images.map((image) => new Image(image)) : [];
    }
  }
}

export type ClientUpdatePayload = Pick<
  Client,
  "id" | "name" | "fiscalNumber" | "email" | "phone" | "address" | "postalCode" | "city" | "country"
>;

export function toClientUpdatePayload(client: Client): ClientUpdatePayload {
  return {
    id: client.id,
    name: client.name,
    fiscalNumber: client.fiscalNumber,
    email: client.email,
    phone: client.phone,
    address: client.address,
    postalCode: client.postalCode,
    city: client.city,
    country: client.country,
  };
}
