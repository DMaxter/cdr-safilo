import type { AxiosResponse } from "axios";
import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import type { Waybill, Service, PackageType, LabelFormat } from "./types";

export async function getShippingServices(requestId: number): Promise<AxiosResponse<Service[] | Error>> {
  return await http.get(`/waybill/services/${requestId}`);
}

export async function getPackageTypes(): Promise<AxiosResponse<PackageType[] | Error>> {
  return await http.get("/waybill/packages");
}

export async function getLabelFormats(): Promise<AxiosResponse<LabelFormat[] | Error>> {
  return await http.get("/waybill/formats");
}

export async function openWaybill(
  requestId: number,
  waybill: Waybill
): Promise<AxiosResponse<File | Error>> {
  return await http.post(`/waybill/${requestId}`, waybill, {
    responseType: "blob"
  });
}

export async function getWaybill(
  requestId: number,
  labelFormat: LabelFormat
): Promise<AxiosResponse<File | Error>> {
  return await http.get(`/waybill/${requestId}/${labelFormat}`, {
    responseType: "blob"
  });
}

export async function cancelWaybill(requestId: number): Promise<AxiosResponse<void | Error>> {
  return await http.delete(`/waybill/${requestId}`);
}
