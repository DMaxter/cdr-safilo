import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Request } from "@router/backend/services/request/types";

export async function addRequest(request: Request): Promise<AxiosResponse<Request | Error>> {
  return await http.post("/request", request);
}

export async function checkPrice(request: Request): Promise<AxiosResponse<number | Error>> {
  return await http.post("/request/price", request);
}

export async function requestToProduction(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/request/production/${id}`);
}

export async function getAllRequests(): Promise<AxiosResponse<Request[] | Error>> {
  return await http.get("/request");
}

export async function finishRequest(
  id: number,
  code: number,
): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/request/finish/${id}/${code}`);
}

export async function cancelRequest(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/request/cancel/${id}`);
}

export async function editRequest(
  id: number,
  request: Request,
): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/request/${id}`, request);
}

export async function exportRequests(): Promise<AxiosResponse<Blob | Error>> {
  return await http.get("/request/export", { responseType: "blob" });
}
