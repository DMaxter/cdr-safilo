import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Brand } from "@router/backend/services/brand/types";
import { Image } from "@router/backend/services/image/types";

export async function getBrands(): Promise<AxiosResponse<Brand[] | Error>> {
  return await http.get("/brand");
}

export async function createBrand(name: string): Promise<AxiosResponse<Brand | Error>> {
  return await http.post(`/brand/${name}`);
}

export async function editBrand(id: number, name: string): Promise<AxiosResponse<Brand | Error>> {
  return await http.put(`/brand/${id}/${name}`);
}

export async function deleteBrand(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/brand/${id}`);
}

export async function addBrandImage(id: number, image: string): Promise<AxiosResponse<Image | Error>> {
  return await http.post(`/brand/image/${id}`, image, {
    headers: { "Content-Type": "text/plain" },
  });
}

export async function deleteBrandImage(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/brand/image/${id}`);
}
