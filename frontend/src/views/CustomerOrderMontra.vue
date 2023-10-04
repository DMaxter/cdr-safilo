<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" tile color="#FAFAFA" :height="$vuetify.breakpoint.height > 670
                ? '650px' 
                : '500px'" width="800" style="border-radius: 15px; background-color: #E0E0E0">
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
          <v-row :class="store.isActive2
                ? 'mt-1 mb-2 mr-16 pr-4' 
                : 'mt-1 mb-2 mr-16 pr-16'" justify="center">
            <template v-if="store.isActive2">
            <v-col cols = "4">
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
                  <v-col cols = "4" class="pa-0">
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
                  <v-img :src=store.facesDefault[1].link></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[1].link height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="2">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  max-width = "400px"
                  max-height ="400px"
                  >
                  <v-img :src=store.facesDefault[4].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[4].link height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
            </v-col>
          </v-row>
          </v-col>
          </template>
          <template v-else-if="store.isActive3">
          <v-col cols="3" align="center">
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
            <v-col cols = "4">
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
                  <v-img :src=store.facesDefault[0].link height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "4" class="pa-0">
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
                  <v-img :src=store.facesDefault[3].link></v-img>
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
                  <v-img :src=store.facesDefault[1].link height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
              </v-row>
            </v-col>
          </template>
          <template v-else-if="store.isActive4">
            <v-col cols = "6" class="ml-14">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0 ml-1">
                   <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  max-width = "400px"
                  max-height ="400px"
                  >
                  <v-img :src=store.facesDefault[0].link ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[0].link height="40px" width="245px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "4" class="pa-0 pl-2">
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
                  <v-col cols = "4" class="pa-0 mr-7">
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
                  <v-img :src=store.facesDefault[1].link height="40px" width="253px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                </v-col>
              </v-row>
            </v-col>
          </template>
          </v-row>
          </v-row>
          <v-row
            align="center"
            justify="center"
            class="d-flex flex-column"
          >
          <v-col cols="4" class="pa-0 pb-2" >
          <v-select
          style="border-radius: 0px;"
          :items="brands"
          label="Marca"
          dense
          hide-details
          outlined
          v-model="brand"
          v-on:change="loadImages()"
        ></v-select>
        </v-col>
          <v-card
            flat
            tile
            outlined
            :width="$vuetify.breakpoint.height > 670
                ? '500px' 
                : '650px'"
            style="background-color: rgba(235,235,238, 0.3);"
          >
    <v-window
      v-model="onboarding"
    >
      <v-window-item
        v-for="n in length"
        :key="`card-${n}`"
      >
          <v-row
            class="fill-height"
            align="center"
            justify="center"
          >
        <v-card
        :height="$vuetify.breakpoint.height > 670
                ? '350px' 
                : '200px'"
          :width="$vuetify.breakpoint.height > 670
                ? '500px' 
                : '650px'"
          outlined
          style="background-color: rgba(235,235,238, 0.4);"
        >
        <template v-if="$vuetify.breakpoint.height > 670">
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto" class="pa-0 pb-3">
              <h2> Face {{faces[n-1]}} </h2>
              <h6> (selecionar pelo menos um acabamento obrigatório quando aplicável) </h6>
            </v-col>
          <v-row justify="center" align="center" class="d-flex">
            <v-col cols="3" >
          <v-select
          style="border-radius: 0px;"
          :items="materials"
          label="Material"
          dense
          hide-details
          outlined
          v-model="material[`material`+n]"
          @change="getFinishes()"
        ></v-select>
        </v-col>

        <v-col cols="7" >
          <v-select
          height="30"
          style="border-radius: 0px; width:280px"
          :items="finishes"
          label="Acabamentos"
          dense
          multiple
          hide-details
          outlined
          v-model="finish[`finish`+n]"
        >
        <template v-slot:selection="{ item, index }">
          <span v-if="index === 0">{{ item }}</span>
        <span
          v-if="index === 1"
          class="ml-2 mt-1 grey--text text-caption"
        >
          (+{{ finish[`finish`+n].length - 1 }} outros)
        </span>
      </template>
      </v-select>
        </v-col>
        
          </v-row>
            <v-row justify="center" align="center" class="d-flex">
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

        <v-card-text style="height: 400px">
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
            @click="dialog = false; getRefs()"
          >
            Confirmar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
            <v-col cols=4>
            <v-img :src="myImage" contain height="100px" width="100px" @click.stop="dialog = true"></v-img>
            </v-col>
            <v-col cols=8> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Zona {{faces[n-1]}}
                </v-col>
                <v-col cols="auto" class="text-decoration-underline" @click="dialog=true">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
            </v-row>
            <v-col cols="12" class="mt-0">
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col>
              Medidas (cm)
              </v-col>
            </v-row>
              <v-row justify="center" class="d-flex flex-row">
              <v-col class="pa-0" cols="4">
            <v-text-field style="border-radius: 0px;"
            label="Largura"
            class="mr-4"
            dense
            outlined
            hide-details
            v-model = "widths[`width`+n]"
          ></v-text-field>
              </v-col>
            <v-col class="pa-0" cols="4">
            <v-text-field style="border-radius: 0px;"
            label="Altura"
            class="ml-4"
            outlined
            dense
            hide-details
            v-model="heights[`height`+n]"
          ></v-text-field>
              </v-col>
            </v-row>
            </v-col>
          </v-row>
        </template>
        <template v-else>
          <v-row justify="center" align="center" class="d-flex flex-column mt-2">
            <v-col cols="auto" class="pa-0 pb-2 pt-2">
              <h3> Face {{faces[n-1]}} </h3>
              <h6> (selecionar pelo menos um acabamento obrigatório quando aplicável) </h6>
            </v-col>
          <v-row justify="center" align="center" class="d-flex">
            <v-col cols="3" >
          <v-select
          style="border-radius: 0px;"
          :items="materials"
          label="Material"
          dense
          hide-details
          outlined
          v-model="material[`material`+n]"
          @change="getFinishes()"
        ></v-select>
        </v-col>

        <v-col cols="3" >
          <v-select
          style="border-radius: 0px;"
          :items="finishes"
          label="Acabamentos"
          dense
          multiple
          hide-details
          outlined
          v-model="finish[`finish`+n]"
        ></v-select>
        </v-col>
        <v-col cols="2">
            <v-text-field style="border-radius: 0px;"
            label="Largura"
            dense
            outlined
            hide-details
            v-model = "widths[`width`+n]"
          ></v-text-field>
              </v-col>
            <v-col cols="2">
            <v-text-field style="border-radius: 0px;"
            label="Altura"
            outlined
            dense
            hide-details
            v-model="heights[`height`+n]"
          ></v-text-field>
              </v-col>
          </v-row>
            <v-row justify="center" align="center" class="d-flex">
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

        <v-card-text style="height: 400px">
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
            @click="dialog = false; getRefs()"
          >
            Confirmar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
            <v-col cols=3>
            <v-img :src="myImage" contain height="50px" width="50px" @click.stop="dialog = true"></v-img>
            </v-col>
                <v-col cols="9" class="text-decoration-underline" @click="dialog=true">
                  Escolher Imagem Zona {{faces[n-1]}}
                </v-col>
            </v-row>
          </v-row>
        </template>
        </v-card>
        </v-row>
      </v-window-item>
    </v-window>
  

    <v-card-actions class="justify-space-between" style="background-color: rgba(235,235,238, 0.7);">
      <v-btn
        text
        @click="prev"
      >
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <v-item-group
        v-model="onboarding"
        class="text-center"
        mandatory
      >
        <v-item
          v-for="n in length"
          :key="`btn-${n}`"
          v-slot="{ active, toggle }"
        >
          <v-btn
            :input-value="active"
            icon
            @click="toggle"
          >
            <span> {{faces[n-1]}} </span>
          </v-btn>
        </v-item>
      </v-item-group>
      <v-btn
        text
        @click="next"
      >
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
  </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 55px;">
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
  name: 'CustomerOrderABC',

  components: {
  },

  data: () => ({
    store,
    myImage2: require('@/assets/logologo1.png'),
    faces: ["A", "B", "C", "D", "E"],
    myImage: require('@/assets/default-placeholder.png'),
    picked: null,
    images: [    
    ],
    dialog: false,
    right: require('@/assets/right.jpg'),
    ex4: false,
    length: 5,
    onboarding: 0,
    items2: ['BACKLIT','BANNER','MICROPERFURADO',
    'PAPEL FOTOGRÁFICO', 'PVC 3 mm', 'PVC 5 mm',
    'TÊXTIL BACKLIT MA', 'VINIL', 'Vinil imp + recort',
    'VINIL INTERIOR MON', 'VINIL METALIZADO', 'VINIL REPOSICIONÁ'],
    allMaterials: null,
    allBrands: null,
    materials: [],
    material: { material1: null, material2: null, material3: null, material4: null, material5: null, },
    brands: [],
    brand: null,
    widths: { width1: null, width2: null, width3: null, width4: null, width5: null},
    heights: { height1: null, height2: null, height3: null, height4: null, height5: null},
    finish: { finish1: [], finish2: [], finish3: [], finish4: [], finish5: []},
    allFinishes: null,
    allFinishGroups: null,
    finishes: []
  }),
  methods: {
    getFinishes: async function () {
      try {
        this.finishes = []
        var selectedMat = null;
        console.log(this.material[`material`+(this.onboarding+1)])
        this.allMaterials.forEach(element => {
          if(element.name == this.material[`material`+(this.onboarding+1)])
          selectedMat = element
        });
        this.allFinishes = await Backend.getFinishes()
        this.allFinishes.forEach(element => {
          if(selectedMat.additionalFinishings.find(x => x.id == element.id))
          this.finishes.push(element.name)
        });
        this.allFinishGroups = await Backend.getFinishGroups()
        this.allFinishGroups.forEach(element => {
          if(selectedMat.mandatoryFinishings.find(x => x.id == element.id)){
            element.finishings.forEach(element2 => {
              this.finishes.push(element2.name + " (Obrigatório)")
            })
          }
        });
        console.log(this.finishes)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    nextScreen () {
      store.widths = this.widths
      store.heights = this.heights
      store.finitos = this.finish
      store.materioscas = this.material
      var uniqueBrands = []
      var costPerBrand = new Map()
          this.allBrands.forEach(element => {
          if(element.name == this.brand){
            store.currentBrandId.push(element.id)
            if(!uniqueBrands.includes(this.brand)){
            uniqueBrands.push(this.brand)
            costPerBrand.set(this.brand, 0)
          }
          }
        });

      var cost = 0;
        for (let n = 1; n < this.length + 1; n++) {
          this.allMaterials.forEach(element => {
          if(element.name == this.material[`material`+n]){
            store.selectedMaterial.push(element.id)
            var price = element.cost
            cost += (((this.widths[`width`+n]/100) * (this.heights[`height`+n]/100)) * price)
            store.dimensions.push({width: this.widths[`width`+n], height: this.heights[`height`+n]})
            var currCost = costPerBrand.get(this.brand[`brand`+n])
            currCost += (((this.widths[`width`+n]/100) * (this.heights[`height`+n]/100)) * price)
            costPerBrand.set(this.brand, currCost)
            var finToUse = this.finish[`finish`+n]
            var finAux = []
            finToUse.forEach(fin => {
              finAux.push(fin.split(" (Obrigatório)")[0])
            })
            console.log(finAux)
            var finishesForScreen = []
            this.allFinishes.forEach(finishe => {
              finAux.forEach(fin => {
                if(fin == finishe.name){
                  finishesForScreen.push({id: finishe.id, name: finishe.name})
                }
                console.log(finishesForScreen)
                })
              })
          store.finishes.push(finishesForScreen)
          }
        });
        }
        store.currentCost = cost
        store.uniqueBrands = uniqueBrands
        store.costPerBrand = costPerBrand
        console.log(store)
        this.$router.push({name: 'ABCfinal'});
      },

      next () {
        this.onboarding = this.onboarding + 1 === this.length
          ? 0
          : this.onboarding + 1
      },
      prev () {
        this.onboarding = this.onboarding - 1 < 0
          ? this.length - 1
          : this.onboarding - 1
      },
      getRefs () {
        store.facesDefault.forEach(element => {
          if (element.face === this.faces[this.onboarding]){
            element.link = this.images[this.picked].link
            var id = this.images[this.picked].id
            this.picked = null
            store.images.push(id)
          }
        });
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
      this.images = []
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
      if (store.isActive4) {
        this.length = 4
      }
      this.getMaterials()
      this.getBrands()
      if(store.backtracking){
      this.widths =  store.widths 
      this.heights = store.heights
      this.material = store.materioscas
      this.allBrands = await Backend.getBrands()
        this.allBrands.forEach(element => {
          if(element.id == store.currentBrandId[0]){
            this.brand = element.name
          }
        });
        store.backtracking = false
      }
      store.images = []
      store.finishes = []
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
.custom-tooltip {
    opacity: 1!important;
}
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