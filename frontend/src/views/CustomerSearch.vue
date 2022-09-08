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

              <v-btn color="#6e4e5d" value="center2" class="v-btn--active" height="60" width="170">
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
          <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto">
                Procurar Pedido
            </v-col>
            <v-col cols="auto" >
            <v-text-field
            v-model= text
            label=""
            placeholder="Nome/Id"
            filled
            rounded
            dense
          ></v-text-field>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Procurar por Material
            </v-col>
            <v-col cols="auto" >
          <v-select
          v-model = material
          :items="items2"
          label="Material"
          dense
          outlined
        ></v-select>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Procurar por Estado
            </v-col>
            <v-col cols="auto" >
          <v-select
          v-model = estado
          :items="items"
          label="Estado"
          dense
          outlined
        ></v-select>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
              <v-btn
              class="d-flex flex-column"
              outlined
              x-large
              rounded
              color="#6e4e5d"
              @click=searchFilters() 
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
  name: 'CustomerSearch',

  components: {
  },

  data: () => ({
    store,
    text: "",
    material: "",
    estado: "",
    collapseOnScroll: true,
    items: [
          {text: "Encomendado", value: "ORDERED"},
          {text: "Em Produção", value: "IN_PRODUCTION"},
          {text: "Terminado", value: "DONE"},
          {text: "Cancelado", value: "CANCELLED"},
        ],
    items2: [],
    allMaterials: null,
  }),
  async created () {
    this.allMaterials = await Backend.getMaterials()
        this.allMaterials.forEach(element => {
          this.items2.push(element.name)
        });
  },
  methods: { 
    searchFilters() {
      if (this.text != "") {
        store.codeSearch = this.text
      } if (this.material != "") {
        store.materialSearch = this.material
      }
      if (this.estado != "") {
        store.estadoSearch = this.estado
      }
      this.$router.push({name: 'searchResults'});
    }  
  }
};
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>