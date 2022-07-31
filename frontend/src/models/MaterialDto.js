export default class MaterialDto {
  id = null // Long
  name = null // String
  cost = null // Double

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.name = obj.name
      this.cost = obj.cost
    }
  }
}