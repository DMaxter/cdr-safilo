import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Price } from "@router/backend/services/price/types";

export async function getAllPrices(): Promise<AxiosResponse<Price[] | Error>> {
  return await http.get("/price");
}

export async function addPrice(price: Price): Promise<AxiosResponse<Price | Error>> {
  return await http.post("/price", price);
}

export async function updatePrice(price: Price): Promise<AxiosResponse<Price | Error>> {
  return await http.put("/price", price);
}

export async function deletePrice(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/price/${id}`);
}
