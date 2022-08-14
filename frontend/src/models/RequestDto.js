import AddressDto from "./AddressDto"

export default class RequestDto {
  id = null // Long
  user = null // String
  client = null // String
  address = null // AddressDto
  created = null // Date
  lastUpdate = null // Date
  trackingCode = null // String
  status = null // RequestStatus (or String ??????)
  type = null // RequestTypeDto
  cost = null // Double
  observations = null // String
  application = null // Boolean

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.user = obj.user
      this.client = obj.client
      this.address = new AddressDto(obj.address)
      this.created = new Date(obj.created)
      this.lastUpdate = new Date(obj.lastUpdate)
      this.status = obj.status
      this.type = RequestTypeDto.from(obj.type)
      this.cost = obj.cost
      this.observations = obj.observations
      this.application = obj.application
    }
  }
}