import * as authController from "@router/backend/services/auth";
import * as clientController from "@router/backend/services/client";
import * as userController from "@router/backend/services/user";

export const API = {
  auth: authController,
  clients: clientController,
  users: userController,
};
