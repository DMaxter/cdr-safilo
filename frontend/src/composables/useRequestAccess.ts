import { Status, type Request } from "@router/backend/services/request/types";
import { useAuthStore } from "@stores/auth";

/**
 * Single source of truth for the role/ownership rules that gate request actions.
 *
 * The request view is rendered from two shells (the `/search` summary dialog and
 * the `/request/:id` page) — every gate must be evaluated the same way in both,
 * so it lives here instead of being copied into each component.
 */
export function useRequestAccess() {
  const authStore = useAuthStore();

  const canManipulate = authStore.isSafilo() || authStore.isCdr() || authStore.isAdmin();

  /** The commercial who created the request, matched by email (preferred) or display name. */
  function isOwnedByCommercial(request: Request): boolean {
    if (!authStore.isCommercial()) {
      return false;
    }

    const loggedEmail = authStore.logged?.email;
    if (loggedEmail && request.userEmail && loggedEmail === request.userEmail) {
      return true;
    }

    const loggedName = authStore.logged?.name;
    return !!loggedName && request.user === loggedName;
  }

  function canEdit(request: Request): boolean {
    if (request.status !== Status.Ordered) {
      return false;
    }

    return canManipulate || isOwnedByCommercial(request);
  }

  function canCancel(request: Request): boolean {
    if (request.status !== Status.Ordered) {
      return false;
    }

    return authStore.isSafilo() || authStore.isAdmin() || isOwnedByCommercial(request);
  }

  /** The waybill code/button is only shown to CDR/ADMIN and never for cancelled requests. */
  function canSeeWaybill(request: Request): boolean {
    return (authStore.isCdr() || authStore.isAdmin()) && request.status !== Status.Cancelled;
  }

  /** CDR/ADMIN may advance a still-ordered request to production, once it has been printed. */
  function canSendToProduction(request: Request): boolean {
    return (authStore.isCdr() || authStore.isAdmin()) && request.status === Status.Ordered;
  }

  return { canCancel, canEdit, canSeeWaybill, canSendToProduction };
}
