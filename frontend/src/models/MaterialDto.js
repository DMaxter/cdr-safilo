export default class MaterialDto {
  id = null // Long
  name = null // String
  obsolete = null // Boolean
  cost = null // Double

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.name = obj.name
      this.obsolete = obj.obsolete
      this.cost = obj.cost
    }
  }
}