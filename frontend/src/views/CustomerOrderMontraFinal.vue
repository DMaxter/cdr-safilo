<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" tile style="background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="space-between" align="center" class="ml-2">
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
          <v-card elevation="2" tile outlined color="#FAFAFA" height="80" style="width: 260px;">
            <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto" class="pa-0">
            Créditos atuais para por marca:
            </v-col>
            <v-col cols="auto" class="pa-0">
              <v-slide-group
                multiple
                show-arrows
              >
                <v-slide-item
                  v-for="n in brandsWithPlafonds.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    dense
                    @click="toggle"
                  >
                    {{brandsWithPlafonds[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
            </v-col>
            </v-row>
            </v-card>
          </v-row> 
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-5">
            <v-row class="mt-1 mb-2" justify="center">
            <template v-if="store.isActive2">
            <v-col cols = "8">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                   <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[0].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[0].link height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "5" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[2].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[2].link height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[3].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[3].link height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                </v-row>
                </v-col>
                <v-col class="pa-0">
                 <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[1].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[1].link height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>                
                  </v-col>
              </v-row>
            </v-col>
            <v-col cols="4">
            <v-row class="ml-1">
            <v-col class="pa-0">
               <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[4].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[4].link height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
            </v-col>
          </v-row>
          </v-col>
          </template>
          <template v-else>
            <v-col cols="4">
            <v-row class="ml-1">
            <v-col class="pa-0">
               <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[4].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[4].link height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
            </v-col>
          </v-row>
          </v-col>
            <v-col cols = "8">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                   <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[0].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[0].link height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "5" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[2].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[2].link height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[3].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[3].link height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>                  </v-col>
                </v-row>
                </v-col>
                <v-col class="pa-0">
                   <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[1].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[1].link height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
              </v-row>
            </v-col>
          </template>
          </v-row>
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
            Créditos a debitar: {{store.currentCost}} <v-icon>mdi-currency-eur</v-icon>
            </v-col>
             <v-col cols="auto" class="pa-0">
              <v-slide-group
                multiple
                show-arrows
              >
                <v-slide-item
                  v-for="n in brandsWithPlafonds.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    dense
                    @click="toggle"
                  >
                    {{brandCost[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
            </v-col>
            </v-row>
            </v-card>
          </v-col>

          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 140px;">
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
import Backend from "@/router/backend";

export default {
  name: 'CustomerOrderFinalABC',

  components: {
  },

  data: () => ({
    store,
    brandsWithPlafonds: [],
    brandCost: []
  }),
  
  async created () {
    var currentUser = await Backend.getProfile()
    currentUser.credits.forEach(element => {
      if(store.uniqueBrands.includes(element.brand)){
        this.brandsWithPlafonds.push(element.brand + ": " + element.amount)
      }
    });
    var brandCosts = []
    store.costPerBrand.forEach(function(value, key) {
      console.log(key,value)  
      brandCosts.push(key + ": " + value)
      })
      console.log(brandCosts)
      this.brandCost = brandCosts
  },

  methods: {
    async placeRequest() {
      var request = null
      if(store.isActive2){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        addressId: store.currentAddress,
        observations: store.observations,
        application: store.application,
        type: {
          type: "RightShowcase",
          top: {
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
          bottom: {
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
          },
          left: {
            brand: {
              id: store.currentBrandId[2],
            },
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            }
          },
          right: {
            brand: {
              id: store.currentBrandId[3],
            },
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            }
          },
          side: {
            brand: {
              id: store.currentBrandId[4],
            },
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
            }
          }
        },
      }
    } else if(store.isActive3) { 
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        addressId: store.currentAddress,
        observations: store.observations,
        application: store.application,
        type: {
          type: "LeftShowcase",
          top: {
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
          bottom: {
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
          },
          left: {
            brand: {
              id: store.currentBrandId[2],
            },
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            }
          },
          right: {
            brand: {
              id: store.currentBrandId[3],
            },
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            }
          },
          side: {
            brand: {
              id: store.currentBrandId[4],
            },
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
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