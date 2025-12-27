import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Material } from "@router/backend/services/material/types";

export async function addMaterial(material: Material): Promise<AxiosResponse<Material | Error>> {
  return await http.post("/material", material);
}

export async function getAllMaterials(): Promise<AxiosResponse<Material[] | Error>> {
  return await http.get("/material");
}

export async function updateMaterial(material: Material): Promise<AxiosResponse<Material | Error>> {
  return await http.put("/material", material);
}

export async function deleteMaterial(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/material/${id}`);
}
