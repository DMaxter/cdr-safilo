import Vue from 'vue'
import VueRouter from 'vue-router'
import Backend from './backend'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLogin.vue')
  },
  {
    path: '/profile',
    name: 'profile',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerProfile.vue')
  },
  {
    path: '/search',
    name: 'search',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerSearch.vue')
  },
  {
    path: '/order',
    name: 'order',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrder.vue')
  },
  {
    path: '/orderClient',
    name: 'orderClient',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderClient.vue')
  },
  {
    path: '/order2',
    name: 'order2',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOneOrTwo.vue')
  },
  {
    path: '/order3',
    name: 'order3',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOneOrTwoFinal.vue')
  },
  {
    path: '/recoverCode',
    name: 'recoverCode',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLoginPassword.vue')
  },
  {
    path: '/redefinePassword',
    name: 'redefinePassword',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLoginPasswordChange.vue')
  },
  {
    path: '/passwordChange',
    name: 'passwordChange',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerProfilePassword.vue')
  },
  {
    path: '/history',
    name: 'history',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerHistory.vue')
  },
  {
    path: '/plafond',
    name: 'plafond',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerPlafond.vue')
  },
  {
    path: '/ABC',
    name: 'ABC',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontra.vue')
  },
  {
    path: '/order22',
    name: 'order22',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOrderOneOrTwoSecond.vue')
  },
  {
    path: '/ABCfinal',
    name: 'ABCfinal',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontra2.vue')
  },
  {
    path: '/ABCfinal2',
    name: 'ABCfinal2',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontraFinal.vue')
  },
  {
    path: '/Details',
    name: 'details',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/OrderDetails.vue')
  },
  {
    path: '/DetailsMontra',
    name: 'detailsMontra',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsMontra.vue')
  },
  {
    path: '/DetailsMontraFinal',
    name: 'detailsMontraFinal',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsMontraFinal.vue')
  },
  {
    path: '/DetailsOneOrTwo',
    name: 'detailsOneOrTwo',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsOneOrTwo.vue')
  },
  {
    path: '/DetailsOneOrTwoFinal',
    name: 'detailsOneOrTwoFinal',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsOneOrTwoFinal.vue')
  },
  {
    path: '/SearchResults',
    name: 'searchResults',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SearchResults.vue')
  },
  {
    path: '/Clients',
    name: 'clients',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClients.vue')
  },
  {
    path: '/ClientInfo',
    name: 'clientInfo',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloSpecificClient.vue')
  },
  {
    path: '/ClientStores',
    name: 'clientStores',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientStores.vue')
  },
  {
    path: '/StoreInfo',
    name: 'storeInfo',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientSpecificStore.vue')
  },
  {
    path: '/Configure',
    name: 'configure',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloConfigure.vue')
  },
  {
    path: '/ImageUpload',
    name: 'imageUpload',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloImageUpload.vue')
  },
  {
    path: '/PlafondChange',
    name: 'plafondChange',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloPlafondChange.vue')
  },
  {
    path: '/Materiais',
    name: 'materiais',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CdrMaterials.vue')
  },
  {
    path: '/brandChange',
    name: 'brandChange',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloBrandChanges.vue')
  },
  {
    path: '/clientHistory',
    name: 'clientHistory',
    meta: { requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientHistory.vue')
  }

]

const router = new VueRouter({
  routes
})

router.beforeEach(async (to,from,next) => {
  //get the authenticated user 
  let authUser = await Backend.isLoggedIn()
  //check sensitive routes
  if (to.meta.requiresAuth) {
      if (!authUser) {
        next('/')
      } else {
        next();
      }
  } else {
      next();
  }
})

export default router
