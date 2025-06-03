export default class ClientDto {
  id: number | string;
  banner: string | null = null;
  name: string;
  address: string;
  postalCode: string;
  fiscalNumber: string;
  email: string;
  phone: string;
  city: string;
  country: string;

  constructor(obj?: ClientDto) {
    if (obj) {
      this.id = obj.id;
      this.banner = obj.banner;
      this.name = obj.name;
      this.fiscalNumber = obj.fiscalNumber;
      this.email = obj.email;
      this.phone = obj.phone;
      this.address = obj.address;
      this.postalCode = obj.postalCode;
      this.city = obj.city;
      this.country = obj.country;
    }
  }

  clear() {
    this.id = "";
    this.banner = null;
    this.name = "";
    this.fiscalNumber = "";
    this.email = "";
    this.phone = "";
    this.address = "";
    this.postalCode = "";
    this.city = "";
    this.country = "";
  }
}
