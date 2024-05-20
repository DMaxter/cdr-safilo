<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">          
          <v-row no-gutters justify="space-between" align="center" class="mr-1 ml-2 pt-2">
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
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="160" class="customGradient">
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

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
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
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('notes')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Notas</span>

                <v-icon right>
                  mdi-note-multiple-outline
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
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="customGradient">
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

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
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
          </v-row> 
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-5">
            <v-carousel hide-delimiters :continuous=false height="175" style="width: 400px;">
              <v-carousel-item
                v-for="(item,i) in facetas"
                :key="i"
                :src="item"
                contain
                ></v-carousel-item>
            </v-carousel>
          <v-col cols="auto" class="mb-5">
          </v-col>
          <v-col cols="auto" >
          <v-card tile elevation="2" outlined color="#FAFAFA" height="80" width="200" style="width: 400px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Referência do Pedido: {{store.pedidoAtual.cod}}
            </v-col>
            <v-col class="pt-4">
            Quantidade: {{store.pedidoAtual.amount}}
            </v-col>
            </v-row>
            </v-card>
          <v-card tile elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 400px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Créditos Totais
            </v-col>
             <v-col cols="auto">
              {{store.pedidoAtual.cost}} <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
          <v-textarea
          class="mt-5" 
          tile
          style="width: 400px; pointer-events: none;"
          rows="2"
          no-resize
          hide-details
          outlined
          name="input-7-4"
          label="Observações sobre o pedido"
          :value= store.pedidoAtual.observations
        ></v-textarea>
          </v-col>

          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 38px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.go(-1)"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
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
      <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrder3',

  components: {
  },

  data: () => ({
    collapseOnScroll: true,
    myImage2: require('@/assets/logologo1.png'),
    items: ['Novo','Em Produção'],
    pics: [
    {
      src: require('@/assets/default-placeholder.png')
    },
    {
      src: require('@/assets/default-placeholder.png')
    }
  ],
  store,
  facetas: null,
  menu1: false,
  menu2: false,
  menu3: false
  }),

  async created () {
    this.facetas = store.pedidoAtual.images
    var profile = await Backend.getProfile()
    if(profile.roles[0] == 'COMMERCIAL' || profile.roles[0] == 'ADMIN'){ 
      this.menu1 = true
    } else if(profile.roles[0] == 'CDR'){ 
      this.menu2 = true
    } else if(profile.roles[0] == 'MANAGER'){
      this.menu3 = true
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
</style>
