<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" class="pt-2 pl-2 pr-2">
            <template v-if="this.menu1">
            <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                class="white--text customGradient"
                tile
                color="#808080"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="160" class="v-btn--active customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu2">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" dark borderless tile>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="v-btn--active customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu3">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="v-btn--active customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
          <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#808080"
                @click="logout()"
              >
                Logout
              </v-btn>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-10 mt-3">
              <v-avatar color="#808080" size="100" class="mb-6 customGradient">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              {{ store.currentUser.name }}
            </v-row>
             <v-row justify="left" align="left" class="d-flex ml-4">
              <v-col cols = 1>
                <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="50" dark class="d-flex"> mdi-currency-usd </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 2 class="d-flex flex-column ml-10" align="left"> Plafond Geral
                <v-main class="text-no-wrap" align="center" style="background-color: #cfcfcf;"> {{ this.credits }}
                <v-icon size="15"> mdi-currency-eur </v-icon> </v-main> </v-col>
              <v-col cols = 1 align="left" class>
                <template v-if="this.role">
                 <v-avatar color="#808080" size="20" class="d-flex customGradient2"  @click = "$router.push('plafond')">
                  <v-icon size="15" dark class="d-flex"> mdi-help </v-icon>
                </v-avatar>
              </template>
              </v-col>
             </v-row>
             <v-row justify="left" align="left" class="d-flex ml-4 pt-7">
              <v-col cols = 1>
                <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="30" dark class="d-flex"> mdi-email </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 4 class="d-flex flex-column ml-10" align="left"> Email
                <v-main class="text-no-wrap" align="center" style="background-color: #cfcfcf;"> {{ store.currentUser.email }} </v-main>
            </v-col>
             </v-row>
             <v-row justify="left" align="left" class="d-flex ml-4 pt-7">
              <v-col cols = 1>
                <v-avatar color="#808080" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="35" dark class="d-flex"> mdi-lock </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 3 class="d-flex flex-column ml-10" align="left"> Password
                <v-main class="text-no-wrap" width="" align="center" style="background-color: #cfcfcf;"> ****** </v-main>
            </v-col>
            <v-btn class="d-flex mt-9 white--text customGradient" tile height="25" elevation="2"  @click="$router.push('passwordChange')"> Alterar
              <v-icon class="pl-1" size="15" dark> mdi-cog-outline </v-icon>
            </v-btn>
             </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import Backend from "@/router/backend";
import { store } from '@/store.js';

export default {
  name: 'CustomerProfile',

  components: {
  },

  data: () => ({
    myImage: require('@/assets/logologo1.png'),
    credits: null,
    store,
    profile: null,
    role: false,
    menu1: false,
    menu2: false,
    menu3: false,

  }),

  created: async function() {
      store.currentUser = await Backend.getProfile()
      if(store.currentUser.roles[0] == 'COMMERCIAL' || store.currentUser.roles[0] == 'ADMIN'){ 
        this.role = true
      }
      if(store.currentUser.roles[0] == 'COMMERCIAL' || store.currentUser.roles[0] == 'ADMIN'){ 
        this.menu1 = true
      } else if(store.currentUser.roles[0] == 'CDR'){ 
        this.menu2 = true
      } else if(store.currentUser.roles[0] == 'MANAGER'){
        this.menu3 = true
      }
      this.credits = 0
      store.currentUser.credits.forEach(element => {
      this.credits += element.amount
    });
      this.credits = this.credits.toFixed(2)
  },

  methods: {
    async logout() {
     await Backend.logout()
     this.$router.push("/")
    }
  }
};
</script>

<style>

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

.customGradient2 {
  background-image: linear-gradient(#616161, grey);
  cursor:pointer;
}
</style>