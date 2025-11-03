import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Client } from "@router/backend/services/client/types";

export async function getClients(): Promise<AxiosResponse<Client[] | Error>> {
  return await http.get("/client");
}

export async function getClient(id: number): Promise<AxiosResponse<Client | Error>> {
  return await http.get(`/client/${id}`);
}

export async function createClient(client: Client): Promise<AxiosResponse<Client | Error>> {
  return await http.post("/client", client);
}

export async function editClient(client: Client): Promise<AxiosResponse<Client | Error>> {
  return await http.put("/client", client);
}

export async function editClientNote(id: number, note: string): Promise<AxiosResponse<Client | Error>> {
  return await http.put(`/client/${id}`, note);
}

export async function importClients(file: string): Promise<AxiosResponse<null | Error>> {
  console.error("TODO");
}

export async function getBannerClients(banner: string): Promise<AxiosResponse<Client[] | Error>> {
  return await http.get(`/client/banner/${banner}`);
}
