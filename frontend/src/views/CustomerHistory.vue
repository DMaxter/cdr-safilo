<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="600" width="800" style="border-radius: 15px">
          <v-row no-gutters justify="center">

            <v-btn-toggle v-model="icon" dark dense rounded style="border-radius: 15px" class="mb-16">
              <v-btn color="#6e4e5d" value="left" @click="$router.push('profile')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" class="v-btn--active">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('order')">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
                <v-data-table
                  :headers="headers"
                  :items="desserts"
                  :items-per-page="5"
                  class="elevation-1"
                ></v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 180px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
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

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        headers: [
          {
            text: 'COD/DATA/NUM',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: 'MARCA', value: 'marca' },
          { text: 'MODELO', value: 'modelo' },
        ],
        desserts: [
          {
            name: 'ABC 01.01.2022 0001',
            marca: 'abc',
            modelo: 'rst',
          },
          {
            name: 'DEF 02.01.2022 0002',
            marca: 'def',
            modelo: 'uvw',
          },
          {
            name: 'GHI 03.01.2022 0003',
            marca: 'ghy',
            modelo: 'xyz',
          }
        ],
      }
    },
  }
</script>

<style>

#app {
  background: url('@/assets/bg_grande.jpg') center center fixed !important;
  background-size: cover;
}
</style>