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
  //{
  //  path: '/order',
  //  name: 'order',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrder.vue')
  //},
  //{
  //  path: '/orderClient',
  //  name: 'orderClient',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderClient.vue')
  //},
  //{
  //  path: '/order2',
  //  name: 'order2',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderOneOrTwo.vue')
  //},
  //{
  //  path: '/order3',
  //  name: 'order3',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderOneOrTwoFinal.vue')
  //},
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
    path: "/search",
    name: "search",
    meta: { title: "Pesquisa | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/Search.vue"),
  },
  //{
  //  path: '/ABC',
  //  name: 'ABC',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderMontra.vue')
  //},
  //{
  //  path: '/order22',
  //  name: 'order22',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderOrderOneOrTwoSecond.vue')
  //},
  //{
  //  path: '/ABCfinal',
  //  name: 'ABCfinal',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderMontra2.vue')
  //},
  //{
  //  path: '/ABCfinal2',
  //  name: 'ABCfinal2',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CustomerOrderMontraFinal.vue')
  //},
  //{
  //  path: '/Details',
  //  name: 'details',
  //  meta: { title: "Detalhes | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/OrderDetails.vue')
  //},
  //{
  //  path: '/DetailsMontra',
  //  name: 'detailsMontra',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/DetailsMontra.vue')
  //},
  //{
  //  path: '/DetailsMontraFinal',
  //  name: 'detailsMontraFinal',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/DetailsMontraFinal.vue')
  //},
  //{
  //  path: '/DetailsOneOrTwo',
  //  name: 'detailsOneOrTwo',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/DetailsOneOrTwo.vue')
  //},
  //{
  //  path: '/DetailsOneOrTwoFinal',
  //  name: 'detailsOneOrTwoFinal',
  //  meta: { title: "Encomendar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/DetailsOneOrTwoFinal.vue')
  //},
  {
    path: "/clients",
    name: "clients",
    meta: { title: "Clientes | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/ClientList.vue"),
  },
  {
    path: "/client",
    name: "client",
    meta: { title: "Informação de Cliente | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/ClientInfo.vue"),
  },
  //{
  //  path: '/ClientStores',
  //  name: 'clientStores',
  //  meta: { title: "Lojas do Cliente | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/SafiloClientStores.vue')
  //},
  //{
  //  path: '/StoreInfo',
  //  name: 'storeInfo',
  //  meta: { title: "Informação de Loja | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/SafiloClientSpecificStore.vue')
  //},
  {
    path: "/configure",
    name: "configure",
    meta: { title: "Configurar | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
    component: () => import("@views/Configure.vue"),
  },
  //{
  //  path: '/Materiais',
  //  name: 'materiais',
  //  meta: { title: "Materiais | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/CdrMaterials.vue')
  //},
  //{
  //  path: '/clientHistory',
  //  name: 'clientHistory',
  //  meta: { title: "Histórico do Cliente | " + import.meta.env.VUE_APP_NAME, requiresAuth: true },
  //  component: () => import('@views/SafiloClientHistory.vue')
  //},
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
