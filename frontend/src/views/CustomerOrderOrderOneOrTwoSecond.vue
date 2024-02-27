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
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-12 mb-3">
                Medidas (cm)
           </v-row>
           <v-row no-gutters justify="center" align="center" class="mt-4">
           <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pr-2">
            <v-text-field style="width: 120px; border-radius: 0px;"
            v-model="width"
            label="Largura"
            outlined
            dense
          ></v-text-field>
            </v-row>
           </v-col>
            <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pl-2">
            <v-text-field style="width: 120px; border-radius: 0px;"
            v-model="height"
            label="Altura"
            outlined
            dense
          ></v-text-field>
            </v-row>
           </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex mt-2">
            <v-col cols="3">
          <v-select style="border-radius: 0px"
          :items="materials"
          v-model="material"
          label="Material"
          dense
          outlined
          hide-details
          @change="getFinishes(); finish = null"
          ></v-select>
        </v-col>
        <v-col cols="3">

          <v-text-field
            style="border-radius: 0px"
            v-model="quantity"
            label="Quantidade"
            hide-details
            dense
            outlined
          ></v-text-field>
        </v-col>

          </v-row>
          
          <v-row justify="center" align="center" class="d-flex flex-column mt-8">
            <v-col cols="8" class="d-flex">
              <v-select style="border-radius: 0px"
              class="mr-3"
          :items="mandatoryFinishes"
          v-model="finish"
          label="Acabamentos obrigatórios"
          dense
          multiple
          outlined
          hide-details
          ></v-select>
          <v-select style="border-radius: 0px"
          class="ml-3"
          :items="aditionalFinishes"
          v-model="finish"
          label="Acabamentos adicionais"
          dense
          multiple
          outlined
          hide-details
          ></v-select>
            </v-col>
          <v-checkbox
            v-model="checkbox"
            hide-details
            dense
            :label="`Aplicação?`"
          ></v-checkbox>
          </v-row>
           <v-row justify="center" align="center" class="d-flex mt-8">
