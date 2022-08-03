export default class BrandDto {
  id = null // Long
  name = null // String
  images = null // List<ImageDto>

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.name = obj.name
      this.images = []

      // TODO: Check this syntax
      for (image in obj.images) {
        this.images.append(new ImageDto(image))
      }
    }
  }
}