import { Brand } from "@router/backend/services/brand/types";
import { Client } from "@router/backend/services/client/types";

export class Request {
  id: number | null = null;
  user: string | null = null;
  client: Client | null = null;
  created: Date | null = null;
  lastUpdate: Date | null = null;
  trackingCode: number | null = null;
  status: string = "";
  type: any | null = null;
  cost: number | null = null;
  observations: string | null = null;
  application: boolean | null = null;
  brand: Brand | null = null;
  amount: number | null = null;
}
