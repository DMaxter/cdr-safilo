<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="650" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
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
              <v-btn color="#6e4e5d" value="left" height="64" width="170" @click="$router.push('profileSafilo')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column">
          <h3> {{store.currentClient.id}}- Loja do Cliente</h3>
          <h3 class="mb-2"> Portfólio </h3>
          <v-col>
          <v-row justify="center" align="center" class="d-flex flex-column">
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5 customGradient"></v-avatar>
          <v-col>
          Código
          <v-main class="text-no-wrap" style="background-color: #cfcfcf;" align="center"> {{store.clientAddress.id}} </v-main> 
          </v-col>
          </v-col>
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5 customGradient"></v-avatar>
          <v-col>
          Nome
          <v-main class="text-no-wrap" style="background-color: #cfcfcf;" align="center"> {{store.currentClient.name}} </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5 customGradient"></v-avatar>
          <v-col>
          Morada
          <v-main class="text-no-wrap " style="background-color: #cfcfcf;" align="center"> {{store.clientAddress.address}} </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5 customGradient"></v-avatar>
          <v-col>
          Telefone
          <v-main class="text-no-wrap " style="background-color: #cfcfcf;" align="center"> {{store.currentClient.phone}} </v-main> 
          </v-col>
          </v-col>    
          <v-col class="d-flex" cols = 4>
          <v-avatar color="#6e4e5d" size="30" class="d-flex mt-5 customGradient"></v-avatar>
          <v-col>
          Imagem
          <v-main class="text-no-wrap " style="background-color: #cfcfcf;" align="center"> 500 </v-main> 
          </v-col>
          </v-col>        
          </v-row>
          </v-col>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profileSafilo')"
              class="d-flex flex-column customGradient"
              tile
              small
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
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
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
        myImage: require('@/assets/logologo1.png'),
      }
    },
created() {
  console.log(store.clientAddress)
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
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

</style>