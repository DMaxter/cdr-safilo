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

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
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
                  <v-img :src=store.facesDefault[4].link ></v-img>
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
                  max-width = "400px"
                  max-height ="400px"
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
          </template>
          </v-row>
          <v-col cols="auto" class="mb-5">
          </v-col>
          <v-col cols="auto" >
            <v-textarea
          class="mt-0" 
          style="width: 400px;"
          rows="2"
          no-resize
          hide-details
          outlined
          tile
          name="input-7-4"
          label="Quantidade"
          value=""
          v-model="quantity"
        ></v-textarea>
          </v-col>
          <v-col cols="5">
            <v-row justify="end" class="mt-0">
            <v-checkbox
            v-model="checkbox"
            hide-details
            :label="`Aplicação?`"
          ></v-checkbox>
          </v-row>
          </v-col>
          <v-col cols="auto" >
            <v-textarea
          class="mt-8" 
          style="width: 450px;"
          rows="2"
          height="100"
          no-resize
          hide-details
          outlined
          tile
          name="input-7-4"
          label="Observações sobre o pedido"
          value=""
          v-model="observations"
        ></v-textarea>
          </v-col>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 95px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="store.backtracking = true; $router.push('ABC')"
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

export default {
  name: 'CustomerOrder3ABC',

  components: {
  },

  data: () => ({
    myImage2: require('@/assets/logologo1.png'),
    store,
    quantity: null,
    checkbox: false,
    observations: null,
  }),

  methods: {
    nextScreen () {
      store.currentCost = store.currentCost * this.quantity
      if(this.checkbox){
        store.currentCost += 500
      }
      var quantidade = this.quantity
      store.costPerBrand.forEach(function(value, key) {
        var newValue = value * quantidade
        store.costPerBrand.set(key, newValue)
      })
      store.quantity = this.quantity
      store.observations = this.observations
      store.application = this.checkbox
      console.log(this.observations)
      this.$router.push({name: 'ABCfinal2'});
    }
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