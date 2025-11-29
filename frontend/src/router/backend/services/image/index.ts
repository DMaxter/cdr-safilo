import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Image } from "@router/backend/services/image/types";

export async function makeImageObsolete(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.put(`/image/obsolete/${id}`);
}

export async function uploadClientImages(
  clientId: number,
  images: File[]
): Promise<AxiosResponse<Image[] | Error>> {
  const form = new FormData();

  images.forEach((image) => {
    form.append("images", image);
  });

  return await http.post(`/image/client/${clientId}`, form, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    timeout: 300000,
  });
}

export async function deleteClientImages(imageIds: number[]): Promise<AxiosResponse<null | Error>> {
  return await http.delete("/image/client", { data: imageIds });
}
