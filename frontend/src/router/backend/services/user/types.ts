export enum Role {
  COMMERCIAL = "Comercial",
  UPLOADER = "Uploader",
  CDR = "CDR",
  MANAGER = "Manager",
  ADMIN = "Admin",
}

import { getEnumKeyByValue, getEnumList } from "@/utils";

export const ROLES = getEnumList(Role);
export function getRole(value: string): Role | undefined {
  return getEnumKeyByValue(Role, value) as Role;
}

export class Plafond {
  brand: string = "";
  amount: number = 0;
}

export class User {
  email: string = "";
  name: string = "";
  roles: Role[] = [];
  credits: Plafond[] = [];
}
