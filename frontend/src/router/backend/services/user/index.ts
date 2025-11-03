import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { User } from "@router/backend/services/user/types";

export async function getUsers(): Promise<AxiosResponse<User[] | Error>> {
  return await http.get("/user/all");
}

export async function getUserInfo(): Promise<AxiosResponse<User | Error>> {
  return await http.get("/user");
}

export async function changePassword(old: string, password: string): Promise<AxiosResponse<undefined | Error>> {
  return await http.put(`/user/password/${encodeURI(old)}/${encodeURI(password)}`);
}

export async function changePlafond(user: string, brand: number, amount: number): Promise<AxiosResponse<undefined | Error>> {
  return await http.put(`/user/plafond/${encodeURI(user)}/${brand}/${amount}`);
}
