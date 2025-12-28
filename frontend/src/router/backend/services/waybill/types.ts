export class Waybill {
  reference: string = "";
  service: Service | null = null;
  source: Contact | null = null;
  destination: Contact = new Contact();
  items: number = 0;
  packaging: PackageType | null = null;
  totalWeight: number = 0;
  description: string = "";
  labelFormat: LabelFormat | null = null;
  dimensions: Dimensions = new Dimensions();

  constructor(obj?: Waybill) {
    if (obj) {
      this.reference = obj.reference || undefined;
      this.service = obj.service ? new Service(obj.service) : undefined;
      this.source = obj.source ? new Contact(obj.source) : undefined;
      this.destination = obj.destination ? new Contact(obj.destination) : undefined;
      this.items = obj.items || undefined;
      this.packaging = obj.packaging ? new PackageType(obj.packaging) : undefined;
      this.totalWeight = obj.totalWeight || undefined;
      this.description = obj.description || undefined;
      this.labelFormat = obj.labelFormat || undefined;
      this.dimensions = obj.dimensions ? new Dimensions(obj.dimensions) : undefined;
    }
  }
}

export class Contact {
  name: string = "";
  address: Address = new Address();
  phone: string = "";

  constructor(obj?: Contact) {
    if (obj) {
      this.name = obj.name || undefined;
      this.address = obj.address ? new Address(obj.address) : undefined;
      this.phone = obj.phone || undefined;
    }
  }
}

export class Address {
  street: string = "";
  city: string = "";
  state: string = "";
  postalCode: string = "";
  country: string = "";

  constructor(obj?: Address) {
    if (obj) {
      this.street = obj.street || undefined;
      this.city = obj.city || undefined;
      this.state = obj.state || undefined;
      this.postalCode = obj.postalCode || undefined;
      this.country = obj.country || undefined;
    }
  }
}

export class Dimensions {
  height: number = 0;
  width: number = 0;
  length: number = 0;

  constructor(obj?: Dimensions) {
    if (obj) {
      this.height = obj.height || undefined;
      this.width = obj.width || undefined;
      this.length = obj.length || undefined;
    }
  }
}

export type LabelFormat = string;

export class PackageType {
  id?: string;
  name?: string;

  constructor(obj?: PackageType) {
    if (obj) {
      this.id = obj.id || undefined;
      this.name = obj.name || undefined;
    }
  }
}

export class Service {
  id?: string;
  name?: string;

  constructor(obj?: Service) {
    if (obj) {
      this.id = obj.id || undefined;
      this.name = obj.name || undefined;
    }
  }
}
