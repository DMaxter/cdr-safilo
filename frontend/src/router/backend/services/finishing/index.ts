import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Finishing } from "@router/backend/services/finishing/types";

export async function addFinishing(
  finishing: Finishing,
): Promise<AxiosResponse<Finishing | Error>> {
  return await http.post("/finishing", finishing);
}

export async function updateFinishing(
  finishing: Finishing,
): Promise<AxiosResponse<Finishing | Error>> {
  return await http.put("/finishing", finishing);
}

export async function getAllFinishings(): Promise<AxiosResponse<Finishing[] | Error>> {
  return await http.get("/finishing");
}

export async function deleteFinishing(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/finishing/${id}`);
}

export async function makeObsolete(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/finishing/obsolete/${id}`);
}
