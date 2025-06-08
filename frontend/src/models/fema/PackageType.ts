export default class PackageType {
  id: string | null = null;
  name: string | null = null;

  constructor(obj?: Partial<PackageType>) {
    if (obj) {
      this.id = obj.id ?? null;
      this.name = obj.name ?? null;
    }
  }
}
