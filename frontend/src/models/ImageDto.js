export default class ImageDto {
  id = null // Long
  link = null // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.link = obj.link
    }
  }
}