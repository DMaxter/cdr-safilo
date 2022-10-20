<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" align="center" class="ml-2">
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
              <v-btn color="#808080" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
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
          <v-card elevation="2" tile outlined color="#FAFAFA" height="80" style="width: 280px;">
            <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto" class="pa-0">
            Créditos atuais por marca:
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
                    style="pointer-events: none;"
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
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-15">
            <v-row class="mt-1 mb-2" justify="center">
            <template v-if="store.isActive2">
            <v-col cols = "8">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                   <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
                  max-width = "400px"
                  max-height ="400px"
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
          <v-col cols="auto" class="mt-10">

          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile class="mt-5" style="width: 400px">
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
                    style="pointer-events: none;"
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
          <v-dialog
        v-model="dialog"
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template>
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12" v-show="added"> Pedido efetuado com sucesso! A redirecionar para o Perfil </div>
              <div class="text-h6 pt-12" v-show="failed"> Ocorreu um erro a efetuar o pedido. A redirecionar para o Perfil  </div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 137px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('orderClient')"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
            class="d-flex flex-column customGradient"
              small
              dark
              tile
              @click="added = false; failed = false; placeRequest(); dialog = true"
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
import Backend from "@/router/backend";

export default {
  name: 'CustomerOrderFinalABC',

  components: {
  },

  data: () => ({
    store,
    myImage2: require('@/assets/logologo1.png'),
    brandsWithPlafonds: [],
    brandCost: [],
    dialog: false,
    added: false,
    failed: false
  }),
  
  async created () {
    var currentUser = await Backend.getProfile()
    console.log(currentUser.credits)
    console.log(store.uniqueBrands)
    currentUser.credits.forEach(element => {
      if(store.uniqueBrands.includes(element.brand)){
        this.brandsWithPlafonds.push(element.brand + ": " + element.amount)
      }
    });
    console.log(this.brandsWithPlafonds)
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
      try{
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
        this.added = true
        setTimeout(() => this.$router.push({name: 'profile'}), 3000);
      } catch (error) {
        setTimeout(() => this.$router.push({name: 'profile'}), 3000);
        this.failed = true
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
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

.custom-tooltip {
    opacity: 1!important;
}
</style>