export default class RegisterDto {
  email = null // String
  name = null // String
  password = null // String

  constructor(obj) {
    if (obj) {
      this.email = obj.email
      this.name = obj.name
      this.password = obj.password
    }
  }
}