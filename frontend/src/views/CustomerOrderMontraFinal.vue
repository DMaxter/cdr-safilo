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
          <template v-else-if="store.isActive3">
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
          <template v-else-if="store.isActive4">
            <v-col cols = "11">
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
                  <v-img :src=store.facesDefault[3].link></v-img>
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
              <div class="text-h6 pt-12" v-show="failed"> Ocorreu um erro a efetuar o pedido. A redirecionar para o Perfil. {{ errorMsg }} </div>
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
      <v-dialog
        v-model="dialog2"
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template>
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12"> Resumo do pedido: </div>
              <div class="text-h6"> Face 1: </div>
              <div> Material: {{ materiales[0] }} </div>
              <div>Altura: {{ store.dimensions[0].height }}</div>
              <div>Largura: {{ store.dimensions[0].width }}</div>
              <div>Marca: {{ currBrand }} </div>
              <div>Acabamentos: {{ currFinishes[0] }}</div>
              <div class="text-h6"> Face 2: </div>
              <div> Material:  {{ materiales[1] }}  </div>
              <div>Altura: {{ store.dimensions[1].height }}</div>
              <div>Largura: {{ store.dimensions[1].width }}</div>
              <div>Marca: {{ currBrand }}</div>
              <div>Acabamentos: {{ currFinishes[1] }}</div>
              <div class="text-h6"> Face 3: </div>
              <div> Material:  {{ materiales[2] }}  </div>
              <div>Altura: {{ store.dimensions[2].height }}</div>
              <div>Largura: {{ store.dimensions[2].width }}</div>
              <div>Marca: {{ currBrand }}</div>
              <div>Acabamentos: {{ currFinishes[2] }} </div>
              <div class="text-h6"> Face 4: </div>
              <div> Material:  {{ materiales[3] }}  </div>
              <div>Altura: {{ store.dimensions[3].height }}</div>
              <div>Largura: {{ store.dimensions[3].width }}</div>
              <div>Marca: {{ currBrand }}</div>
              <div>Acabamentos: {{ currFinishes[3] }}</div>
              <div class="text-h6" v-if="!store.isActive4"> Face 5: </div>
              <div v-if="!store.isActive4"> Material:  {{ materiales[4] }}   </div>
              <div v-if="!store.isActive4">Altura: {{ store.dimensions[4].height }}</div>
              <div v-if="!store.isActive4">Largura: {{ store.dimensions[4].width }} </div>
              <div v-if="!store.isActive4">Marca: {{ currBrand }}</div>
              <div v-if="!store.isActive4">Acabamentos: {{ currFinishes[4] }}</div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog2 = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 137px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('ABCfinal')"
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
              @click="dialog2 = true"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
            > <v-icon style="transform: rotate(180deg);">mdi-rewind</v-icon>
            Rever Pedido
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
    failed: false,
    allPrices: null,
  allMaterials: null,
  allBrands: null,
  totalPrice: null,
  dialog2: false,
  materiales: [],
  allFinishes: null,
  currBrand: null,
  errorMsg: "",
  currFinishes: []
  }),
  
  async created () {
    await this.getRequestPrice()
    console.log(this.store)
    console.log(this.totalPrice)
    this.allMaterials = await Backend.getMaterials()
    var cena = store.isActive4 ? 4 : 5
    for (var j = 0; j < cena; j++){
      var a = this.allMaterials.filter(y => y.id == store.selectedMaterial[j])[0].name
        this.materiales.push(a)
    }
    console.log(this.materiales)
    this.allFinishes = await Backend.getFinishes()
    this.currFinishes = Array.apply(null, Array(cena)).map(function () {})
    var n = 0
    store.finishes.forEach(fin => {
      var strToUse = ""
      if (fin.length > 1){
        fin.forEach(finit => {
          strToUse = strToUse.concat(finit.name + "; ")
        })
      } else if (fin.length == 1){
        strToUse = fin[0].name + ";"
      } else (
        strToUse = "Nenhum"
      )
      this.currFinishes[n] = strToUse
      n = n+1
    })
  console.log(this.currFinishes)
    this.allBrands = await Backend.getBrands()
    this.allBrands.forEach(brand => {
      if(brand.id == store.currentBrandId[store.currentBrandId.length-1]){
        this.currBrand = brand.name
      }
    })
    var currentUser = await Backend.getProfile()
    console.log(currentUser.credits)
    console.log(store.uniqueBrands)
    currentUser.credits.forEach(element => {
      if(store.uniqueBrands.includes(element.brand)){
        this.brandsWithPlafonds.push(element.brand + ": " + element.amount.toFixed(2))
      }
    });
    var actBrand = this.allBrands.find(x => x.id == store.currentBrandId[store.currentBrandId.length-1]).name
    this.brandCost = [ actBrand + ": " + this.totalPrice]
      store.currentCost = this.totalPrice

  },

  methods: {
    async placeRequest() {
      try{
      var request = null
      if(store.isActive2){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "RightShowcase",
          top: {
            
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
            
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
          side: {
            
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
            },
            finishings: store.finishes[4]
          }
        },
      }
    } else if(store.isActive3) { 
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "LeftShowcase",
          top: {
           
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
           
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
          side: {
           
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
            } ,
            finishings: store.finishes[4]
          }
        },
      }
    } else if(store.isActive4){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "SimpleShowcase",
          top: {
           
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
           
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
        }
      }
    }
      console.log(request)
        await Backend.placeRequest(request)
        this.added = true
        setTimeout(() => this.$router.push({name: 'profile'}), 3000);
      } catch (error) {
        this.errorMsg = error
        setTimeout(() => this.$router.push({name: 'profile'}), 3000);
        this.failed = true
      }
    },
    async getRequestPrice() {
      try{
        var request = null
      if(store.isActive2){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "RightShowcase",
          top: {
            
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
            
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
          side: {
            
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
            },
            finishings: store.finishes[4]
          }
        },
      }
    } else if(store.isActive3) { 
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "LeftShowcase",
          top: {
           
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
           
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
          side: {
           
            image: { 
              id: store.images[4]
            },
            measurements: {
              width: store.dimensions[4].width,
              height: store.dimensions[4].height
            },
            material: { 
              id: store.selectedMaterial[4]
            } ,
            finishings: store.finishes[4]
          }
        },
      }
    } else if(store.isActive4){
      request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: store.observations,
        application: store.application,
        brand: {
              id: store.currentBrandId[store.currentBrandId.length-1],
            },
        type: {
          type: "SimpleShowcase",
          top: {
           
            image: { 
              id: store.images[0]
            },
            measurements: {
              width: store.dimensions[0].width,
              height: store.dimensions[0].height
            },
            material: { 
              id: store.selectedMaterial[0]
            },
            finishings: store.finishes[0]
          },
          bottom: {
           
            image: { 
              id: store.images[1]
            },
            measurements: {
              width: store.dimensions[1].width,
              height: store.dimensions[1].height
            },
            material: { 
              id: store.selectedMaterial[1]
            },
            finishings: store.finishes[1]
          },
          left: {
            
            image: { 
              id: store.images[2]
            },
            measurements: {
              width: store.dimensions[2].width,
              height: store.dimensions[2].height
            },
            material: { 
              id: store.selectedMaterial[2]
            },
            finishings: store.finishes[2]
          },
          right: {
           
            image: { 
              id: store.images[3]
            },
            measurements: {
              width: store.dimensions[3].width,
              height: store.dimensions[3].height
            },
            material: { 
              id: store.selectedMaterial[3]
            },
            finishings: store.finishes[3]
          },
        }
      }
    }
      console.log(request)
        this.totalPrice = await Backend.getRequestPrice(request)
        this.totalPrice = this.totalPrice.toFixed(2)

      } catch (error) {
        console.log(error)
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
