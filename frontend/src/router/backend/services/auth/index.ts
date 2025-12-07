import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { Login, Register } from "@router/backend/services/auth/types";
import { User } from "@router/backend/services/user/types";

export async function register(request: Register): Promise<AxiosResponse<User | Error>> {
  return await http.post("/auth/register", request);
}

export async function login(request: Login): Promise<AxiosResponse<undefined | Error>> {
  return await http.post("/auth/login", request);
}

export async function logout(): Promise<AxiosResponse<undefined | Error>> {
  return await http.delete("/auth/logout");
}

export async function sendToken(username: string): Promise<AxiosResponse<undefined | Error>> {
  return await http.post(`/auth/forgot/${encodeURI(username)}`);
}

export async function changePassword(
  username: string,
  password: string,
  token: string,
): Promise<AxiosResponse<undefined | Error>> {
  return await http.post(
    `/auth/forgot/${encodeURI(username)}/${encodeURI(password)}/${encodeURI(token)}`,
  );
}

export async function isLogged(): Promise<AxiosResponse<boolean | Error>> {
  return await http.get("/auth/logged");
}
