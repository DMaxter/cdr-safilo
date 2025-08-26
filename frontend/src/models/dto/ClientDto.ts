import ImageDto from "@models/dto/ImageDto";

export default class ClientDto {
  id: number | undefined = undefined;
  banner: string | null = null;
  name: string;
  address: string;
  postalCode: string;
  fiscalNumber: string;
  email: string;
  phone: string;
  city: string;
  country: string;
  note: string | null = null;
  images: ImageDto[] = [];

  constructor(obj?: Partial<ClientDto>) {
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
      this.images = obj.images.map((i) => new ImageDto(i));
    }
  }

  clear() {
    this.id = undefined;
    this.banner = null;
    this.name = "";
    this.fiscalNumber = "";
    this.email = "";
    this.phone = "";
    this.address = "";
    this.postalCode = "";
    this.city = "";
    this.country = "";
    this.images = [];
  }
}
