export default class AddressDto {
  id = null // Long
  address = null // String
  postalCode = null // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.address = obj.address
      this.postalCode = obj.postalCode
    }
  }
}