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
      this.reference = obj.reference || "";
      this.service = obj.service ? new Service(obj.service) : null;
      this.source = obj.source ? new Contact(obj.source) : null;
      this.destination = obj.destination ? new Contact(obj.destination) : new Contact();
      this.items = obj.items || 0;
      this.packaging = obj.packaging ? new PackageType(obj.packaging) : null;
      this.totalWeight = obj.totalWeight || 0;
      this.description = obj.description || "";
      this.labelFormat = obj.labelFormat || null;
      this.dimensions = obj.dimensions ? new Dimensions(obj.dimensions) : new Dimensions();
    }
  }
}

export class Contact {
  name: string | null = null;
  address: Address = new Address();
  phone: string | null = null;

  constructor(obj?: Contact) {
    if (obj) {
      this.name = obj.name ?? null;
      this.address = obj.address ? new Address(obj.address) : new Address();
      this.phone = obj.phone ?? null;
    }
  }
}

export class Address {
  address: string | null = null;
  city: string | null = null;
  postalCode: string | null = null;
  country: string | null = null;

  constructor(obj?: Address) {
    if (obj) {
      this.address = obj.address ?? null;
      this.city = obj.city ?? null;
      this.postalCode = obj.postalCode ?? null;
      this.country = obj.country ?? null;
    }
  }
}

export class Dimensions {
  height: number = 0;
  width: number = 0;
  length: number = 0;

  constructor(obj?: Dimensions) {
    if (obj) {
      this.height = obj.height || 0;
      this.width = obj.width || 0;
      this.length = obj.length || 0;
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
