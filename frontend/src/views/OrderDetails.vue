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
              <v-col cols="auto" class="d-flex"> Estado: {{store.pedidoAtual.estado}}
                <template>
    <v-dialog
      v-model="dialog1"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="d-flex ml-5 customGradient" small dark tile v-bind="attrs" v-on="on"> Alterar 
              <v-icon class="pl-1" size="15" dark> mdi-cog-outline </v-icon>
        </v-btn>  
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Alterar estado do pedido </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-select
                  :items=available
                  required
                  v-model = picked
                ></v-select>
              </v-col>
              <v-col
                cols="12"
              >
              Estado atual: {{store.pedidoAtual.estado}}
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog1 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="updateStatus(); dialog1 = false;"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
              </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row no-gutters justify="end" class="mr-9">
            <v-btn dark class="customGradient" @click="getPedido()">
              Ver Pedido
            </v-btn>
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage2: require('@/assets/logologo1.png'),
        store,
        picked: null,
        dialog1: false,
        available: [
          "IN_PRODUCTION",
          "DONE",
          "CANCELLED"
        ]
      }
    },

methods: {
  getPedido() {
    console.log(store.pedidoAtual)
    if(store.pedidoAtual.modelo == "OneFace" || store.pedidoAtual.modelo == "TwoFaces"){
      this.$router.push({name: 'detailsOneOrTwo'});
    } else {
      this.$router.push({name: 'detailsMontra'});
    }
  },
  async updateStatus() {
    await Backend.updateStatus(store.pedidoAtual.cod, this.picked)
  }
}
  }
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