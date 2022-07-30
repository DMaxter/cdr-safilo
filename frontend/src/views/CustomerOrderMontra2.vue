<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
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
          </v-row> 
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-5">
            <v-row class="mt-1 mb-2" justify="center">
            <v-col cols = "8">
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
            <v-col cols="4">
            <v-row class="ml-1">
            <v-col class="pa-0">
              <v-img :src=store.facesDefault[4].src height="140px" width="80px"></v-img>
            </v-col>
          </v-row>
          </v-col>
          </v-row>
          <v-col cols="auto" class="mb-5">
          </v-col>
          <v-col cols="auto" >
            <v-textarea
          class="mt-0" 
          style="width: 400px; border-radius: 15px"
          rows="2"
          no-resize
          hide-details
          outlined
          name="input-7-4"
          label="Quantidade"
          value=""
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
          style="width: 450px; border-radius: 15px"
          rows="2"
          height="100"
          no-resize
          hide-details
          outlined
          name="input-7-4"
          label="Observações sobre o pedido"
          value=""
        ></v-textarea>
          </v-col>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 95px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('ABC')"
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
              @click="$router.push('ABCfinal2')"
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
  name: 'CustomerOrder3ABC',

  components: {
  },

  data: () => ({
    store
  }),
};
</script>

<style>
#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>