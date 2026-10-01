/* eslint-disable @typescript-eslint/no-explicit-any */

export type Rule = (value: any) => boolean | string;

export const required: Rule = (value) => !!value || "Campo obrigatório";

export function validateField(value: unknown, rules: Rule[] = []): string | null {
  for (const rule of rules) {
    const result = rule(value);

    if (result !== true) {
      return typeof result === "string" ? result : "Valor inválido";
    }
  }

  return null;
}
