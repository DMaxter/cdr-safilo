import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { getErrorMessage } from "@router/backend/errors";
import { Finishing } from "@router/backend/services/finishing/types";

export const useFinishingStore = defineStore("finishingStore", () => {
  const finishings = ref<Finishing[]>([]);

  function init(data: Finishing[]) {
    finishings.value = data;
  }

  async function getFinishings(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.finishings.getAllFinishings();

      if (status === 200) {
        init(data as Finishing[]);
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

  async function addFinishing(finishing: Finishing): Promise<APIResponse<Finishing | string>> {
    try {
      const { status, data } = await API.finishings.addFinishing(finishing);

      if (status === 200) {
        const newFinishing = new Finishing(data as Finishing);
        finishings.value.push(newFinishing);
        return {
          success: true,
          content: newFinishing,
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

  async function editFinishing(finishing: Finishing): Promise<APIResponse<Finishing | string>> {
    try {
      const { status, data } = await API.finishings.updateFinishing(finishing);

      if (status === 200) {
        const updatedFinishing = new Finishing(data as Finishing);
        const index = finishings.value.findIndex((f) => f.id === updatedFinishing.id);
        if (index !== -1) {
          finishings.value[index] = updatedFinishing;
        }
        return {
          success: true,
          content: updatedFinishing,
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

  async function deleteFinishing(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.finishings.deleteFinishing(id);

      if (status === 200) {
        finishings.value = finishings.value.filter((f) => f.id !== id);
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

  async function makeFinishingObsolete(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.finishings.makeObsolete(id);

      if (status === 200) {
        const finishing = finishings.value.find((f) => f.id === id);
        if (finishing) {
          finishing.obsolete = true;
        } else {
          console.error(`Finishing ${id} not found in store`);
        }

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

  return {
    finishings,
    getFinishings,
    addFinishing,
    editFinishing,
    deleteFinishing,
    makeFinishingObsolete,
  };
});
