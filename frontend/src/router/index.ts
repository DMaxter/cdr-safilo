import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@stores/auth";

const BASE_TITLE = "Casa dos Reclamos";

const routes = [
  {
    path: "/",
    name: "login",
    component: () => import("@views/Login.vue"),
    meta: {
      title: BASE_TITLE,
      requiresAuth: false,
    },
  },
  {
    path: "/profile",
    name: "profile",
    meta: {
      title: `Perfil | ${BASE_TITLE}`,
      requiresAuth: true,
    },
    component: () => import("@views/Profile.vue"),
  },
  {
    path: "/order",
    name: "order",
    meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/OrderWizard.vue"),
  },
  {
    path: "/redefine-password",
    name: "redefine-password",
    meta: {
      title: "Redefinir Palavra-passe | " + import.meta.env.VUE_APP_NAME,
      requiresAuth: false,
    },
    component: () => import("@views/RedefinePassword.vue"),
  },
  {
    path: "/request/:id",
    name: "request",
    meta: {
      title: "Detalhe do Pedido | " + import.meta.env.VUE_APP_NAME,
      requiresAuth: true,
    },
    component: () => import("@views/RequestInfo.vue"),
  },
  {
    path: "/search",
    name: "search",
    meta: { title: "Pesquisa | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/Search.vue"),
  },
  {
    path: "/clients",
    name: "clients",
    meta: { title: "Clientes | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/ClientList.vue"),
  },
  {
    path: "/client",
    name: "client",
    meta: {
      title: "Informação de Cliente | " + import.meta.env.VUE_APP_NAME,
      requiresAuth: true,
    },
    component: () => import("@views/ClientInfo.vue"),
  },
  {
    path: "/configure",
    name: "configure",
    meta: { title: "Configurar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/Configure.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();

  //check sensitive routes
  if (to.meta.requiresAuth) {
    if (await authStore.isLoggedIn()) {
      next();
    } else {
      next("/");
    }
  } else {
    next();
  }
});

router.afterEach(async (to) => {
  document.title = to.meta.title as string;
});

export default router;
