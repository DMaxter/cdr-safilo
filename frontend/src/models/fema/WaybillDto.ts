import Contact from "@models/fema/Contact";
import Dimensions from "@models/fema/Dimensions";
import LabelFormat from "@models/fema/LabelFormat";
import PackageType from "@models/fema/PackageType";
import Service from "@models/fema/Service";

export default class WaybillDto {
  reference: string | null = null;
  service: Service | null = null;
  source: Contact | null = null;
  destination: Contact = new Contact();
  items: number = 0;
  packaging: PackageType | null = null;
  totalWeight: double = 0;
  description: string = "";
  labelFormat: LabelFormat | null = null;
  dimensions: Dimensions = new Dimensions();

  constructor(obj?: Partial<WaybillDto>) {
    if (obj) {
      this.reference = obj.reference ?? null;
      this.service = obj.service ? new Service(obj.service) : null;
      this.source = obj.source ? new Contact(obj.source) : null;
      this.destination = obj.destination ? new Contact(obj.destination) : new Contact();
      this.items = obj.items ?? 0;
      this.packaging = obj.packaging ?? null;
      this.totalWeight = obj.totalWeight ?? null;
      this.description = obj.description ?? "";
      this.labelFormat = obj.labelFormat ? new LabelFormat(obj.labelFormat) : null;
      this.dimensions = obj.dimensions ? new Dimensions(obj.dimensions) : new Dimensions();
    }
  }
}
