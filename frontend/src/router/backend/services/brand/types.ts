import { Image } from "@router/backend/services/image/types";

export class Brand {
  id: number = 0;
  name: string = "";
  images: Image[] = [];

  constructor(obj?: Brand) {
    if (obj) {
      this.id = obj.id || 0;
      this.name = obj.name || "";
      this.images = obj.images ? obj.images.map((i) => new Image(i)) : [];
    }
  }
}
