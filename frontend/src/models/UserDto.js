export default class UserDto {
  email = null // String
  name = null // String
  credits = null // Double

  constructor(obj) {
    if (obj) {
      this.email = obj.email
      this.name = obj.name
      this.credits = obj.credits
    }
  }
}