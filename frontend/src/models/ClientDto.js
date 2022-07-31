export default class ClientDto {
  id = null // Long
  name = null // String
  address = null // String
  fiscalNumber = null // Long
  postalCode = null // String
  email = null // String
  phone = null // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.name = obj.name
      this.address = obj.address
      this.fiscalNumber = obj.fiscalNumber
      this.postalCode = obj.postalCode
      this.email = obj.email
      this.phone = obj.phone
    }
  }
}