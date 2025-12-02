import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { NewRequest, Request } from "@router/backend/services/request/types";

export const useRequestStore = defineStore("requestStore", () => {
  const requests = ref<Request[]>([]);

  function init(data: Request[]) {
    requests.value = data;
  }

  function add(request: Request) {
    requests.value.push(request);
  }

  function _update(id: number, request: Partial<Request>) {
    const index = requests.value.findIndex((r) => r.id === id);

    if (index === -1) {
      console.error(`Request ${id} not in store`);
      return;
    }

    requests.value[index] = { ...requests.value[index], ...request };
  }

  async function getAllRequests(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.getAllRequests();

      if (status === 200) {
        init(data as Request[]);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function addRequest(request: NewRequest): Promise<APIResponse<Request | string>> {
    try {
      const { status, data } = await API.requests.addRequest(request);

      if (status === 200) {
        const newRequest = data as Request;
        add(newRequest);
        return {
          success: true,
          content: newRequest,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function editRequest(id: number, request: NewRequest): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.editRequest(id, request);

      if (status === 200) {
        _update(id, request as Partial<Request>);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function requestToProduction(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.requestToProduction(id);

      if (status === 200) {
        _update(id, { status: "IN_PRODUCTION" });
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function finishRequest(id: number, code: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.finishRequest(id, code);

      if (status === 200) {
        _update(id, { status: "DONE", trackingCode: code });
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function cancelRequest(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.cancelRequest(id);

      if (status === 200) {
        _update(id, { status: "CANCELLED" });
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function checkPrice(request: NewRequest): Promise<APIResponse<number | string>> {
    try {
      const { status, data } = await API.requests.checkPrice(request);

      if (status === 200) {
        return {
          success: true,
          content: data as number,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function exportRequests(): Promise<APIResponse<Blob | string>> {
    try {
      const { status, data } = await API.requests.exportRequests();

      if (status === 200) {
        return {
          success: true,
          content: data as Blob,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  return {
    requests,
    getAllRequests,
    addRequest,
    editRequest,
    requestToProduction,
    finishRequest,
    cancelRequest,
    checkPrice,
    exportRequests,
  };
});
