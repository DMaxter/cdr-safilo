export type Rule<T = unknown> = (value: T) => boolean | string;

export const required: Rule = (value) => !!value || "Campo obrigatório";

export function validateField<T>(value: T, rules: Rule<T>[] = []): string | null {
  for (const rule of rules) {
    const result = rule(value);

    if (result !== true) {
      return typeof result === "string" ? result : "Valor inválido";
    }
  }

  return null;
}
