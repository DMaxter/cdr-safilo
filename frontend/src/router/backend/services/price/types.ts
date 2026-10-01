import { Finishing } from "@router/backend/services/finishing/types";

export class Price {
  id: number = 0;
  material: number = 0;
  finishings: Finishing[] = [];
  costPerSquareMeter: number = 0;
  fixedCost: number = 0;

  constructor(obj?: Price) {
    if (obj) {
      this.id = obj.id || 0;
      this.material = obj.material || 0;
      this.finishings = obj.finishings ? obj.finishings.map((f) => new Finishing(f)) : [];
      this.costPerSquareMeter = obj.costPerSquareMeter || 0;
      this.fixedCost = obj.fixedCost || 0;
    }
  }
}
