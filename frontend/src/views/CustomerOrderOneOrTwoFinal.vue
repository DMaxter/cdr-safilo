<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="610" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" align="center" class="mr-1 ml-2">
            <v-col cols="auto">
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
              <v-btn color="#808080" value="left" height="64" width="170" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="170" class="customGradient">
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

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-col>
          <v-col cols="auto">
          <v-card elevation="2" color="#FAFAFA" tile height="80" style="width: 300px">
            <v-row justify="center" align="center" class="d-flex flex-column mt-1">
            <v-col cols="auto" class="pa-3">
            Créditos atuais para marca {{store.currentBrand}}:
            </v-col>
            <v-col cols="auto" class="pa-0">
              3000 <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
            </v-col>
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
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile style="width: 400px;">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Referência do Pedido:
            </v-col>
            <v-col>
              AE3456
            </v-col>
            </v-row>
            </v-card>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 400px;">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Créditos a debitar
            </v-col>
             <v-col cols="auto">
              {{store.currentCost}} <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
          <v-textarea
          class="mt-5" 
          style="width: 400px;"
          rows="2"
          no-resize
          hide-details
          outlined
          tile
          name="input-7-4"
          label="Observações sobre o pedido"
          value=""
          v-model="observacoes"
        ></v-textarea>
          </v-col>

          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 35px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('order2')"
              class="d-flex flex-column customGradient"
              small
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
            class="d-flex flex-column customGradient"
              small
              dark
              @click="placeRequest()"
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
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
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
  observacoes: "",
  }),

  created () {
    if (store.face2 == null){
      this.facetas = [store.face1]
    } else {
      this.facetas = [store.face1, store.face2]
    }
  },

  methods: {
    async placeRequest() {
      var request = null
      if(store.face2 == null){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        addressId: store.currentAddress,
        observations: this.observacoes,
        application: store.application,
        type: {
          type: "OneFace",
          cover: {
            brand: {
              id: store.currentBrandId[0],
            },
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            }
          }
        },
      }
    } else { 
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        addressId: store.currentAddress,
        observations: this.observacoes,
        application: store.application,
        type: {
          type: "TwoFaces",
          cover: {
            brand: {
              id: store.currentBrandId[0],
            },
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            }
          },
          back: {
            brand: {
              id: store.currentBrandId[1],
            },
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            }
          }
        },
      }
    }
      console.log(request)
      await Backend.placeRequest(request)
    },
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