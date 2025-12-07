import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { Login } from "@router/backend/services/auth/types";
import { getRole, Role, User } from "@router/backend/services/user/types";

export const useAuthStore = defineStore("authStore", () => {
  const logged = ref<User | null>(null);

  function hasRole(role: string): boolean {
    if (logged.value) {
      return logged.value!.roles.includes(getRole(role));
    } else {
      return false;
    }
  }

  async function isLoggedIn(): Promise<boolean> {
    try {
      const { status, data } = await API.users.getUserInfo();

      if (status === 200) {
        logged.value = data as User;

        return true;
      }

      return false;
    } catch (error) {
      return false;
    }
  }

  function isSafilo(): boolean {
    return hasRole(Role.MANAGER);
  }

  function isAdmin(): boolean {
    return hasRole(Role.ADMIN);
  }

  function isCdr(): boolean {
    return hasRole(Role.CDR);
  }

  function isCommercial(): boolean {
    return hasRole(Role.COMMERCIAL);
  }

  async function login(request: Login): Promise<APIResponse<null | string>> {
    try {
      const { status, data } = await API.auth.login(request);

      if (status === 200) {
        logged.value = data as User;

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

  async function logout(): Promise<APIResponse<null | string>> {
    try {
      const { status, data } = await API.auth.logout();

      if (status === 200) {
        logged.value = null;

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

  async function sendRecoveryEmail(email: string): Promise<APIResponse<null | string>> {
    try {
      const { status, data } = await API.auth.sendToken(email);

      if (status === 200) {
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

  async function changePassword(
    old: string,
    password: string,
  ): Promise<APIResponse<null | string>> {
    try {
      const { status, data } = await API.users.changePassword(old, password);

      if (status === 200) {
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
    logged,
    changePassword,
    login,
    logout,
    isLoggedIn,
    isAdmin,
    isCdr,
    isCommercial,
    isSafilo,
    sendRecoveryEmail,
  };
});
