import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { Finishing } from "@router/backend/services/finishing/types";

export class Material {
  id: number = 0;
  name: string = "";
  obsolete: boolean = false;
  mandatoryFinishings: FinishingGroup[] = [];
  additionalFinishings: Finishing[] = [];

  constructor(obj?: Material) {
    if (obj) {
      this.id = obj.id || 0;
      this.name = obj.name || "";
      this.obsolete = obj.obsolete || false;
      this.mandatoryFinishings = obj.mandatoryFinishings
        ? obj.mandatoryFinishings.map((fg) => new FinishingGroup(fg))
        : [];
      this.additionalFinishings = obj.additionalFinishings
        ? obj.additionalFinishings.map((f) => new Finishing(f))
        : [];
    }
  }
}
