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
};
