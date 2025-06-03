import Measurements from "@models/Measurements";
import { RequestTypeDto } from "@models/RequestTypeDto";

export default class NewRequestDto {
  clientId: number | null = null;
  brandId: number | null = null;
  materialId: number | null = null;
  amount: number | null = null;
  measurements: Measurements | null = null;
  application: boolean | null = null;
  requestType: RequestTypeDto | null = null;

  constructor(obj?: NewRequestDto) {
    if (obj) {
      this.clientId = obj.clientId;
      this.brandId = obj.brandId;
      this.materialId = obj.materialId;
      this.amount = obj.amount;
      this.measurements = obj.measurements ? new Measurements(obj.measurements) : null;
      this.application = obj.application;

      if (obj.requestType) {
        this.requestType = RequestTypeDto.from(obj.requestType);
      }
    }
  }
}
