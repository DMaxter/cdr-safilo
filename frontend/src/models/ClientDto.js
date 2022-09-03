import AddressDto from '@/models/AddressDto';

export default class ClientDto {
  id = null // Long
  name = null // String
  addresses = null // List<AddressDto>
  fiscalNumber = null // Long
  email = null // String
  phone = null // String

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.name = obj.name
      this.fiscalNumber = obj.fiscalNumber
      this.email = obj.email
      this.phone = obj.phone
      this.addresses = []

      obj.addresses.forEach(address => {
        this.addresses.append(new AddressDto(address))
      });
    }
  }
}