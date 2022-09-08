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
            <v-row justify="center" align="center" class="d-flex flex-column mb-11 mt-5">
              Selecionar cliente e morada de entrega
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="desserts2" fixed-header item-key="name" hide-default-footer height="210" style="width: 600px;" class="elevation-1">
        <template v-slot:top>

            <!-- v-container, v-col and v-row are just for decoration purposes. -->
            <v-container fluid>
                <v-row>

                    <v-col cols="5">
                        <v-row class="pa-2">
                             <v-select
                                    style="width: 150px;"
                                    :items="marcasList"
                                    v-model="marcasFilterValue"
                                    label="Faturação"
                            ></v-select>
                        </v-row>
                    </v-col>

                    <v-col cols="4">
                        <v-row class="pa-2">
                            <!-- Filter for calories -->
                            <v-select
                                    style="width: 150px;"
                                    :items="estadosList"
                                    v-model="estadosFilterValue"
                                    label="Ativo"
                            ></v-select>
                        </v-row>
                    </v-col>

                </v-row>
            </v-container>

        </template>
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="getSpecificClient(item)">mdi-plus</v-icon>
          </template>
    </v-data-table>
     <v-dialog
      v-model="dialog1"
      persistent
      max-width="500px"
    >
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Selecionar Morada </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
          <v-autocomplete
            v-model="selectedAdress"
            :items="items"
            dense
            label="Filled"
          ></v-autocomplete>
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
            @click="nextPage()"
          >
            Selecionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        items: [],
        selectedAdress: null,
        allClients: null,
        desserts2: [],
        clientName: null,
        clientEmail: null,
        clientCode: null,
        clientPhone: null,
        clientNIF: null,
        dialog1: false,
        store,
        estadosList: [
          {text: "Qualquer", value: null},
          {text: "Sim", value: "sim"},
          {text: "Não", value: "não"},
        ],
        marcasList: [
          {text: "Qualquer", value: null},
          {text: "Sim", value: "sim"},
          {text: "Não", value: "não"},
        ],
        // Filter models.
        dates: [],
        menu: false,
        selectedClient: null,
      }
    },
    async created() { 
       try {
        this.allClients = await Backend.getClients()
        this.allClients.forEach(element => {
          this.desserts2.push({ name: element.name, fat: element.id })
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
            text: 'Código',
            align: 'left',
            sortable: false,
            value: 'fat',
          },
          {
            text: 'Nome',
            value: 'name',
            align: "center",
          },
          { text: "", value: "actions", align: "right", sortable: false },
        ]
      },
      
    },
    methods: {
      getSpecificClient(item){
        this.allClients.forEach(element => {
          if(element.name == item.name){
            this.selectedClient = element
            element.addresses.forEach(element2 => {
              this.items.push(element2.address)
            })
          }
        });
        this.dialog1 = true
      },

      nextPage(){
        this.selectedClient.addresses.forEach(element => {
          if(element.address == this.selectedAdress){
            store.currentAddress = element.id
            store.currentClient = this.selectedClient.id
          }
        })
        this.dialog1 = false
        console.log(store.currentAddress)
        console.log(store.currentClient)
        this.$router.push({name: 'order'});
      }
    }
  }
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>