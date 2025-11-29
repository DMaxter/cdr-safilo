import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { Image } from "@router/backend/services/image/types";

export const useClientStore = defineStore("clientStore", () => {
  const clients = ref<Client[]>([]);

  function init(data: Client[]) {
    clients.value = data;
  }

  function add(client: Client) {
    clients.value.push(client);
  }

  function edit(client: Client) {
    const index = clients.value.findIndex((c) => c.id === client.id);

    if (index === -1) {
      console.error(`Client ${client.id} not in store`);
      return
    }

    clients.value[index] = client;
  }

  function _editNote(client: number, note: string) {
    const index = clients.value.findIndex((c) => c.id === client);

    if (index === -1) {
      console.error(`Client ${id} not in store`);
      return
    }

    clients.value[index].note = note;
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
        };
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

  async function editClient(client: Client): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.clients.editClient(client);

      if (status === 200) {
        edit(client);

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  async function addClient(client: Client): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.clients.createClient(client);

      if (status === 200) {
        add(client);

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  async function editNote(client: number, note: string): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.clients.editClientNote(client, note);

      if (status === 200) {
        _editNote(client, note);

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  async function importClients(file: File): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.clients.importClients(file);

      if (status === 200) {
        return await getClients();
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  async function getClient(id: number): Promise<APIResponse<Client | string | null>> {
    let index = clients.value.findIndex((c) => c.id === id);

    if (index === -1) {
      let response = await getClients();

      if (response.status === false) {
        return response;
      }

      index = clients.value.findIndex((c) => c.id === id);
      if (index === -1) {
        console.error(`Client ${id} not in store`);
        return {
          success: false,
          status: 404,
          content: `Cliente ${id} não existe`,
        };
      } else {
        return {
          success: true,
          content: clients.value[index],
        };
      }
    } else {
      return {
        success: true,
        content: clients.value[index],
      };
    }
  }

  async function uploadImages(clientId: number, images: File[]): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.images.uploadClientImages(clientId, images);

      if (status === 200) {
        const newImages = data as Image[];
        const client = clients.value.find((c) => c.id === clientId);

        if (client) {
          if (!client.images) {
            client.images = [];
          }
          client.images.push(...newImages);
        }

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  async function deleteImages(clientId: number, imageIds: number[]): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.images.deleteClientImages(imageIds);

      if (status === 200) {
        clients.value.forEach((client) => {
          if (client.id === clientId) {
            client.images = client.images.filter((image) => !imageIds.includes(image.id));
          }
        });

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: data as string,
          status: status,
        };
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

  return {
    clients,
    addClient,
    editClient,
    editNote,
    getClient,
    getClients,
    importClients,
    uploadImages,
    deleteImages
  };
});
