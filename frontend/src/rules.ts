export const required = (value) => !!value || "Campo obrigatório";

export const checkAllRefsValid = (refs) => {
  try {
    return refs.every((ref) => ref.value.isValid);
  } catch (error) {
    return false;
  }
};
