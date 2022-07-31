export default class Measurements {
  width = null // Double
  height = null // Double

  constructor(obj) {
    if (obj) {
      this.width = obj.width
      this.height = obj.height
    }
  }
}