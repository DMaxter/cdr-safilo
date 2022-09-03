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
          </v-row>
          <v-row justify="space-around" align="center" class="d-flex flex-column mt-0">
           <v-col cols="auto" style="font-size: 1.5em;">
            Selecione o tipo de pedido
           </v-col>
          </v-row>
           <v-row justify="space-around" align="center" class="d-flex flex-column mt-2">
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
          <v-row no-gutters align="center" class="d-flex flex-column mt-3 mb-5">
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
           <v-row no-gutters align="end" justify="end" class="d-flex flex-column pr-4" style="height: 45px;">
           <v-col cols="auto">
            <v-btn
              @click = nextScreen()
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
  name: 'CustomerOrder',

  components: {
  },

  data: () => ({
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
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
.selected {
    background-image: linear-gradient(to top, #F0E68C 0%, transparent 72px);
  }

</style>