<v-dialog
      v-model="dialog"
      persistent
      max-width="800"
    >
      <v-card>
        <v-card-title class="text-h5">
          Selecionar uma imagem
        </v-card-title>

        <v-card-text style="height: 400px;">
          <v-item-group active-class="selectedOne" v-model="picked">
    <v-container>
      <v-row>
        <v-col
          v-for="(image, i) in images"
          :key="i"
          cols="3"
        >
          <v-item v-slot="{ active, toggle }">
            <v-img
              :src= image.link
              height="150"
              @click="toggle"
            >
            <div
                  v-if="active"
                  class="selectedOne"
                >
                </div>
            </v-img>
          </v-item>
        </v-col>
      </v-row>
    </v-container>
  </v-item-group>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1"
            text
            @click="dialog = false; picked = []"
          >
            Voltar
          </v-btn>

          <v-btn
            color="green darken-1"
            text
            @click="dialog = false; getFace()"
          >
            Confirmar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
            <v-col cols="auto">
            <v-img :src="myImage" contain height="80px" width="80px" @click.stop="loadImages(); dialog = true"></v-img>
            </v-col>
            <v-col cols=3> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Face 2
                </v-col>
                <v-col cols="auto" class="text-decoration-underline">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="2">
            </v-col>
           </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 64px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click = "store.backtracking = true; $router.push('order2')"
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
              @click="nextScreen()"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
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
  name: 'CustomerOrder22',

  components: {
  },

  data: () => ({
    store,
    myImage2: require('@/assets/logologo1.png'),
    allMaterials: null,
    allBrands: null,
    checkbox: false,
    width: null,
    height: null,
    quantity: null,
    brand: null,
    material: null,
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    materials: [],
    brands: [],
    myImage: require('@/assets/default-placeholder.png'),
    dialog: false,
    ex4: false,
    picked: null,
    images: [
    ],
    mandatoryFinishes: [],
    aditionalFinishes: [],
    allFinishes: [],
    allFinishGroup: [],
    finish: null

  }),
    methods: {
      nextScreen () {
        var cost = 0;
        this.allMaterials.forEach(element => {
          if(element.name == this.material){
            cost = element.cost
            store.selectedMaterial.push(element.id)
          }
        });
        var cost2 = (((((this.width/100) * (this.height/100)) * cost) + (this.checkbox ? 500 : 0)) * this.quantity )
        store.currentCost = store.currentCost + (((((this.width/100) * (this.height/100)) * cost) + (this.checkbox ? 500 : 0)) * this.quantity )
        store.dimensions.push({width: this.width, height: this.height})
        var fafa = []
        var finAux = []
        console.log(store.finishesAux)
        store.finishesAux.push(this.finish)
        console.log(this.finish)
        if(this.finish !== null){
        this.finish.forEach(fin => {
          finAux.push(fin.split("*")[0])
        })
        this.allFinishes.forEach(element => {
          if(finAux.includes(element.name))
          fafa.push({id: element.id})
        });
      } else {
          store.finishesAux.push([])
        }
        store.finishes.push(fafa)
        console.log(store.finishes)
        store.pickado2 = this.picked
        this.allBrands.forEach(element => {
          if(element.name == this.brand){
            store.currentBrandId.push(element.id)
          }
        });
        if(!store.uniqueBrands.includes(this.brand)){
            store.uniqueBrands.push(this.brand)
            store.costPerBrand.set(this.brand, cost2)
          } else { 
            store.costPerBrand.set(this.brand, store.currentCost)
          }
          this.$router.push({name: 'order3'});
      },

      getFace () {
        this.myImage = this.images[this.picked].link
        store.face2 = this.myImage
        store.images.push(this.images[this.picked].id)
      },

      getFinishes: async function () {
      try {
        this.aditionalFinishes = []
        this.mandatoryFinishes = []
        var selectedMat = null;
        console.log(store.selectedMaterial)
        this.allMaterials.forEach(element => {
          if(element.name == this.material)
          selectedMat = element
        });
        this.allFinishes = await Backend.getFinishes()
        this.allFinishes.forEach(element => {
          let x = selectedMat.additionalFinishings.find(x => x.id == element.id)
          if(x != undefined && !x.obsolete){
            this.aditionalFinishes.push(element.name)
          }
        });
        this.allFinishGroups = await Backend.getFinishGroups()
        this.allFinishGroups.forEach(element => {
          if(selectedMat.mandatoryFinishings.find(x => x.id == element.id)){
            element.finishings.forEach(element2 => {
              if(!element2.obsolete)
                this.mandatoryFinishes.push(element2.name)
            })
          }
        });
        console.log(this.finishes)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
  getMaterials: async function () {
      try {
        this.allMaterials = await Backend.getMaterials()
        this.allMaterials.forEach(element => {
          this.materials.push(element.name)
        });
        console.log(this.materials)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    getBrands: async function () {
      try {
        this.allBrands = await Backend.getBrands()
        console.log(this.allBrands)
        this.allBrands.forEach(element => {
          this.brands.push(element.name)
        });
        console.log(this.brands)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    loadImages() {
      this.brand = store.currentBrand
      console.log(this.brand)
      this.allBrands.forEach(element => {
          if(this.brand == element.name){
            this.images = element.images
            var filteredImages = []
              this.images.forEach(image => {
                if(image.obsolete == false) 
                  filteredImages.push(image)
              })
              this.images = filteredImages
          }
        });
    }
    },
    created: async function () {
      this.getMaterials()
      this.getBrands()
      if(store.backtracking){
        store.images.pop()
        this.width = store.dimensions[1].width
        this.height = store.dimensions[1].height
        this.quantity = store.quantity
        this.allMaterials = await Backend.getMaterials()
        this.allMaterials.forEach(element => {
          if(element.id == store.selectedMaterial[1]){
            this.material = element.name
          }
        });
        this.getFinishes()
        this.allFinishes = await Backend.getFinishes();
        this.finish = store.finishesAux[1]
        this.picked = store.pickado2
        store.images = [store.images[0]]
        this.loadImages()
        this.getFace()
        store.backtracking = false
      } else if(store.isEditing){
        console.log(store.currentRequest)
        this.brand = store.currentRequest.brand.name
        this.width = store.currentRequest.type.back.measurements.width
        this.height = store.currentRequest.type.back.measurements.height
        this.allMaterials = await Backend.getMaterials()
        var mataux2 = null
        this.allMaterials.forEach(element => {
          if(element.id == store.currentRequest.type.back.material.id){
            this.material = element.name
            mataux2 = element
          }
        });
        console.log(mataux2)
        this.quantity = 1
        this.getFinishes()
        this.allFinishes = await Backend.getFinishes();
        console.log(store.finishesAux)
        if (this.finish == null){
          this.finish = []
        }
        store.currentRequest.type.back.finishings.forEach(f => {
          this.finish.push(f.name)
        })
        this.loadImages()
        var wowie = 0
        this.images.forEach(element => {
          if(element.id == store.currentRequest.type.back.image.id){
            this.picked = wowie
          }
          wowie = wowie + 1
        });
        console.log(this.picked)
        console.log(this.images)
        store.images = [store.images[0]]
        this.getFace()
      }
      store.dimensions = [store.dimensions[0]]
      store.selectedMaterial = [store.selectedMaterial[0]]
      store.finishes = [store.finishes[0]]
      store.finishesAux = [store.finishesAux[0]]
      console.log(store.finishesAux)

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
.selectedOne {
  background: linear-gradient(white, white) padding-box,
              linear-gradient(to right, #fc44b4, #fc44b4) border-box;
  border-radius: 50em;
  border: 3px solid transparent;
  }


</style>
