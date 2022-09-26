<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="650" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
            <template v-if="this.menu1">
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
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu2">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" dark borderless tile>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu3">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
          <v-row class="mt-1 mb-2" justify="center">
            <template v-if="store.isActive2">
            <v-col cols = "4">
              <v-row class="d-flex flex-column">
                <v-col class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.pedidoAtual.images[0] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[0] height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[2] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[2] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.pedidoAtual.images[3]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[3] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[1]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[1] height="40px" width="250.5px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[4] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[4] height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[4]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[4] height="140px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[0]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[0] height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[2]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[2] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
                  </template>
                  </v-tooltip>
                  </v-col>
                  <v-col cols = "4" class="pa-0">
                  <v-tooltip
                  bottom
                  color="white"
                  content-class="custom-tooltip"
                  >
                  <v-img :src=store.pedidoAtual.images[3]></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[3] height="60px" width="80px" contain v-bind="attrs" v-on="on"></v-img>
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
                  <v-img :src=store.pedidoAtual.images[1] ></v-img>
                  <template v-slot:activator="{ on, attrs }">
                  <v-img :src=store.pedidoAtual.images[1] height="40px" width="250px" contain v-bind="attrs" v-on="on"></v-img>
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
          <v-text-field style="pointer-events: none;"
          label = "material"
          :value = store.pedidoAtual.material[n-1]
          dense
          hide-details
          outlined
        ></v-text-field>
            </v-col>
            <v-col cols="5" >
          <v-text-field style="pointer-events: none;"
          label="Marca"
          :value = store.pedidoAtual.marca[n-1]
          dense
          hide-details
          outlined
        ></v-text-field>
      </v-col>
      </v-row>
            <v-row justify="center" align="center" class="d-flex">
            <v-col cols=4>
            <v-img :src = store.pedidoAtual.images[n-1] contain height="100px" width="100px" @click.stop="dialog = true"></v-img>
            </v-col>
            <v-col cols=8> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Zona {{faces[n-1]}}
                </v-col>
                <v-col cols="auto" class="text-decoration-underline">
                </v-col>
              </v-row>
            </v-col>
            </v-row>
            <v-col cols="4" class="mt-0 pa-0">
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col class="pt-0">
              Medidas (cm)
              </v-col>
              <v-col class="pa-0">
            <v-text-field style="width: 200px; pointer-events: none;"
            label= "Altura"
            :value="getHeight(n-1)"
            filled
            rounded
            dense
            hide-details
          ></v-text-field>
              </v-col>
            <v-col class="pa-0 mt-3">
            <v-text-field style="width: 200px; pointer-events: none;"
            label="Largura"
            :value="getWidth(n-1)"
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
              @click = "$router.push('history')"
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
              @click="$router.push('detailsMontraFinal')"
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
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
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
    myImage2: require('@/assets/logologo1.png'),
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
    menu1: false,
    menu2: false,
    menu3: false,
  }),
  async created() {
    var profile = await Backend.getProfile()
    if(profile.roles[0] == 'COMMERCIAL' || profile.roles[0] == 'ADMIN'){ 
      this.menu1 = true
    } else if(profile.roles[0] == 'CDR'){ 
      this.menu2 = true
    } else if(profile.roles[0] == 'MANAGER'){
      this.menu3 = true
    }
  },
  methods: {
      getHeight(n) {
        return store.pedidoAtual.dimensoes[n].height
      },
      getWidth(n) {
        return store.pedidoAtual.dimensoes[n].width
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
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

  .customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>