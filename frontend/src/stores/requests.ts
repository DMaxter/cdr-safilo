import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import { getBlobErrorMessage, getErrorMessage } from "@router/backend/errors";
import type { Error, APIResponse } from "@router/backend/types";
import { Request, Status } from "@router/backend/services/request/types";

export const useRequestStore = defineStore("requestStore", () => {
  const requests = ref<Request[]>([]);
  // Tracks whether the request list has been fetched at least once, so returning
  // to screens like Search reuses the cached data instead of re-hitting the backend.
  const loaded = ref(false);

  function init(data: Request[]) {
    requests.value = data.map((item) => new Request(item));
    loaded.value = true;
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

    const updated = { ...requests.value[index], ...request };

    requests.value[index] = new Request(updated as unknown as Request);
  }

  async function getAllRequests(force = false): Promise<APIResponse<string | null>> {
    if (!force && loaded.value) {
      return { success: true, content: null, status: 200 };
    }

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

  async function addRequest(request: Request): Promise<APIResponse<Request | string>> {
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

  async function editRequest(id: number, request: Request): Promise<APIResponse<string | null>> {
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

  async function cancelRequest(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.requests.cancelRequest(id);

      if (status === 200) {
        _update(id, { status: Status.Cancelled });
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

  async function checkPrice(request: Request): Promise<APIResponse<number | string>> {
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

  // Auth failures return an empty body, so answer them by status.
  async function exportErrorMessage(status: number | undefined, data: unknown): Promise<string> {
    if (status === 401) {
      return "Sessão expirada. Volte a entrar.";
    }
    if (status === 403) {
      return "Não tem permissões para descarregar os pedidos.";
    }

    return await getBlobErrorMessage(data);
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
          content: await exportErrorMessage(status, data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        // No response body (timeout / network failure)
        content: _error.response
          ? await exportErrorMessage(_error.response.status, _error.response.data)
          : "Tempo de espera esgotado. Tente novamente mais tarde.",
      };
    }
  }

  return {
    requests,
    loaded,
    getAllRequests,
    addRequest,
    editRequest,
    cancelRequest,
    checkPrice,
    exportRequests,
  };
});
