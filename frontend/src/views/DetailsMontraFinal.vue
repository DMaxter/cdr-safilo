<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="space-between" align="center" class="mr-1 ml-2">
                        <v-menu
            :offset-x="true"
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                round
                class="white--text"
                color="#6e4e5d"
                dark
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" dark dense>
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('profileComercial')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" @click="$router.push('history')">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" height="60" width="170"  @click="$router.push('search')" >
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" class="v-btn--active" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" style="width: 250px; border-radius: 15px">
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
                   <v-img :src=store.pedidoAtual.images[0].src height="40px" width="250.5px"></v-img>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "5" class="pa-0">
                  <v-img :src=store.pedidoAtual.images[2].src height="60px" width="80px"></v-img>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-img :src=store.pedidoAtual.images[3].src height="60px" width="80px"></v-img>
                  </v-col>
                </v-row>
                </v-col>
                <v-col class="pa-0">
                  <v-img :src=store.pedidoAtual.images[1].src height="40px" width="250.5px"></v-img>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="4">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-img :src=store.pedidoAtual.images[4].src height="140px" width="80px"></v-img>
            </v-col>
          </v-row>
          </v-col>
          </v-row>
          <v-col cols="auto" class="mb-5">
          </v-col>
          <v-col cols="auto" >
          <v-row justify="start" align="start" class="d-flex">
          <v-col cols="6">
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" style="width: 200px; border-radius: 15px">
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
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 300px; border-radius: 15px; overflow-y: scroll;">
            {{store.pedidoAtual.observations}}
          </v-card>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 300px; border-radius: 15px">
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
              class="d-flex flex-column"
              outlined
              rounded
              color="#6e4e5d"
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
              class="d-flex flex-column"
              outlined
              rounded
              color="#6e4e5d"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          </v-card>
        </v-col>
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
    store
  }),


};
</script>

<style>
#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>