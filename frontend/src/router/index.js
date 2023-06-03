import Vue from 'vue'
import VueRouter from 'vue-router'
import Backend from './backend'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    base: process.env.BASE_URL,
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLogin.vue'),
    meta: { title: process.env.VUE_APP_NAME, requiresAuth: false }
  },
  {
    path: '/profile',
    name: 'profile',
    meta: { title: "Perfil | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerProfile.vue')
  },
  {
    path: '/search',
    name: 'search',
    meta: { title: "Pesquisar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerSearch.vue')
  },
  {
    path: '/order',
    name: 'order',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrder.vue')
  },
  {
    path: '/orderClient',
    name: 'orderClient',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderClient.vue')
  },
  {
    path: '/order2',
    name: 'order2',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOneOrTwo.vue')
  },
  {
    path: '/order3',
    name: 'order3',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOneOrTwoFinal.vue')
  },
  {
    path: '/recoverCode',
    name: 'recoverCode',
    meta: { title: "Recuperar Palavra-passe | " + process.env.VUE_APP_NAME, requiresAuth: false},
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLoginPassword.vue')
  },
  {
    path: '/redefinePassword',
    name: 'redefinePassword',
    meta: { title: "Redefinir Palavra-passe | " + process.env.VUE_APP_NAME, requiresAuth: false },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerLoginPasswordChange.vue')
  },
  {
    path: '/passwordChange',
    name: 'passwordChange',
    meta: { title: "Alterar Palavra-passe | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerProfilePassword.vue')
  },
  {
    path: '/history',
    name: 'history',
    meta: { title: "Histórico | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerHistory.vue')
  },
  {
    path: '/plafond',
    name: 'plafond',
    meta: { title: "Plafond | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerPlafond.vue')
  },
  {
    path: '/ABC',
    name: 'ABC',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontra.vue')
  },
  {
    path: '/order22',
    name: 'order22',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderOrderOneOrTwoSecond.vue')
  },
  {
    path: '/ABCfinal',
    name: 'ABCfinal',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontra2.vue')
  },
  {
    path: '/ABCfinal2',
    name: 'ABCfinal2',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CustomerOrderMontraFinal.vue')
  },
  {
    path: '/Details',
    name: 'details',
    meta: { title: "Detalhes | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/OrderDetails.vue')
  },
  {
    path: '/DetailsMontra',
    name: 'detailsMontra',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsMontra.vue')
  },
  {
    path: '/DetailsMontraFinal',
    name: 'detailsMontraFinal',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsMontraFinal.vue')
  },
  {
    path: '/DetailsOneOrTwo',
    name: 'detailsOneOrTwo',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsOneOrTwo.vue')
  },
  {
    path: '/DetailsOneOrTwoFinal',
    name: 'detailsOneOrTwoFinal',
    meta: { title: "Encomendar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DetailsOneOrTwoFinal.vue')
  },
  {
    path: '/SearchResults',
    name: 'searchResults',
    meta: { title: "Resultados da Pesquisa | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SearchResults.vue')
  },
  {
    path: '/Clients',
    name: 'clients',
    meta: { title: "Clientes | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClients.vue')
  },
  {
    path: '/ClientInfo',
    name: 'clientInfo',
    meta: { title: "Informação de Cliente | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloSpecificClient.vue')
  },
  {
    path: '/ClientStores',
    name: 'clientStores',
    meta: { title: "Lojas do Cliente | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientStores.vue')
  },
  {
    path: '/StoreInfo',
    name: 'storeInfo',
    meta: { title: "Informação de Loja | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientSpecificStore.vue')
  },
  {
    path: '/Configure',
    name: 'configure',
    meta: { title: "Configurar | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloConfigure.vue')
  },
  {
    path: '/ImageUpload',
    name: 'imageUpload',
    meta: { title: "Adicionar Imagem | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloImageUpload.vue')
  },
  {
    path: '/PlafondChange',
    name: 'plafondChange',
    meta: { title: "Atribuir Plafond | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloPlafondChange.vue')
  },
  {
    path: '/Materiais',
    name: 'materiais',
    meta: { title: "Materiais | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CdrMaterials.vue')
  },
  {
    path: '/brandChange',
    name: 'brandChange',
    meta: { title: "Marcas | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloBrandChanges.vue')
  },
  {
    path: '/clientHistory',
    name: 'clientHistory',
    meta: { title: "Histórico do Cliente | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SafiloClientHistory.vue')
  },
  {
    path: '/notes',
    name: 'notas',
    meta: { title: "Notas | " + process.env.VUE_APP_NAME, requiresAuth: true },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/CdrNotes.vue')
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
});

router.afterEach(async (to) => {
    document.title = to.meta.title;
});

export default router
