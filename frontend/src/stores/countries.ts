import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { getErrorMessage } from "@router/backend/errors";
import { Country } from "@router/backend/services/country/types";

export const useCountryStore = defineStore("countryStore", () => {
  const countries = ref<Country[]>([]);
  const loaded = ref(false);

  async function getCountries(force = false): Promise<APIResponse<string | null>> {
    if (!force && loaded.value) {
      return {
        success: true,
        content: null,
      };
    }

    try {
      const { status, data } = await API.countries.getCountries();

      if (status === 200) {
        countries.value = (data as Country[]).map((country) => new Country(country));
        loaded.value = true;

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: getErrorMessage(data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;

      return {
        success: false,
        status: _error.response?.status,
        content: getErrorMessage(_error.response?.data),
      };
    }
  }

  function label(code: string): string {
    return countries.value.find((country) => country.code === code)?.name ?? code;
  }

  return {
    countries,
    loaded,
    getCountries,
    label,
  };
});
