import { reactive } from "vue";
import { defineStore } from "pinia";

import Backend from "@/router/backend";
import UserDto from "@models/UserDto";

export const useUserStore = defineStore("user", () => {
  const user = reactive(new UserDto());

  async function init() {
    if (user.email === null) {
      let updated = await Backend.getProfile();

      user.email = updated.email;
      user.name = updated.name;
      user.credits = updated.credits;
      user.roles = updated.roles;
    }
  }

  function hasRole(role: string): boolean {
    return user.roles!.includes(role);
  }

  function clear() {
    user.email = null;
    user.name = null;
    user.credits = null;
    user.roles = null;
  }

  function isSafilo(): boolean {
    return hasRole("MANAGER");
  }

  function isAdmin(): boolean {
    return hasRole("ADMIN");
  }

  function isCdr(): boolean {
    return hasRole("CDR");
  }

  function isCommercial(): boolean {
    return hasRole("COMMERCIAL");
  }

  return { user, init, clear, isSafilo, isCdr, isAdmin, isCommercial };
});
