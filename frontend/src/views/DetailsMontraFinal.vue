<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" align="center" class="mr-1 ml-2">
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
              <v-btn color="#808080" value="left" height="64" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" tile style="width: 250px;">
            <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto" class="pa-0">
            Referência do pedido: 
            </v-col>
            <v-col cols="auto" class="pa-0">
              {{store.pedidoAtual.cod}}
            </v-col>
            </v-row>
            </v-card>
          </v-row> 
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-5">
            <v-row class="mt-1 mb-2" justify="center">
            <v-col cols = "8">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                  <v-tooltip bottom color="white" content-class="custom-tooltip">
                  <v-img :src=store.pedidoAtual.images[0] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[0] height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "5" class="pa-0">
                  <v-tooltip bottom color="white" content-class="custom-tooltip">
                  <v-img :src=store.pedidoAtual.images[2] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[2] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip bottom color="white" content-class="custom-tooltip">
                  <v-img :src=store.pedidoAtual.images[3] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[3] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                </v-row>
                </v-col>
                <v-col class="pa-0">
                  <v-tooltip bottom color="white" content-class="custom-tooltip">
                  <v-img :src=store.pedidoAtual.images[1] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[1] height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="4">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-tooltip bottom color="white" content-class="custom-tooltip">
                  <v-img :src=store.pedidoAtual.images[4] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[4] height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
            </v-col>
          </v-row>
          </v-col>
          </v-row>
          <v-col cols="auto" class="mb-5">
          </v-col>
          <v-col cols="auto" >
          <v-row justify="start" align="start" class="d-flex">
          <v-col cols="6">
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile style="width: 200px;">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Quantidade:
            </v-col>
            <v-col>
              {{store.pedidoAtual.quantity}}
            </v-col>
            </v-row>
            </v-card>
            </v-col>
            <v-col cols="3">
              <v-checkbox
            v-model= store.pedidoAtual.application 
            disabled
            hide-details
            :label="`Aplicação?`"
            ></v-checkbox>
            </v-col>
          </v-row>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile class="mt-5" style="width: 300px; overflow-y: scroll;">
            {{store.pedidoAtual.observations}}
          </v-card>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile class="mt-5" style="width: 300px;">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Créditos Totais
            </v-col>
             <v-col cols="auto">
              {{store.pedidoAtual.cost}} <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
          </v-col>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 40px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('ABCfinal')"
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
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'

export default {
  name: 'CustomerOrderFinalABC',

  components: {
  },

  data: () => ({
    store,
    myImage2: require('@/assets/logologo1.png'),

  }),


};
</script>

<style>
  .custom-tooltip {
    opacity: 1!important;
}
#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
.customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>