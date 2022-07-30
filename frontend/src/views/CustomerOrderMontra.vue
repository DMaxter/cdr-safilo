<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="650" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
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
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('profile')">
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
          <v-row class="mt-1 mb-2" justify="center">
            <v-col cols = "4">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                   <v-img :src=store.facesDefault[0].src height="40px" width="250.5px"></v-img>
                </v-col>
                <v-col>
                <v-row justify="space-between">
                  <v-col cols = "5" class="pa-0">
                  <v-img :src=store.facesDefault[2].src height="60px" width="80px"></v-img>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-img :src=store.facesDefault[3].src height="60px" width="80px"></v-img>
                  </v-col>
                </v-row>
                </v-col>
                <v-col class="pa-0">
                  <v-img :src=store.facesDefault[1].src height="40px" width="250.5px"></v-img>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="2">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-img :src=store.facesDefault[4].src height="140px" width="80px"></v-img>
            </v-col>
          </v-row>
          </v-col>
          </v-row>
          </v-row>
          <v-row
            align="center"
            justify="center"
          >
          <v-card
            flat
            rounded
            width="500"
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
        >
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto" >
              <h2> Face {{faces[n-1]}} </h2>
            </v-col>
          <v-col cols="auto" >
          <v-select
          :items="items2"
          label="Material"
          dense
          hide-details
          outlined
        ></v-select>
            </v-col>
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
                  class="text-h2 flex-grow-1 text-center selected"
                >
                  <span style="opacity: 0">Ahahahahahahahaha que cheatcode do caralho</span>
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
          ></v-text-field>
              </v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-card>
        </v-row>
      </v-window-item>
    </v-window>

    <v-card-actions class="justify-space-between">
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
            <v-icon>mdi-record</v-icon>
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
              @click="$router.push('ABCfinal')"
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
    'VINIL INTERIOR MON', 'VINIL METALIZADO', 'VINIL REPOSICIONÁ']
  }),
  methods: {
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
            element.src = this.images[this.picked]
            this.picked = null
          }
        });
      }
    },
};

</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}

.selected {
    background-image: repeating-linear-gradient(-45deg,
                        rgba(255,0,0,.25),
                        rgba(255,0,0,.25) 5px,
                        rgba(0,0,255,.25) 5px,
                        rgba(0,0,255,.25) 10px
                      );
  }
</style>