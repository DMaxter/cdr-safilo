<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="610" width="800" tile style="background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="space-between" align="center" class="mr-1 ml-2">
            <v-col cols="auto">
                        <v-menu
            :offset-x="true"
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                round
                class="white--text mb-1"
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
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" style="width: 400px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Referência do Pedido:
            </v-col>
            <v-col>
              AE3456
            </v-col>
            </v-row>
            </v-card>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 400px; border-radius: 15px">
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
          style="width: 400px; border-radius: 15px"
          rows="2"
          no-resize
          hide-details
          outlined
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
              @click="placeRequest()"
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrder3',

  components: {
  },

  data: () => ({
    collapseOnScroll: true,
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
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>