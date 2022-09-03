<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" :height="$vuetify.breakpoint.lg
                ? '650px' 
                : '900px'" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
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
          <v-row :class="$vuetify.breakpoint.lg
                ? 'mt-1 mb-2 mr-10' 
                : 'mt-10 mb-5 mr-10'" justify="center">
            <template v-if="store.isActive2">
            <v-col cols = "4">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[0].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[0].src height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
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
                  >
                  <v-img :src=store.facesDefault[2].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[2].src height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[3].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[3].src height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.facesDefault[1].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[1].src height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
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
                  >
                  <v-img :src=store.facesDefault[4].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[4].src height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
            </v-col>
          </v-row>
          </v-col>
          </template>
          <template v-else>
          <v-col cols="2">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[4].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[4].src height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  >
                  <v-img :src=store.facesDefault[0].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[0].src height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
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
                  >
                  <v-img :src=store.facesDefault[2].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[2].src height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.facesDefault[3].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[3].src height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.facesDefault[1].src ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.facesDefault[1].src height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
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
          >
          <v-card
            flat
            tile
            outlined
            width="500"
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
          height="400"
          width="500"
          outlined
          style="background-color: rgba(235,235,238, 0.4);"
        >
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto" >
              <h2> Face {{faces[n-1]}} </h2>
            </v-col>
          <v-row justify="center" align="center" class="d-flex">
            <v-col cols="5" >
          <v-select
          :items="materials"
          label="Material"
          dense
          hide-details
          outlined
          v-model="material[`material`+n]"
        ></v-select>
        </v-col>
          <v-col cols="5" >
          <v-select
          :items="brands"
          label="Marca"
          dense
          hide-details
          outlined
          v-model="brand[`brand`+n]"
        ></v-select>
        </v-col>
          </v-row>
            <v-row justify="center" align="center" class="d-flex">
                 <v-dialog
      v-model="dialog"
      persistent
      max-width="800"
    >
      <v-card>
        <v-card-title class="text-h5">
          Selecionar uma imagem
        </v-card-title>

        <v-card-text>
          <v-item-group active-class="selected" v-model="picked">
    <v-container>
      <v-row>
        <v-col
          v-for="(image, i) in images"
          :key="i"
          cols="3"
        >
          <v-item v-slot="{ active, toggle }">
            <v-img
              :src= image.src
              height="150"
              @click="toggle"
            >
                <v-scroll-y-transition>
                <div
                  v-if="active"
                  class="text-h3 flex-grow-1 text-center selected"
                >
                  <span style="opacity: 0">Ahahahahahaha</span>
                </div>
              </v-scroll-y-transition>
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
                <v-col cols="auto" class="text-decoration-underline">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
            </v-row>
            <v-col cols="4" class="mt-0">
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col>
              Medidas (cm)
              </v-col>
              <v-col class="pa-0">
            <v-text-field style="width: 200px;"
            label=""
            placeholder="Largura"
            filled
            rounded
            dense
            hide-details
            v-model = "widths[`width`+n]"
          ></v-text-field>
              </v-col>
            <v-col class="pa-0 mt-3">
            <v-text-field style="width: 200px;"
            label=""
            placeholder="Altura"
            filled
            rounded
            dense
            hide-details
            v-model="heights[`height`+n]"
          ></v-text-field>
              </v-col>
              </v-row>
            </v-col>
          </v-row>
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
              @click="nextScreen()"
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrderABC',

  components: {
  },

  data: () => ({
    store,
    faces: ["A", "B", "C", "D", "E"],
    myImage: require('@/assets/default-placeholder.png'),
    picked: null,
    images: [
        {
          src: require('@/assets/uno.jpg'),
        },
        {
          src: require('@/assets/dos.jpg'),
        },
        {
          src: require('@/assets/trese.png'),
        },
        {
          src: require('@/assets/quatre.png'),
        },
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
    brand: { brand1: null, brand2: null, brand3: null, brand4: null, brand5: null, },
    widths: { width1: null, width2: null, width3: null, width4: null, width5: null},
    heights: { height1: null, height2: null, height3: null, height4: null, height5: null},
  }),
  methods: {
    nextScreen () {
      var uniqueBrands = []
      var costPerBrand = new Map()
        for (let n = 1; n < 6; n++) {
          if(!uniqueBrands.includes(this.brand[`brand`+n])){
            uniqueBrands.push(this.brand[`brand`+n])
            costPerBrand.set(this.brand[`brand`+n], 0)
          }
        }
        uniqueBrands.pop()
        costPerBrand.delete(null)
      var cost = 0;
      this.allMaterials.forEach(element => {
        for (let n = 1; n < 6; n++) {
          if(element.name == this.material[`material`+n]){
            var price = element.cost
            cost += (((this.widths[`width`+n]/100) * (this.heights[`height`+n]/100)) * price)
            var currCost = costPerBrand.get(this.brand[`brand`+n])
            currCost += (((this.widths[`width`+n]/100) * (this.heights[`height`+n]/100)) * price)
            costPerBrand.set(this.brand[`brand`+n], currCost)
          }
        }
        });
        store.currentCost = cost
        store.currentBrand = uniqueBrands
        store.costPerBrand = costPerBrand
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
          console.log(this.onboarding)
          if (element.face === this.faces[this.onboarding]){
            console.log(element.src)
            element.src = this.images[this.picked].src
            console.log(element.src)
            this.picked = null
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
    },
     created: async function () {
      this.getMaterials()
      this.getBrands()
    }
};

</script>

<style>
.custom-tooltip {
    opacity: 1!important;
}
#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}

.selected {
    background-image: linear-gradient(to top, #F0E68C 0%, transparent 72px);
  }
</style>