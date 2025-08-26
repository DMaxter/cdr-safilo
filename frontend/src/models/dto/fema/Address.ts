export default class Address {
  postalCode: string = "";
  country: string = "";
  city: string = "";
  address: string = "";

  constructor(obj?: Partial<Address>) {
    if (obj) {
      this.postalCode = obj.postalCode ?? "";
      this.country = obj.country ?? "";
      this.city = obj.city ?? "";
      this.address = obj.address ?? "";
    }
  }
}
