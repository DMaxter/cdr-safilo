import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { Finishing } from "../finishing/types";

export async function createGroup(
  name: string,
  finishings: Finishing[],
): Promise<AxiosResponse<FinishingGroup | Error>> {
  return await http.post(`/finishing/group/${name}`, finishings);
}

export async function getAllGroups(): Promise<AxiosResponse<FinishingGroup[] | Error>> {
  return await http.get("/finishing/group");
}

export async function editGroup(
  group: FinishingGroup,
): Promise<AxiosResponse<FinishingGroup | Error>> {
  return await http.put("/finishing/group", group);
}

export async function deleteGroup(id: number): Promise<AxiosResponse<null | Error>> {
  return await http.delete(`/finishing/group/${id}`);
}
