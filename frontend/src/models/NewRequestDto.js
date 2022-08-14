export default class NewRequestDto {
  clientId = null // Long
  amount = null // Int
  application = null // Boolean
  addressId = null // Long
  observations = null // String
  type = null // RequestTypeDto

  constructor(obj) {
    if (obj) {
      this.clientId = obj.clientId
      this.amount = obj.amount
      this.application = obj.application
      this.addressId = obj.addressId
      this.observations = obj.observations
      this.type = RequestTypeDto.from(obj.type)
    }
  }
}