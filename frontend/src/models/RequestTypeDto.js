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
  top = null // ImageDto
  bottom = null // ImageDto
  left = null // ImageDto
  right = null // ImageDto
  side = null // ImageDto

  constructor(obj) {
    if (obj) {
      this.top = obj.top
      this.bottom = obj.bottom
      this.left = obj.left
      this.right = obj.right
      this.side = obj.side
    }
  }
}

export class OneFace extends RequestTypeDto {
  type = "OneFace"
  cover = null // ImageDto

  constructor(obj) {
    if (obj) {
      this.cover = obj.cover
    }
  }
}

export class TwoFaces extends RequestTypeDto {
  type = "TwoFaces"
  cover = null // ImageDto
  back = null // ImageDto

  constructor(obj) {
    if (obj) {
      this.cover = obj.cover
      this.back = obj.back
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