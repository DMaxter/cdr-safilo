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
          <v-row justify="center" align="center" class="d-flex flex-column mt-0">
            <v-col cols="auto" >
          <v-select style="width: 257px; border-radius: 0px;"
          :items="brands"
          v-model="brand"
          label="Marca"
          dense
          outlined
          hide-details
          v-on:change="loadImages"
          ></v-select>
          </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-5 mb-3">
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
          @change="finish = null; getFinishes();"
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
      scrollable
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
            <v-img :src="myImage" contain height="80px" width="80px" @click.stop="dialog = true"></v-img>
            </v-col>
            <v-col cols=3> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Face 1
                </v-col>
                <v-col cols="auto" class="text-decoration-underline" @click="dialog=true">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="2">
              <v-checkbox
              class="pb-6"
              v-model="ex4"
              label="Face 2?"
              hide-details
            ></v-checkbox>
            </v-col>
           </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 40px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click = "$router.push('order')"
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
              @click = nextScreen()
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
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrder2',

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
    allFinishes: null,
    allFinishGroups: null,
    aditionalFinishes: [],
    mandatoryFinishes: [],
    finish: null

  }),
    methods: {
      nextScreen () {
        var uniqueBrands = []
        var costPerBrand = new Map()
        this.allBrands.forEach(element => {
          if(element.name == this.brand){
            store.currentBrandId.push(element.id)
          }
          if(!uniqueBrands.includes(this.brand)){
            uniqueBrands.push(this.brand)
            costPerBrand.set(this.brand, 0)
          }
        });
        var cost = 0;
        this.allMaterials.forEach(element => {
          if(element.name == this.material){
            cost = element.cost
            store.selectedMaterial.push(element.id)
          }
        });
        store.currentCost = (((((this.width/100) * (this.height/100)) * cost) + (this.checkbox ? 500 : 0)) * this.quantity )
        costPerBrand.set(this.brand, store.currentCost)
        store.dimensions.push({width: this.width, height: this.height})
        store.quantity = this.quantity
        store.currentBrand = this.brand
        store.application = this.checkbox
        store.uniqueBrands = uniqueBrands
        store.costPerBrand = costPerBrand
        var fafa = []
        var finAux = []
        console.log(this.finish)
        if(this.finish !== null){
          store.finishesAux.push(this.finish)
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
        store.pickado = this.picked
        console.log(store.finishes)
        if(this.ex4){
          this.$router.push({name: 'order22'});
        } else {
          this.$router.push({name: 'order3'});
        }
      },

      getFace () {
        this.myImage = this.images[this.picked].link
        store.face1 = this.myImage
        store.images.push(this.images[this.picked].id)
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
    getFinishes: async function () {
      try {
        this.aditionalFinishes = []
        this.mandatoryFinishes = []
        var selectedMat = null;
        this.allMaterials.forEach(element => {
          if(element.name == this.material)
          selectedMat = element
        });
        this.allFinishes = await Backend.getFinishes()
        this.allFinishes.forEach(element => {
          if(selectedMat.additionalFinishings.find(x => x.id == element.id))
          this.aditionalFinishes.push(element.name)
        });
        this.allFinishGroups = await Backend.getFinishGroups()
        this.allFinishGroups.forEach(element => {
          if(selectedMat.mandatoryFinishings.find(x => x.id == element.id)){
            element.finishings.forEach(element2 => {
              this.mandatoryFinishes.push(element2.name)
            })
          }
        });
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
        this.brand = store.uniqueBrands[0]
        this.width = store.dimensions[0].width
        this.height = store.dimensions[0].height
        this.allMaterials = await Backend.getMaterials()
        var mataux = null
        this.allMaterials.forEach(element => {
          if(element.id == store.selectedMaterial[0]){
            this.material = element.name
            mataux = element
          }
        });
        console.log(mataux)
        this.quantity = store.quantity
        this.getFinishes()
        this.allFinishes = await Backend.getFinishes();
        console.log(store.finishesAux)
        this.finish = store.finishesAux[0]
        this.picked = store.pickado
        this.loadImages()
        console.log(this.picked)
        console.log(this.images)
        store.images = []
        this.getFace()
        store.backtracking = false
      } else {
        store.images = []
        store.face1 = null
      }
      store.finishesAux = []
      store.selectedMaterial = []
      store.currentBrandId = []
      store.dimensions = []
      store.isActive1 = false
      store.isActive2 = false
      store.isActive3 = false
      store.face2 = null
      store.address = null
      store.uniqueBrands = null
      store.quantity = null
      store.observations = null
      store.currentCost = null
      store.application = false
      store.costPerBrand = null
      store.finishes = []
      store.pedidoAtual = {
        cod: null,
        data: null,
        marca: null,
        modelo: null,
        material: null,
        dimensoes: null,
        estado: null,
        images: null,
        quantity: null,
        observations: null,
        cost: null,
        application: null
      },
        store.facesDefault = [
          {
            face: "A",
            link: require('@/assets/A.png'),
          },
          {
            face: "B",
            link: require('@/assets/B.png'),
          },
          {
            face: "C",
            link: require('@/assets/C.png'),
          },
          {
            face: "D",
            link: require('@/assets/D.png'),
          },
          {
            face: "E",
            link: require('@/assets/E.png'),
          },
        ]
    }
};

</script>

<style>

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.selectedOne {
  background: linear-gradient(white, white) padding-box,
              linear-gradient(to right, #fc44b4, #fc44b4) border-box;
  border-radius: 50em;
  border: 3px solid transparent;
  }

  .customGradient {
  background-image: linear-gradient(#616161, grey);
}

</style>