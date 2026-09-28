import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Request, RequestSlot } from "@router/backend/services/request/types";

const SLOT_KEYS: Record<string, string[]> = {
  OneFace: ["cover"],
  TwoFaces: ["cover", "back"],
  SimpleShowcase: ["top", "bottom", "left", "right"],
  LeftShowcase: ["top", "bottom", "left", "right", "side"],
  RightShowcase: ["top", "bottom", "left", "right", "side"],
};

function toNewRequestDto(request: Request) {
  const typeDto: Record<string, unknown> = { type: request.type?.type ?? "" };

  for (const key of SLOT_KEYS[typeDto.type as string] ?? []) {
    const slot = (request.type as unknown as Record<string, RequestSlot | null>)[key];
    if (!slot) continue;

    typeDto[key] = {
      image: slot.image ? { id: slot.image.id } : null,
      measurements: slot.measurements
        ? { height: slot.measurements.height, width: slot.measurements.width }
        : null,
      material: slot.material ? { id: slot.material.id } : null,
      finishings: slot.finishings.map((f) => ({ id: f.id })),
    };
  }

  return {
    clientId: request.client?.id ?? null,
    amount: request.amount,
    observations: request.observations,
    application: request.application,
    brand: request.brand ? { id: request.brand.id } : undefined,
    type: typeDto,
  };
}

export async function addRequest(request: Request): Promise<AxiosResponse<Request | Error>> {
  return await http.post("/request", toNewRequestDto(request));
}

export async function checkPrice(request: Request): Promise<AxiosResponse<number | Error>> {
  return await http.post("/request/price", toNewRequestDto(request));
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
  return await http.put(`/request/${id}`, toNewRequestDto(request));
}

export async function exportRequests(): Promise<AxiosResponse<Blob | Error>> {
  return await http.get("/request/export", { responseType: "blob" });
}
