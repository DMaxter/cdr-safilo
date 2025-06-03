import BrandDto from "./BrandDto";
import MaterialDto from "./MaterialDto";
import Measurements from "./Measurements";
import { RequestTypeDto } from "./RequestTypeDto";

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
  material: MaterialDto | null = null;
  measurements: Measurements | null = null;
  application: boolean | null = null;
  images: RequestTypeDto | null = null;
  amount: number | null = null;

  constructor(
    obj?: Partial<
      Omit<RequestDto, "created" | "lastUpdate" | "brand" | "material" | "measurements" | "images">
    > & {
      created?: string | Date;
      lastUpdate?: string | Date;
      brand?: any;
      material?: any;
      measurement?: any; // Note: there's a typo in original code (measurement vs measurements)
      images?: any;
    },
  ) {
    if (obj) {
      this.id = obj.id ?? null;
      this.user = obj.user ?? null;
      this.client = obj.client ?? null;
      this.created = obj.created ? new Date(obj.created) : null;
      this.lastUpdate = obj.lastUpdate ? new Date(obj.lastUpdate) : null;
      this.trackingCode = obj.trackingCode ?? null;
      this.status = obj.status ?? null;
      this.brand = obj.brand ? new BrandDto(obj.brand) : null;
      this.material = obj.material ? new MaterialDto(obj.material) : null;
      this.measurements = obj.measurement ? new Measurements(obj.measurement) : null; // Fixed typo
      this.application = obj.application ?? null;
      this.images = obj.images ? RequestTypeDto.from(obj.images) : null;
      this.amount = obj.amount ?? null;
    }
  }
}
