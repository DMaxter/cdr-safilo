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

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" class="v-btn--active">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('order')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mb-2 mt-2">
              <v-avatar color="#6e4e5d" size="100" class="mb-6">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
                <v-data-table
                  height = "280px"
                  fixed-header
                  :headers="headers"
                  :items="desserts"
                  hide-default-footer
                  class="elevation-1"
                ></v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 80px;">
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
          { text: 'MARCA', value: 'marca', width: '200px', align: 'left' },
          { text: 'PLAFOND', value: 'plafond', width: '200px', align: 'left' },
        ],
        desserts: [
          {
            marca: 'abc',
            plafond: '100',
          },
          {
            marca: 'def',
            plafond: '100',
          },
          {
            marca: 'ghy',
            plafond: '100',
          },
          {
            marca: 'ghy',
            plafond: '100',
          },
          {
            marca: 'ghy',
            plafond: '100',
          },
          {
            marca: 'ghy',
            plafond: '100',
          }
        ],
      }
    },
  }
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>