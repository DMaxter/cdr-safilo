import { Image } from "@router/backend/services/image/types";

export class Brand {
  id: number | null = null;
  name: string = "";
  images: Image[] = [];
};
