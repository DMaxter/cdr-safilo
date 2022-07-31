export default class NewRequestDto {
  clientId = null // Long
  brandId = null // Long
  materialId = null // Long
  amount = null // Int
  measurements = null // Measurements
  application = null // Boolean
  images = null // RequestTypeDto

  constructor(obj) {
    if (obj) {
      this.clientId = obj.clientId
      this.brandId = obj.brandId
      this.materialId = obj.materialId
      this.amount = obj.amount
      this.measurements = new Measurements(obj.measurements)
      this.application = obj.application
      this.images = RequestTypeDto.from(obj.images)
    }
  }
}