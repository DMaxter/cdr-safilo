/* eslint-disable @typescript-eslint/no-explicit-any */
export const required = (value: any) => !!value || "Campo obrigatório";

export const checkAllRefsValid = (refs: any[]) => {
  try {
    return refs.every((ref) => ref?.value?.isValid ?? true);
  } catch (error) {
    return false;
  }
};
