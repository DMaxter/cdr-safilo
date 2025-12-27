import { Finishing } from "@router/backend/services/finishing/types";

export class FinishingGroup {
  id: number = 0;
  name: string = "";
  finishings: Finishing[] = [];

  constructor(obj?: FinishingGroup) {
    if (obj) {
      this.id = obj.id || null;
      this.name = obj.name || "";
      this.finishings = obj.finishings ? obj.finishings.map((f) => new Finishing(f)) : null;
    }
  }
}
