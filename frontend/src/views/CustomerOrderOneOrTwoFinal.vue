<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" align="center" class="ml-2">
            <v-col cols="auto">
              <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                class="white--text customGradient mb-1"
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
          </v-col>
          <v-col cols="auto">
          <v-card elevation="2" color="#FAFAFA" tile height="80" style="width: 280px">
            <v-row justify="center" align="center" class="d-flex flex-column mt-0">
            <v-col cols="auto" class="pa-0 pt-2">
            Créditos atuais por marca:
            </v-col>
            <v-col cols="auto" class="pa-0 pt-1">
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
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" tile class="mt-5" style="width: 400px;">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pa-0 pt-4">
            Créditos a debitar:
            </v-col>
             <v-col cols="auto" class="pa-0 pt-1">
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
          <v-textarea
          class="mt-10" 
          style="width: 400px; border-radius: 0px;"
          rows="3"
          no-resize
          hide-details
          outlined
          name="input-7-4"
          label="Observações sobre o pedido"
          v-model="observacoes"
        ></v-textarea>
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
              <div class="text-h6 pt-12" v-show="added && !store.isEditing"> Pedido efetuado com sucesso! A redirecionar para o Perfil </div>
              <div class="text-h6 pt-12" v-show="added && store.isEditing"> Pedido editado com sucesso! A redirecionar para o Perfil </div>
              <div class="text-h6 pt-12" v-show="failed && !store.isEditing"> Ocorreu um erro a efetuar o pedido. A redirecionar para o Perfil. {{ errorMsg }} </div>
              <div class="text-h6 pt-12" v-show="failed && store.isEditing"> Ocorreu um erro a editar o pedido. A redirecionar para o Perfil. {{ errorMsg }} </div>
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
              <div>Altura: {{ request.type.cover.measurements.width }}</div>
              <div>Largura: {{ request.type.cover.measurements.height }}</div>
              <div>Marca: {{ currBrand }}</div>
              <div>Acabamentos: {{ store.finishesAux[0] }}</div>
              <template v-if=!oneFace>
                <div class="text-h6"> Face 2: </div>
              <div> Material: {{ materiales[1] }} </div>
              <div>Altura: {{ request.type.back.measurements.width }}</div>
              <div>Largura: {{ request.type.back.measurements.height }}</div>
              <div>Marca: {{ currBrand }}</div>
              <div>Acabamentos: {{ store.finishesAux[1] }}</div>
              </template>
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
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 62px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="store.backtracking = true; $router.go(-1)"
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrder3',

  components: {
  },

  data: () => ({
    dialog2: false,
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
  dialog: false,
  added: false,
  failed: false,
  brandsWithPlafonds: [],
  brandCost: [],
  totalPrice: null,
  allMaterials: null,
  allBrands: null,
  request: null,
  materiales: [],
  currBrand: null,
  allFinishes: null,
  currFinishes: [],
  oneFace: false,
  errorMsg: ""

  }),

  async created () {
    await this.getRequestPrice()
    if (store.face2 == null){
      this.facetas = [store.face1]
      this.oneFace = true
    } else {
      this.facetas = [store.face1, store.face2]
      this.oneFace = false
    }
    console.log(this.totalPrice)
    this.allMaterials = await Backend.getMaterials()
    this.allMaterials.forEach(material => {
      console.log(this.request)
      if(material.id == this.request.type.cover.material.id){
        this.materiales.push(material.name)
      }
      if(!this.oneFace){
        if(material.id == this.request.type.back.material.id){
          this.materiales.push(material.name)
        } 
      }
    })
    console.log(this.materiales)
    this.allBrands = await Backend.getBrands()
    this.currBrand = this.allBrands.find(x => x.id == store.currentBrandId[0]).name
    this.allFinishes = await Backend.getFinishes()
    console.log(store.finishesAux)
    this.currFinishes = ""
    this.allFinishes.forEach(fin => {
      if(this.oneFace){
        store.finishes[0].forEach(f => {
        console.log(f)
        console.log(fin)
        if(fin.id == f.id){
        this.currFinishes = this.currFinishes.concat(fin.name + "; ")
      }
      })
      } else {
        this.currFinishes = []
        for(var i = 0; i < store.finishes.length; i++){
        var auxstr = ""
        store.finishes[i].forEach(f => {
        console.log(f)
        console.log(fin)
        if(fin.id == f.id){
        auxstr = auxstr.concat(fin.name + "; ")
      }
      })
      this.currFinishes.push(auxstr)
    }
      }
    })
    if(this.oneFace){
      this.currFinishes = [this.currFinishes]
    }
    console.log(this.currFinishes)

    var currentUser = await Backend.getProfile()
    currentUser.credits.forEach(element => {
      if(store.uniqueBrands.includes(element.brand)){
        this.brandsWithPlafonds.push(element.brand + ": " + element.amount.toFixed(2))
      }
    });
    this.brandCost = [store.uniqueBrands[0] +" : "+ this.totalPrice.toFixed(2)]
    store.currentCost = this.totalPrice
    this.observacoes = store.currentRequest.observations
  },

  methods: {
    async placeRequest() {
      this.request = null
      if(store.face2 == null){
      this.request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: this.observacoes,
        application: store.application,
        brand: {
              id: store.currentBrandId[0],
            },
        type: {
          type: "OneFace",
          cover: {
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
          }
        },
      }
    } else { 
      this.request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: this.observacoes,
        application: store.application,
        brand: {
              id: store.currentBrandId[0],
            },
        type: {
          type: "TwoFaces",
          cover: {
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
          back: {
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
          }
        },
      }
    }
      console.log(this.request)
      try{
        if(store.isEditing){
          await Backend.editRequest(store.currentRequest.id, this.request)
        }else{
          await Backend.placeRequest(this.request)
        }
        this.added = true
        setTimeout(() => this.$router.push({name: 'profile'}), 4000);
      } catch (error) {
        this.errorMsg = error
        setTimeout(() => this.$router.push({name: 'profile'}), 4000);
        this.failed = true
      }
      setTimeout(() => store.isEditing = false, 4000);
    },
    async getRequestPrice() {
      this.request = null
      if(store.face2 == null){
      this.request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: this.observacoes,
        application: store.application,
        brand: {
              id: store.currentBrandId[0],
            },
        type: {
          type: "OneFace",
          cover: {
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
          }
        },
      }
    } else { 
      this.request = {
        clientId: store.currentClient,
        amount: store.quantity,
        observations: this.observacoes,
        application: store.application,
        brand: {
              id: store.currentBrandId[0],
            },
        type: {
          type: "TwoFaces",
          cover: {
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
          back: {
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
          }
        },
      }
    }
      console.log(this.request)
      try{
        this.totalPrice = await Backend.getRequestPrice(this.request)
        console.log(this.totalPrice)
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
</style>
