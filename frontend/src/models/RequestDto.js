export default class RequestDto {
  id = null // Long
  user = null // String
  client = null // String
  created = null // Date
  lastUpdate = null // Date
  trackingCode = null // String
  status = null // RequestStatus
  brand = null // BrandDto
  material = null // MaterialDto
  measurements = null // Measurements
  application = null // Boolean
  images = null // RequestTypeDto
  amount = null // Int

  constructor(obj) {
    if (obj) {
      this.id = obj.id
      this.user = obj.user
      this.client = obj.client
      this.created = new Date(obj.created)
      this.lastUpdate = new Date(obj.lastUpdate)
      this.status = obj.status
      this.brand = new BrandDto(obj.brand)
      this.material = new MaterialDto(obj.material)
      this.measurement = new Measurements(obj.measurement)
      this.application = obj.application
      this.images = RequestTypeDto.from(obj.images)
      this.amount = obj.amount
    }
  }
}
