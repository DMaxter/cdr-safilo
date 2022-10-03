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

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="space-around" align="center" class="d-flex flex-column mt-0">
           <v-col cols="auto" style="font-size: 1.5em;">
            Selecione o tipo de pedido
           </v-col>
          </v-row>
           <v-row justify="space-around" align="center" class="d-flex flex-column mt-1">
           <v-col cols="auto">
            Pedido normal (1 ou 2 faces)
           </v-col>
           <v-col cols="auto">
            <v-img
              :src = myImage
              height="100"
              width="100"
              contain
              @click="store.isActive1 = !store.isActive1; store.isActive2 = false; store.isActive3 = false"
            > <div class="fill-height" :class="{ selected: store.isActive1 }"> </div> </v-img>
           </v-col>
          </v-row>
          <v-row justify="space-around" align="center" no-gutters style="height: 50px;" class="mt-2">
        <v-col cols = "auto"
          v-for="n in 2"
          :key="n"
        >
        <span align="center"> <v-icon> mdi-circle </v-icon> </span>
        </v-col>
        <v-col cols = "auto">
        Ou
        </v-col>
        <v-col cols = "auto"
          v-for="n in 2"
          :key="n"
        >
        <span align="center"> <v-icon> mdi-circle </v-icon> </span>
        </v-col>
        </v-row>
          <v-row no-gutters align="center" class="d-flex flex-column mt-3 mb-3">
            Pedido de Montra
          </v-row>
           <v-row no-gutters justify="space-around" align="center">
           <v-col cols="6">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column">
            <v-img
              :src = right
              height="100"
              width="200"
              contain 
              @click="store.isActive2 = !store.isActive2; store.isActive1 = false; store.isActive3 = false;"
            > <div class="fill-height" :class="{ selected: store.isActive2 }"> </div> </v-img>
            <span class="pt-5"> imagem à direita </span>
            </v-row>
           </v-col>
            <v-col cols="6">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column">
              <v-img
              :src = left
              height="100"
              width="200"
              contain
              @click="store.isActive3 = !store.isActive3; store.isActive2 = false; store.isActive1 = false"
            > <div class="fill-height" :class="{ selected: store.isActive3 }"> </div> </v-img>
            <span class="pt-5"> imagem à esquerda </span>
            </v-row>
           </v-col>
           </v-row>
           <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mt-4" style="height: 36px">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('orderClient')"
              class="d-flex flex-column customGradient"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
              class="d-flex flex-column customGradient"
              small
              tile
              dark
              @click="nextScreen()"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'

export default {
  name: 'CustomerOrder',

  components: {
  },

  data: () => ({
    myImage2: require('@/assets/logologo1.png'),
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    myImage: require('@/assets/default-placeholder.png'),
    right: require('@/assets/right.jpg'),
    left: require('@/assets/left.jpg'),
    store
  }),

   methods: {
      nextScreen () {
        if(store.isActive1){
          this.$router.push({name: 'order2'});
        } else if (store.isActive2 || store.isActive3) {
          this.$router.push({name: 'ABC'});
        }
      },
    }
};
</script>

<style>

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
.selected {
    background-image: linear-gradient(to top, #F0E68C 0%, transparent 72px);
  }
  .customGradient {
  background-image: linear-gradient(#616161, grey);
}

</style>