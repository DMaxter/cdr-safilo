import { Status } from "@router/backend/services/request/types";

export type EnumEntry = {
  name: string;
  value: string;
};

export function getEnumList(enumObj: object): EnumEntry[] {
  return Object.entries(enumObj).map(([key, value]) => ({ name: value, value: key }));
}

export function getEnumKeyByValue(enumObj: object, value: string): string | undefined {
  const entries = Object.entries(enumObj).filter(([_, val]) => val === value);
  if (entries.length > 0) {
    return entries[0][0];
  } else {
    console.error(`Value "${value}" not found in ${enumObj}`);
  }
}

export enum ManageMode {
  Add = "ADD",
  Edit = "EDIT",
  None = "NONE",
}

export function getStatusIcon(value: Status) {
  if (value === Status.Cancelled) {
    return "cancel";
  } else if (value === Status.Ordered) {
    return "package_2";
  } else if (value === Status.Done) {
    return "check";
  } else {
    return "question_mark";
  }
}

export function getStatusClass(value: Status): string {
  if (value === Status.Cancelled) {
    return "status-cancelled";
  } else if (value === Status.Ordered) {
    return "status-ordered";
  } else if (value === Status.Done) {
    return "status-done";
  } else {
    return "status-unknown";
  }
}
