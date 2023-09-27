export default class ClientDto {
  id = null // Long
  banner = null // String
  name = null // String
  address = null // String
  postalCode = null // String
  fiscalNumber = null // Long
  email = null // String
  phone = null // String
  city = null; // String
  country = null; // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.banner = obj.banner
      this.name = obj.name
      this.fiscalNumber = obj.fiscalNumber
      this.email = obj.email
      this.phone = obj.phone
      this.address = obj.address
      this.postalCode = obj.postalCode
      this.city = obj.city
      this.country = obj.country
    }
  }
}
