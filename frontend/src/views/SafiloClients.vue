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

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="v-btn--active customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-11 mt-5">
              
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="desserts2" fixed-header tile item-key="name" hide-default-footer height="260" style="width: 600px;" class="elevation-1 my-header-style">
        <template v-slot:top>

            <!-- v-container, v-col and v-row are just for decoration purposes. -->
            <v-container fluid >
                <v-row>

                    <v-col cols="5">
                        <v-row class="pa-2">
                          <v-text-field
                                    style="width: 150px; border-radius: 0px;"
                                    v-model="dessertFilterValue"
                                    label="Cliente"
                                    hide-details
                                    outlined
                                    dense
                                    
                            ></v-text-field>
                        </v-row>
                    </v-col>

                </v-row>
            </v-container>

        </template>
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="getSpecificClient(item)">mdi-plus</v-icon>
          </template>
    </v-data-table>
    <template>
    <v-dialog
      v-model="dialog1"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="200" class="mb-3 mt-5 customGradient" dark tile v-bind="attrs" v-on="on" @click="added = false; failed = false;"> Adicionar Cliente </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added && !failed"> Adicionar Cliente </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <span class="text-h5" v-show="added"> Cliente adicionada com sucesso! </span>
              <span class="text-h5" v-show="failed"> Ocorreu um erro a adicionar o Cliente </span>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Código do cliente"
                  required
                  v-model="clientCode"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Nome do cliente"
                  required
                  v-model="clientName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Email do cliente"
                  required
                  v-model="clientEmail"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="NIF do cliente"
                  required
                  v-model="clientNIF"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Número de telefone do cliente"
                  required
                  v-model="clientPhone"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Endereço do cliente"
                  required
                  v-model="clientAddress"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Código postal do cliente"
                  required
                  v-model="clientPostalCode"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added && !failed"
              >
                <v-text-field
                  label="Banner do cliente"
                  required
                  v-model="clientBanner"
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
            v-show="!added && !failed"
            @click="addClient();"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 60px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
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
      <v-img :src="myImage" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'
import ClientDto from '@/models/ClientDto';

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        allClients: null,
        desserts2: [],
        clientName: null,
        clientEmail: null,
        clientCode: null,
        clientPhone: null,
        clientNIF: null,
        dialog1: false,
        added: false,
        failed: false,
        dessertFilterValue: null,
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
        client: new ClientDto(),
        clientPostalCode: null,
        clientBanner: null,
        clientAddress: null,
      }
    },
    async created() { 
       try {
        this.allClients = await Backend.getClients()
        console.log(this.allClients)
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
            class: 'my-header-style'
          },
          {
            text: 'Nome',
            value: 'name',
            align: "center",
            class: 'my-header-style',
            filter: this.nameFilter
          },
          { text: "", value: "actions", align: "right", sortable: false, class: 'my-header-style' },
        ]
      },
      
    },
    methods: {
      addClient: async function () {
      try {
        this.client.id = this.clientCode
        this.client.name = this.clientName
        this.client.fiscalNumber = this.clientNIF
        this.client.email = this.clientEmail
        this.client.phone = this.clientPhone
        this.client.banner = this.clientBanner
        this.client.address = this.clientAddress
        this.client.postalCode = this.clientPostalCode
        await Backend.addClient(this.client)
        this.allClients = await Backend.getClients()
        this.desserts2 = []
        this.allClients.forEach(element => {
          this.desserts2.push({ name: element.name, fat: element.id })
        });
        this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
    },
      getSpecificClient(item) {
        this.allClients.forEach(element => {
          if(item.name == element.name){
            store.currentClient = element
          }
        });
        console.log(store.currentClient)
        this.$router.push({name: 'clientInfo'});
      },

      nameFilter(value) {
        // If this filter has no value we just skip the entire filter.
        if (!this.dessertFilterValue) {
          return true;
        }
        // Check if the current loop value (The dessert name)
        // partially contains the searched word.
        console.log(this.dessertFilterValue)
        return value.toLowerCase().includes(this.dessertFilterValue.toLowerCase());
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
  background-color: rgb(241, 241, 241) !important;
}
</style>