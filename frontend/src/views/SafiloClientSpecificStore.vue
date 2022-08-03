<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="640" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
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

              <v-btn-toggle v-model="icon" dark dense mandatory>
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('safiloProfile')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column">
          <h3> Código - Loja do Cliente</h3>
          <h3 class="mb-2"> Portfólio </h3>
          <v-col>
          <v-row justify="center" align="center" class="d-flex flex-column">
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5"></v-avatar>
          <v-col>
          Código
          <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500 </v-main> 
          </v-col>
          </v-col>
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5"></v-avatar>
          <v-col>
          Nome
          <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500 </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5"></v-avatar>
          <v-col>
          Morada
          <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500 </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5"></v-avatar>
          <v-col>
          Telefone
          <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500 </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5"></v-avatar>
          <v-col>
          Imagem
          <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500 </v-main> 
          </v-col>
          </v-col>        
          </v-row>
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