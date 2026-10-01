import type { Material } from "@router/backend/services/material/types";

export class Finishing {
  id: number = 0;
  name: string = "";
  obsolete: boolean = false;

  constructor(obj?: Finishing) {
    if (obj) {
      this.id = obj.id || 0;
      this.name = obj.name || "";
      this.obsolete = obj.obsolete || false;
    }
  }
}

export class NewFinishing {
  name: string = "";
  cost: number = 0;
  materials: Material[] = [];

  constructor(obj?: Partial<NewFinishing>) {
    if (obj) {
      this.name = obj.name ?? "";
      this.cost = obj.cost ?? 0;
      this.materials = obj.materials ?? [];
    }
  }
}
