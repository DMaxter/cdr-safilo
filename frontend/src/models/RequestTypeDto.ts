import RequestSlotDto from "@models/RequestSlotDto";

export class RequestTypeDto {
  type: string | null = null;

  static from(obj: RequestTypeDto): RequestTypeDto {
    switch (obj.type) {
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
}

export class OneFace extends RequestTypeDto {
  cover: RequestSlotDto;

  constructor(obj: OneFace) {
    super();

    this.cover = new RequestSlotDto(obj.cover);
    this.type = "OneFace";
  }
}

export class TwoFaces extends RequestTypeDto {
  cover: RequestSlotDto;
  back: RequestSlotDto;

  constructor(obj: TwoFaces) {
    super();

    this.cover = new RequestSlotDto(obj.cover);
    this.back = new RequestSlotDto(obj.back);
    this.type = "TwoFaces";
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
  type = "LeftShowcase";

  constructor(obj: LeftShowcase) {
    super(obj);
  }
}

export class RightShowcase extends ShowcaseDto {
  type = "RightShowcase";

  constructor(obj: RightShowcase) {
    super(obj);
  }
}

export class SimpleShowcase extends ShowcaseDto {
  type = "SimpleShowcase";

  constructor(obj: SimpleShowcase) {
    super(obj);
  }
}
