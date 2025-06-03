import ImageDto from "@models/ImageDto";

export default class BrandDto {
  id: number | null = null;
  name: string | null = null;
  images: ImageDto[] | null = null;

  constructor(obj?: Partial<BrandDto>) {
    if (obj) {
      this.id = obj.id ?? null;
      this.name = obj.name ?? null;
      this.images = [];

      // Fixed the loop syntax and method
      if (obj.images && Array.isArray(obj.images)) {
        for (const image of obj.images) {
          this.images.push(new ImageDto(image));
        }
      }
    }
  }
}
