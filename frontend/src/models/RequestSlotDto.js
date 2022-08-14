export default class RequestSlotDto {
  brand = null // BrandDto
  image = null // ImageDto
  measurements = null // Measurements
  material = null // MaterialDto

  constructor(obj) {
    if (obj) {
      this.brand = new BrandDto(obj.brand)
      this.image = new ImageDto(obj.image)
      this.measurements = new Measurements(obj.measurements)
      this.material = new MaterialDto(obj.material)
    }
  }
}