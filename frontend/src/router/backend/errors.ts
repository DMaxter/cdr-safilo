export function getErrorMessage(data: unknown): string {
  if (typeof data === "string") {
    return data.length > 0 ? data : "Erro desconhecido";
  }

  if (data && typeof data === "object") {
    const obj = data as Record<string, unknown>;
    for (const key of ["message", "msg", "detail", "error"]) {
      if (typeof obj[key] === "string" && (obj[key] as string).length > 0) {
        return obj[key] as string;
      }
    }

    if (Array.isArray(obj["violations"])) {
      const messages = (obj["violations"] as Array<Record<string, unknown>>)
        .filter((v) => typeof v?.["message"] === "string")
        .map((v) => v["message"] as string)
        .filter((v) => v.length > 0);
      if (messages.length > 0) {
        return messages.join("; ");
      }
    }
  }

  return "Erro desconhecido";
}
