<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
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
              <v-btn color="#808080" value="left" @click="$router.push('profileComercial')" height="64" width="170" class="customGradient">
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

              <v-btn-toggle v-model="icon" dark dense tile>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profileCdr')" width="170" class="customGradient">
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
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profileSafilo')" width="170" class="customGradient">
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
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto">
                Procurar Pedido
            </v-col>
            <v-col cols="auto" >
            <v-text-field
            style="width: 250px"
            v-model= text
            label=""
            placeholder="Nome/Id"
            tile
            outlined
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
          style="width: 250px"
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
          style="width: 250px"
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
              class="d-flex flex-column customGradient"
              x-large
              tile
              dark
              @click=searchFilters() 
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
          </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
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
  name: 'CustomerSearch',

  components: {
  },

  data: () => ({
    store,
    text: "",
    myImage: require('@/assets/logologo1.png'),
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
    menu1: false,
    menu2: false,
    menu3: false
  }),
  async created () {
    this.allMaterials = await Backend.getMaterials()
        this.allMaterials.forEach(element => {
          this.items2.push(element.name)
        });
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
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
</style>