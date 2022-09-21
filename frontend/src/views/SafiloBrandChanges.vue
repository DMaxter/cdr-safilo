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
              <v-btn color="#6e4e5d" value="left" height="64" width="170" @click="$router.push('profileSafilo')" class="customGradient">
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

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="desserts2" fixed-header item-key="name" hide-default-footer height="250" style="width: 600px;" class="elevation-1 my-header-style mt-10">
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="deleteBrand(item)">mdi-delete</v-icon>
          </template>
    </v-data-table>
            </v-row>
            <v-row justify="center">
                <template>
    <v-dialog
      v-model="dialog1"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="200" class="mb-3 mt-5 customGradient" dark v-bind="attrs" v-on="on"> Adicionar Marca </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Adicionar Marca </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Nome da marca a adicionar"
                  required
                  v-model="brandName"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog1 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="addBrand(); dialog1 = false;"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 100px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient"
              tile
              small
              dark
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
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        dialog1: false,
        store,
        myImage2: require('@/assets/logologo1.png'),
        desserts: [
          {
            name: 'Hugo Boss',
          },
          {
            name: 'Jimmy Choo',
          },
          {
            name: 'Tommy Hilfiger',
          },
        ],
        desserts2: [],
        allBrands: null,
        brandName: null,
      }
    },
    async created() { 
       try {
        this.allBrands = await Backend.getBrands()
        console.log(this.allBrands)
        this.allBrands.forEach(element => {
          this.desserts2.push({ name: element.name })
        });
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    computed: {
      headers() {
        return [
          {
            text: 'Marca',
            align: 'left',
            sortable: true,
            value: 'name',
            class: 'my-header-style'
          },
          { text: "", value: "actions", align: 'right', sortable: false, class: 'my-header-style' },
        ]
      },
      
    },
    methods: {
      getRequest(item) {
        console.log(item)
        this.$router.push({name: 'clientInfo'});
      },
      addBrand: async function () {
      try {
        await Backend.addBrand(this.brandName)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    deleteBrand: async function (item) {
      try {
        var id = null
        this.allBrands.forEach(element => {
          if(element.name == item.name){
            id = element.id
          }
        });
        await Backend.deleteBrand(id)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    }
  }
</script>

<style>

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.customGradient {
  background-image: linear-gradient(#616161, grey);
}

  
.my-header-style {
    background-color: rgb(225, 225, 225) !important;
  }
</style>