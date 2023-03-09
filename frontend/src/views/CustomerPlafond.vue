<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
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

              <v-btn-toggle v-model="icon" tile dark mandatory borderless>
              <v-btn color="#808080" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
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
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mb-2 mt-3">
              <v-avatar class="customGradient mb-6" size="100">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              {{profile}}
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-4">
                <v-data-table
                  height = "280px"
                  fixed-header
                  :headers="headers"
                  :items="desserts"
                  hide-default-footer
                  disable-pagination
                  class="elevation-1 my-header-style"
                ></v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 80px;">
           <v-col cols="auto" class="pl-3">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient mb-2"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            </v-col>
           </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import Backend from "@/router/backend";
import { store } from '@/store.js'
export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        headers: [
          { text: 'MARCA', value: 'marca', width: '200px', align: 'left', class: 'my-header-style' },
          { text: 'PLAFOND', value: 'plafond', width: '200px', align: 'left', class: 'my-header-style' },
        ],
        desserts: [
        
        ],
        store,
        profile: null,
      }
    },
  async created () {
    if(store.currentUser == null){
        store.currentUser = await Backend.getProfile()
      }
      this.profile = store.currentUser.name
    store.currentUser.credits.forEach(element => {
      this.desserts.push({ marca: element.brand, plafond: element.amount.toFixed(2)})
    });
  }
  }
</script>

<style>
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}
</style>