export default class AuthDto {
  email = null // String
  password = null // String

  constructor(obj) {
    if (obj) {
      this.email = obj.email
      this.password = obj.password
    }
  }
}