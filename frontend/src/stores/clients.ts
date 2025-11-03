import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";

export const useClientStore = defineStore("clientStore", () => {
  const clients = ref<Client[]>([]);

  function init(data: Client[]) {
    clients.value = data;
  }

  function add(client: Client) {
    clients.value.push(client);
  }

  function remove(id: number) {
    const index = clients.value.findIndex((c) => c.id === id);

    if (index === -1) {
      console.error(`Client ${id} not in store`);
      return
    }

    clients.value.splice(index, 1);
  }

  async function getClients(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.clients.getClients();

      if (status === 200) {
        init(data as Client[]);

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        }
      }
    } catch (error) {
      const _error = error as AxiosError<string>;

      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.message,
      };
    }
  }

  return { clients, getClients };
});
