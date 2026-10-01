import * as authController from "@router/backend/services/auth";
import * as brandController from "@router/backend/services/brand";
import * as clientController from "@router/backend/services/client";
import * as finishingController from "@router/backend/services/finishing";
import * as finishingGroupController from "@router/backend/services/finishingGroup";
import * as imageController from "@router/backend/services/image";
import * as materialController from "@router/backend/services/material";
import * as priceController from "@router/backend/services/price";
import * as requestController from "@router/backend/services/request";
import * as userController from "@router/backend/services/user";
import * as waybillController from "@router/backend/services/waybill";

export const API = {
  auth: authController,
  brands: brandController,
  clients: clientController,
  finishings: finishingController,
  finishingGroups: finishingGroupController,
  images: imageController,
  materials: materialController,
  prices: priceController,
  requests: requestController,
  users: userController,
  waybill: waybillController,
};
