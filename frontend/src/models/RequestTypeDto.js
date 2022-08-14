export class RequestTypeDto {
  type = null // String

  from(obj) {
    switch (obj.type) {
      case "OneFace": return new OneFace(obj)
      case "TwoFaces": return new TwoFaces(obj)
      case "LeftShowcase": return new LeftShowcase(obj)
      case "RightShowcase": return new RightShowcase(obj)
    }
  }
}

export class ShowcaseDto extends RequestTypeDto {
  top = null // RequestSlotDto
  bottom = null // RequestSlotDto
  left = null // RequestSlotDto
  right = null // RequestSlotDto
  side = null // RequestSlotDto

  constructor(obj) {
    if (obj) {
      this.top = new RequestSlotDto(obj.top)
      this.bottom = new RequestSlotDto(obj.bottom)
      this.left = new RequestSlotDto(obj.left)
      this.right = new RequestSlotDto(obj.right)
      this.side = new RequestSlotDto(obj.side)
    }
  }
}

export class OneFace extends RequestTypeDto {
  type = "OneFace"
  cover = null // RequestSlotDto

  constructor(obj) {
    if (obj) {
      this.cover = new RequestSlotDto(obj.cover)
    }
  }
}

export class TwoFaces extends RequestTypeDto {
  type = "TwoFaces"
  cover = null // RequestSlotDto
  back = null // RequestSlotDto

  constructor(obj) {
    if (obj) {
      this.cover = new RequestSlotDto(obj.cover)
      this.back = new RequestSlotDto(obj.back)
    }
  }
}

export class LeftShowcase extends ShowcaseDto {
  type = "LeftShowcase"

  constructor(obj) {
    if (obj) {
      super(obj)
    }
  }
}

export class RightShowcase extends ShowcaseDto {
  type = "RightShowcase"

  constructor(obj) {
    if (obj) {
      super(obj)
    }
  }
}