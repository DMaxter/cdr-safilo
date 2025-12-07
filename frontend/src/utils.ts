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
