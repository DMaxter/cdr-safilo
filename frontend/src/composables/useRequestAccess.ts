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

  /** CDR/ADMIN/MANAGER, or the commercial who created the request, while it is still ordered. */
  function canManage(request: Request): boolean {
    if (request.status !== Status.Ordered) {
      return false;
    }

    return canManipulate || (authStore.isCommercial() && request.user === authStore.logged?.name);
  }

  /** Whether the current user may cancel this request (same backend rule as edit). */
  function canCancel(request: Request): boolean {
    return canManage(request);
  }

  /** Whether the current user may edit this request. PUT /request/{id} enforces the same rule server-side. */
  function canEdit(request: Request): boolean {
    return canManage(request);
  }

  /** The waybill code/button is only shown to CDR/ADMIN and never for cancelled requests. */
  function canSeeWaybill(request: Request): boolean {
    return (authStore.isCdr() || authStore.isAdmin()) && request.status !== Status.Cancelled;
  }

  return { canCancel, canEdit, canSeeWaybill };
}
