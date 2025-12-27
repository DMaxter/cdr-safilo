import { Brand } from "@router/backend/services/brand/types";
import { Client } from "@router/backend/services/client/types";
import { Image } from "@router/backend/services/image/types";
import { Material } from "@router/backend/services/material/types";
import { Finishing } from "@router/backend/services/finishing/types";

export class Request {
  id: number = 0;
  user: string = "";
  client: Client | null = null;
  created: Date | null = null;
  lastUpdate: Date | null = null;
  trackingCode: number | null = null;
  status: Status = Status.Ordered;
  type: RequestType | null = null;
  cost: number = 0;
  observations: string = "";
  application: boolean = false;
  brand: Brand | null = null;
  amount: number = 0;

  constructor(obj?: Request) {
    if (obj) {
      this.id = obj.id;
      this.user = obj.user;
      this.client = obj.client ? new Client(obj.client) : null;
      this.created = obj.created ? new Date(obj.created) : null;
      this.lastUpdate = obj.lastUpdate ? new Date(obj.lastUpdate) : null;
      this.trackingCode = obj.trackingCode;
      this.status = obj.status;
      this.cost = obj.cost;
      this.observations = obj.observations;
      this.application = obj.application;
      this.brand = obj.brand ? new Brand(obj.brand) : null;
      this.amount = obj.amount;

      if (obj.type) {
        if (obj.type.type === "OneFace") {
          this.type = new OneFace(obj.type as OneFace);
        } else if (obj.type.type === "TwoFaces") {
          this.type = new TwoFaces(obj.type as TwoFaces);
        } else if (obj.type.type === "SimpleShowcase") {
          this.type = new SimpleShowcase(obj.type as SimpleShowcase);
        } else if (obj.type.type === "LeftShowcase") {
          this.type = new LeftShowcase(obj.type as LeftShowcase);
        } else if (obj.type.type === "RightShowcase") {
          this.type = new RightShowcase(obj.type as RightShowcase);
        }
      }
    }
  }

  getFinishings(): string[] {
    return this.type.getFinishings();
  }

  getMaterials(): string[] {
    return this.type.getMaterials();
  }

  getMeasurements(): number[][] {
    return this.type.getMeasurements();
  }
}

export abstract class RequestType {
  type: string = "";

  abstract getFinishings(): string[];

  abstract getMaterials(): string[];

  abstract getMeasurements(): number[][];

  constructor(obj?: RequestType) {
    if (obj) {
      this.type = obj.type;
    }
  }
}

export class Showcase extends RequestType {
  top: RequestSlot | null = null;
  bottom: RequestSlot | null = null;
  left: RequestSlot | null = null;
  right: RequestSlot | null = null;
  side: RequestSlot | null = null;

  constructor(obj?: Showcase) {
    super(obj);
    if (obj) {
      this.top = obj.top ? new RequestSlot(obj.top) : null;
      this.bottom = obj.bottom ? new RequestSlot(obj.bottom) : null;
      this.left = obj.left ? new RequestSlot(obj.left) : null;
      this.right = obj.right ? new RequestSlot(obj.right) : null;
      this.side = obj.side ? new RequestSlot(obj.side) : null;
    }
  }

  getFinishings(): string[] {
    let result: Set<string> = new Set();

    result = result.union(this.top.getFinishings());
    result = result.union(this.bottom.getFinishings());
    result = result.union(this.left.getFinishings());
    result = result.union(this.right.getFinishings());
    result = result.union(this.side?.getFinishings() || []);

    return Array.from(result);
  }

  getMaterials(): string[] {
    let result: Set<string> = new Set();

    result.add(this.top.getMaterial());
    result.add(this.bottom.getMaterial());
    result.add(this.left.getMaterial());
    result.add(this.right.getMaterial());

    if (this.side) {
      result.add(this.side.getMaterial());
    }

    return Array.from(result);
  }

  getMeasurements(): number[][] {
    let result = [];

    result.add(this.top.getMeasurements());
    result.add(this.bottom.getMeasurements());
    result.add(this.left.getMeasurements());
    result.add(this.right.getMeasurements());

    if (this.side) {
      result.add(this.side.getMeasurements());
    }

    return result;
  }
}

export class OneFace extends RequestType {
  cover: RequestSlot | null = null;

  constructor(obj?: OneFace) {
    super(obj);
    if (obj) {
      this.cover = obj.cover ? new RequestSlot(obj.cover) : null;
    }
  }

  getFinishings(): string[] {
    return Array.from(this.cover.getFinishings());
  }

  getMaterials(): string[] {
    return [this.cover.getMaterial()];
  }

  getMeasurements(): number[][] {
    return [this.cover.getMeasurements()];
  }
}

export class TwoFaces extends RequestType {
  cover: RequestSlot | null = null;
  back: RequestSlot | null = null;

  constructor(obj?: TwoFaces) {
    super(obj);
    if (obj) {
      this.cover = obj.cover ? new RequestSlot(obj.cover) : null;
      this.back = obj.back ? new RequestSlot(obj.back) : null;
    }
  }

  getFinishings(): string[] {
    let result: Set<string> = new Set();

    result = result.union(this.cover.getFinishings());
    result = result.union(this.back.getFinishings());

    return Array.from(result);
  }

  getMaterials(): string[] {
    let result: Set<string> = new Set();

    result.add(this.cover.getMaterial());
    result.add(this.back.getMaterial());

    return Array.from(result);
  }

  getMeasurements(): number[][] {
    return [this.cover.getMeasurements(), this.back.getMeasurements()];
  }
}

export class SimpleShowcase extends Showcase {}

export class LeftShowcase extends Showcase {}

export class RightShowcase extends Showcase {}

export class RequestSlot {
  id: number = 0;
  image: Image | null = null;
  measurements: Measurements | null = null;
  material: Material | null = null;
  finishings: Finishing[] = [];

  constructor(obj?: RequestSlot) {
    if (obj) {
      this.id = obj.id;
      this.image = obj.image ? new Image(obj.image) : null;
      this.measurements = obj.measurements ? new Measurements(obj.measurements) : null;
      this.material = obj.material ? new Material(obj.material) : null;
      this.finishings = obj.finishings ? obj.finishings.map((f) => new Finishing(f)) : [];
    }
  }

  getFinishings(): Set<string> {
    let result: Set<string> = new Set();

    this.finishings.forEach((f) => result.add(f.name));

    return result;
  }

  getMaterial(): string {
    return this.material.name;
  }

  getMeasurements(): number[] {
    return [this.measurements.height, this.measurements.width];
  }
}

export class Measurements {
  height: number = 0;
  width: number = 0;

  constructor(obj?: Measurements) {
    if (obj) {
      this.height = obj.height;
      this.width = obj.width;
    }
  }
}

export enum Status {
  Ordered = "ORDERED",
  InProduction = "IN_PRODUCTION",
  Done = "DONE",
  Cancelled = "CANCELLED",
}
