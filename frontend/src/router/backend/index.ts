import * as authController from "@router/backend/services/auth";
import * as brandController from "@router/backend/services/brand";
import * as clientController from "@router/backend/services/client";
import * as imageController from "@router/backend/services/image";
import * as requestController from "@router/backend/services/request";
import * as userController from "@router/backend/services/user";

export const API = {
  auth: authController,
  brands: brandController,
  clients: clientController,
  images: imageController,
  requests: requestController,
  users: userController,
};
