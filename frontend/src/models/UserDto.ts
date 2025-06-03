class PlafondDetail {
  brand: string;
  amount: number;

  constructor(obj: PlafondDetail) {
    this.brand = obj.brand;
    this.amount = obj.amount;
  }
}

export default class UserDto {
  email: string | null = null;
  name: string | null = null;
  credits: Array<PlafondDetail> | null = null;
  roles: Array<string> | null = null;

  constructor(obj?: UserDto) {
    if (obj) {
      this.email = obj.email!!;
      this.name = obj.name!!;
      this.credits = obj.credits!!.map((element) => new PlafondDetail(element));
      this.roles = obj.roles!!;
    }
  }
}
