export default class ImageDto {
  id: number | null = null;
  obsolete: boolean | null = null;
  link: string | null = null;

  constructor(obj?: Partial<ImageDto>) {
    if (obj) {
      this.id = obj.id ?? null;
      this.obsolete = obj.obsolete ?? null;
      this.link = obj.link ?? null;
    }
  }
}
