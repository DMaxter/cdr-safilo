import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { User } from "@router/backend/services/user/types";

export const useUserStore = defineStore("userStore", () => {
  const users = ref<User[]>([]);

  function init(data: User[]) {
    users.value = data;
  }

  async function getUsers(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.users.getUsers();

      if (status === 200) {
        init(data as User[]);

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

  async function changePlafond(
    user: string,
    brand: number,
    amount: number,
  ): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.users.changePlafond(user, brand, amount);

      if (status === 200) {
        const updatedUser = data as User;
        const userIndex = users.value.findIndex((u) => u.email === updatedUser.email);
        if (userIndex !== -1) {
          users.value[userIndex].credits = updatedUser.credits;
        }
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
    users,
    getUsers,
    changePlafond,
  };
});
