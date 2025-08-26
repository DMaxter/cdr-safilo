import FinishingDto from "@models/dto/FinishingDto";
import ImageDto from "@models/dto/ImageDto";
import MaterialDto from "@models/dto/MaterialDto";
import Measurements from "@models/dto/Measurements";

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

  getMaterial(): string {
    return this.material.name;
  }

  getFinishings(): string[] {
    return this.finishings.map((f) => f.name);
  }

  getMeasurements(): number[] {
    return [this.measurements.width, this.measurements.height];
  }
}
