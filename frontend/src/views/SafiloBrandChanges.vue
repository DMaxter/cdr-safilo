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
              <v-btn color="#6e4e5d" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column">
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column">
        <v-data-table :headers="headers" :items="desserts2" :search="procura" fixed-header disable-pagination item-key="name" hide-default-footer height="330" style="width: 600px;" class="elevation-1 my-header-style mt-3">
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="currentItem = item; toDelete = true, dialog1 = true">mdi-delete</v-icon>
            <v-icon @click="currentItem = item; dialog2 = true">mdi-pen</v-icon>
          </template>
          <template v-slot:top>
                  <v-container fluid>
                    <v-row>
                      <v-col cols="5">
                        <v-row class="pa-2">
                          <v-text-field style="width: 150px; border-radius: 0px;" v-model="procura" label="Procurar" hide-details outlined
                            dense></v-text-field>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>
                </template>
    </v-data-table>
            </v-row>
            <v-row justify="center">
                <template>
    <v-dialog
      v-model="dialog1"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="200" class="mb-3 mt-5 customGradient" tile @click="added = false; toDelete = false; toDelete2 = false; failed = false" dark v-bind="attrs" v-on="on"> Adicionar Marca </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added && !failed && !toDelete && !toDelete2"> Adicionar Marca </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <span class="text-h5" v-show="added"> Marca adicionada com sucesso! </span>
              <span class="text-h5" v-show="failed"> Ocorreu um erro a adicionar a marca </span>
              <span class="text-h5" v-show="toDelete"> Tem a certeza que pretende eliminar a marca da base de dados? (pressione o botão novamente para apagar) </span>
              <span class="text-h5" v-show="toDelete2"> Marca eliminada com sucesso! </span>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Nome da marca a adicionar"
                  required
                  v-model="brandName"
                  v-show="!added && !failed && !toDelete && !toDelete2"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog1 = false; brandName = null; added = false"
          >
            Voltar
          </v-btn>
          <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  v-show="toDelete"
                  @click="deleteBrand(); added = false; toDelete = false; failed = false; toDelete2 = true"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added && !failed && !toDelete && !toDelete2"
            @click="addBrand()"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="dialog2"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
  
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added && !failed"> Editar nome da marca </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <span class="text-h5" v-show="added"> Nome da marca alterado com sucesso! </span>
              <span class="text-h5" v-show="failed"> Ocorreu um erro a alterar o nome da marca </span>
              <span class="text-h7 mt-5" v-show="!added && !failed">Nome atual da marca: {{this.currentItem.name}}</span>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Novo nome da marca"
                  required
                  v-model="brandName"
                  v-show="!added && !failed"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog2 = false; brandName = null; added = false"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added && !failed && !toDelete && !toDelete2"
            @click="editBrand()"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 32px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('configure')"
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
        dialog2: false,
        store,
        currentItem: "",
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
        added: false,
        failed: false,
        toDelete: false,
        toDelete2: false,
        procura: ""
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
        this.allBrands = await Backend.getBrands()
        this.desserts2 = []
        this.allBrands.forEach(element => {
          this.desserts2.push({ name: element.name })
        });
        this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
    },
    deleteBrand: async function () {
      try {
        var id = null
        this.allBrands.forEach(element => {
          if(element.name == this.currentItem.name){
            id = element.id
          }
        });
        await Backend.deleteBrand(id)
        this.allBrands = await Backend.getBrands()
        this.desserts2 = []
        this.allBrands.forEach(element => {
          this.desserts2.push({ name: element.name })
        });
        this.toDelete = false
        this.toDelete2 = true
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    editBrand: async function () {
      try {
        var id = null
        this.allBrands.forEach(element => {
          if(element.name == this.currentItem.name){
            id = element.id
          }
        });
        await Backend.updateBrand(id, this.brandName)
        this.allBrands = await Backend.getBrands()
        this.desserts2 = []
        this.allBrands.forEach(element => {
          this.desserts2.push({ name: element.name })
        });
        this.added = true
      } catch (error) {
        this.failed = true
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
    border-radius: 0px;
    background-color: rgb(243, 243, 243) !important;
  }
</style>
