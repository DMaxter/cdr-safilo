import Address from "@models/fema/Address";

export default class Contact {
  name: string = "";
  address: Address = new Address();
  phone: string = "";

  constructor(obj?: Partial<Contact>) {
    if (obj) {
      this.name = obj.name ?? "";
      this.address = obj.address ? new Address(obj.address) : new Address();
      this.phone = obj.phone ?? "";
    }
  }
}
