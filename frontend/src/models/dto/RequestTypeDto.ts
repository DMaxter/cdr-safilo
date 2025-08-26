import RequestSlotDto from "@models/dto/RequestSlotDto";
import { RequestType } from "@models/dto/RequestType";

export class RequestTypeDto {
  type: RequestType | null = null;

  static from(obj: RequestTypeDto): RequestTypeDto {
    switch (obj.type as string) {
      case "OneFace":
        return new OneFace(obj as OneFace);
      case "TwoFaces":
        return new TwoFaces(obj as TwoFaces);
      case "LeftShowcase":
        return new LeftShowcase(obj as LeftShowcase);
      case "RightShowcase":
        return new RightShowcase(obj as RightShowcase);
      case "SimpleShowcase":
        return new SimpleShowcase(obj as SimpleShowcase);
      default:
        throw new Error(`Unknown request type: ${obj.type}`);
    }
  }

  getFinishings?(): string[][];
  getMaterials?(): string[];
  getMeasurements?(): number[][];
}

export class OneFace extends RequestTypeDto {
  cover: RequestSlotDto;

  constructor(obj: OneFace) {
    super();

    this.cover = new RequestSlotDto(obj.cover);
    this.type = RequestType.OneFace;
  }

  getMaterials(): string[] {
    return [this.cover.getMaterial()];
  }

  getFinishings(): string[][] {
    return [this.cover.getFinishings()];
  }

  getMeasurements(): number[][] {
    return [this.cover.getMeasurements()];
  }
}

export class TwoFaces extends RequestTypeDto {
  cover: RequestSlotDto;
  back: RequestSlotDto;

  constructor(obj: TwoFaces) {
    super();

    this.cover = new RequestSlotDto(obj.cover);
    this.back = new RequestSlotDto(obj.back);
    this.type = RequestType.TwoFaces;
  }

  getMaterials(): string[] {
    return [this.cover.getMaterial(), this.back.getMaterial()];
  }

  getFinishings(): string[][] {
    return [this.cover.getFinishings(), this.back.getFinishings()];
  }

  getMeasurements(): number[][] {
    return [this.cover.getMeasurements(), this.back.getMeasurements()];
  }
}

export class ShowcaseDto extends RequestTypeDto {
  top: RequestSlotDto;
  bottom: RequestSlotDto;
  left: RequestSlotDto;
  right: RequestSlotDto;
  side: RequestSlotDto | null = null;

  constructor(obj: ShowcaseDto) {
    super();

    this.top = new RequestSlotDto(obj.top);
    this.bottom = new RequestSlotDto(obj.bottom);
    this.left = new RequestSlotDto(obj.left);
    this.right = new RequestSlotDto(obj.right);

    if (obj.side) {
      this.side = new RequestSlotDto(obj.side);
    }
  }
}

export class LeftShowcase extends ShowcaseDto {
  type = RequestType.LeftShowcase;

  constructor(obj: LeftShowcase) {
    super(obj);
  }

  getMaterials(): string[] {
    return [
      this.top.getMaterial(),
      this.bottom.getMaterial(),
      this.left.getMaterial(),
      this.right.getMaterial(),
      this.side!!.getMaterial(),
    ];
  }

  getFinishings(): string[][] {
    return [
      this.top.getFinishings(),
      this.bottom.getFinishings(),
      this.left.getFinishings(),
      this.right.getFinishings(),
      this.side!!.getFinishings(),
    ];
  }

  getMeasurements(): number[][] {
    return [
      this.top.getMeasurements(),
      this.bottom.getMeasurements(),
      this.left.getMeasurements(),
      this.right.getMeasurements(),
      this.side!!.getMeasurements(),
    ];
  }
}

export class RightShowcase extends ShowcaseDto {
  type = RequestType.RightShowcase;

  constructor(obj: RightShowcase) {
    super(obj);
  }

  getMaterials(): string[] {
    return [
      this.top.getMaterial(),
      this.bottom.getMaterial(),
      this.left.getMaterial(),
      this.right.getMaterial(),
      this.side!!.getMaterial(),
    ];
  }

  getFinishings(): string[][] {
    return [
      this.top.getFinishings(),
      this.bottom.getFinishings(),
      this.left.getFinishings(),
      this.right.getFinishings(),
      this.side!!.getFinishings(),
    ];
  }

  getMeasurements(): number[][] {
    return [
      this.top.getMeasurements(),
      this.bottom.getMeasurements(),
      this.left.getMeasurements(),
      this.right.getMeasurements(),
      this.side!!.getMeasurements(),
    ];
  }
}

export class SimpleShowcase extends ShowcaseDto {
  type = RequestType.SimpleShowcase;

  constructor(obj: SimpleShowcase) {
    super(obj);
  }

  getMaterials(): string[] {
    return [
      this.top.getMaterial(),
      this.bottom.getMaterial(),
      this.left.getMaterial(),
      this.right.getMaterial(),
    ];
  }

  getFinishings(): string[][] {
    return [
      this.top.getFinishings(),
      this.bottom.getFinishings(),
      this.left.getFinishings(),
      this.right.getFinishings(),
    ];
  }

  getMeasurements(): number[][] {
    return [
      this.top.getMeasurements(),
      this.bottom.getMeasurements(),
      this.left.getMeasurements(),
      this.right.getMeasurements(),
    ];
  }
}
