import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { FinishingGroup } from "@router/backend/services/finishingGroup/types";
import { Finishing } from "@router/backend/services/finishing/types";

export const useFinishingGroupStore = defineStore("finishingGroupStore", () => {
  const finishingGroups = ref<FinishingGroup[]>([]);

  function init(data: FinishingGroup[]) {
    finishingGroups.value = data;
  }

  async function getFinishingGroups(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.finishingGroups.getAllGroups();

      if (status === 200) {
        init(data as FinishingGroup[]);
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
        content: _error.response?.data?.message ?? "Erro desconhecido",
      };
    }
  }

  async function addGroup(
    name: string,
    finishings: Finishing[],
  ): Promise<APIResponse<FinishingGroup | string>> {
    try {
      const { status, data } = await API.finishingGroups.createGroup(name, finishings);

      if (status === 200) {
        const newGroup = new FinishingGroup(data as FinishingGroup);
        finishingGroups.value.push(newGroup);
        return {
          success: true,
          content: newGroup,
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
        content: _error.response?.data?.message ?? "Erro desconhecido",
      };
    }
  }

  async function editGroup(group: FinishingGroup): Promise<APIResponse<FinishingGroup | string>> {
    try {
      const { status, data } = await API.finishingGroups.editGroup(group);

      if (status === 200) {
        const updatedGroup = new FinishingGroup(data as FinishingGroup);
        const index = finishingGroups.value.findIndex((g) => g.id === updatedGroup.id);
        if (index !== -1) {
          finishingGroups.value[index] = updatedGroup;
        }
        return {
          success: true,
          content: updatedGroup,
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
        content: _error.response?.data?.message ?? "Erro desconhecido",
      };
    }
  }

  async function deleteGroup(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.finishingGroups.deleteGroup(id);

      if (status === 200) {
        finishingGroups.value = finishingGroups.value.filter((g) => g.id !== id);
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
        content: _error.response?.data?.message ?? "Erro desconhecido",
      };
    }
  }

  return {
    finishingGroups,
    getFinishingGroups,
    addGroup,
    editGroup,
    deleteGroup,
  };
});
