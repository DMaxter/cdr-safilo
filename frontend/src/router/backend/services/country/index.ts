import type { AxiosResponse } from "axios";

import { http } from "@router/backend/api";
import type { Error } from "@router/backend/types";
import type { Country } from "@router/backend/services/country/types";

export async function getCountries(): Promise<AxiosResponse<Country[] | Error>> {
  return await http.get("/country");
}
