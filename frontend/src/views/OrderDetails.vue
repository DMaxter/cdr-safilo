<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
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
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('profile')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" class="v-btn--active">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('order')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 470px">
          <h3> PEDIDO {{store.pedidoAtual.cod}}</h3>
            <v-card elevation="12" color="#FAFAFA" height="400" width="400">
              <v-row justify="space-between" align="start" class="d-flex flex-column fill-height mt-0 ml-4">
              <v-col cols="auto"> Data:  {{store.pedidoAtual.data}}</v-col>
              <v-col cols="auto"> Marca: {{store.pedidoAtual.marca}} </v-col>
              <v-col cols="auto"> Modelo: {{store.pedidoAtual.modelo}} </v-col>
              <v-col cols="auto"> Material: {{store.pedidoAtual.material}} </v-col>
              <v-col cols="auto"> Dimensões: {{store.pedidoAtual.dimensoes}} </v-col>
              <v-col cols="auto"> Estado: {{store.pedidoAtual.estado}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row no-gutters justify="end" class="mr-9">
            <v-btn @click="getPedido()">
              Ver Pedido
            </v-btn>
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
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        store,
      }
    },

methods: {
  getPedido() {
    if(store.pedidoAtual.modelo == "montra"){
      this.$router.push({name: 'detailsMontra'});
    } else {
      this.$router.push({name: 'detailsOneOrTwo'});
    }
  }
}
  }
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>