export default class MaterialDto {
  id: number | null = null;
  name: string | null = null;
  cost: number | null = null;

  constructor(obj?: Partial<MaterialDto>) {
    if (obj) {
      this.id = obj.id ?? null;
      this.name = obj.name ?? null;
      this.cost = obj.cost ?? null;
    }
  }
}
