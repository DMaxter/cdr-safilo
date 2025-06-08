import FinishingDto from "@models/FinishingDto";
import ImageDto from "@models/ImageDto";
import MaterialDto from "@models/MaterialDto";
import Measurements from "@models/Measurements";

export default class RequestSlotDto {
  image: ImageDto;
  measurements: Measurements;
  material: MaterialDto;
  finishings: Array<FinishingDto>;

  constructor(obj: RequestSlotDto) {
    this.image = new ImageDto(obj.image);
    this.measurements = new Measurements(obj.measurements);
    this.material = new MaterialDto(obj.material);
    this.finishings = obj.finishings.map((f) => new FinishingDto(f));
  }

  getMaterial(): String {
    return this.material.name;
  }

  getFinishings(): String[] {
    return this.finishings.map((f) => f.name);
  }

  getMeasurements(): number[] {
    return [this.measurements.width, this.measurements.height];
  }
}
