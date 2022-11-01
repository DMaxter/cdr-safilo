export default class ImageDto {
  id = null // Long
  obsolete = null // Boolean
  link = null // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.obsolete = obj.obsolete
      this.link = obj.link
    }
  }
}
