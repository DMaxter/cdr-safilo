import BrandDto from "@models/BrandDto";
import MaterialDto from "@models/MaterialDto";
import Measurements from "@models/Measurements";
import { RequestTypeDto } from "@models/RequestTypeDto";

// Consider creating an enum for RequestStatus
type RequestStatus = string; // Replace with actual enum if available

export default class RequestDto {
  id: number | null = null;
  user: string | null = null;
  client: string | null = null;
  created: Date | null = null;
  lastUpdate: Date | null = null;
  trackingCode: string | null = null;
  status: RequestStatus | null = null;
  brand: BrandDto | null = null;
  application: boolean | null = null;
  type: RequestTypeDto | null = null;
  amount: number | null = null;
  cost: number | null = null;
  observations: string | null = null;

  constructor(obj?: Partial<RequestDto>) {
    if (obj) {
      this.id = obj.id ?? null;
      this.user = obj.user ?? null;
      this.client = obj.client ?? null;
      this.created = obj.created ? new Date(obj.created) : null;
      this.lastUpdate = obj.lastUpdate ? new Date(obj.lastUpdate) : null;
      this.trackingCode = obj.trackingCode ?? null;
      this.status = obj.status ?? null;
      this.brand = obj.brand ? new BrandDto(obj.brand) : null;
      this.application = obj.application ?? null;
      this.type = obj.type ? RequestTypeDto.from(obj.type) : null;
      this.amount = obj.amount ?? null;
      this.cost = obj.cost ?? null;
      this.observations = obj.observations ?? null;
    }
  }

  getMaterials(): String[] {
    return this.type.getMaterials();
  }

  getFinishings(): String[][] {
    return this.type.getFinishings();
  }

  getMeasurements(): number[][] {
    return this.type.getMeasurements();
  }
}
