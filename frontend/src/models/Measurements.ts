export default class Measurements {
  width: number | null = null;
  height: number | null = null;

  constructor(obj?: Partial<Measurements>) {
    if (obj) {
      this.width = obj.width ?? null;
      this.height = obj.height ?? null;
    }
  }
}
