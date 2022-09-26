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
              <v-btn color="#6e4e5d" value="left" height="64" width="170" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="170" class="v-btn--active customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="addresses" fixed-header item-key="name" hide-default-footer height="210" style="width: 600px;" class="elevation-1 my-header-style">
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
            <v-icon @click="getRequest(item)">mdi-plus</v-icon>
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
      <v-btn height="60" width="240" class="mb-3 mt-5 customGradient" tile dark v-bind="attrs" v-on="on"> Adicionar Loja a Cliente </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5"> Adicionar Loja a Cliente </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Código da Loja"
                  required
                  v-model="storeID"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Morada da Loja"
                  required
                  v-model="storeAdress"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Código postal da Loja"
                  required
                  v-model="storePostCode"
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
            @click="addStore(); dialog1 = false;"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 70px;">
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
import AddressDto from '@/models/AddressDto';

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        store,
        myImage: require('@/assets/logologo1.png'),
        storeID: null,
        storeAdress: null,
        storePostCode: null,
        dialog1: false,
        dates: [],
        menu: false,
        address: new AddressDto(),
        addresses: null,
        desserts: [
          {
            name: '1234',
            fat: "xxxxxx - Lisboa",
            morada: "Rua João Paulo 3º",
            imagem: require('@/assets/quatre.png'),
            telefone: "999999999"
          },
          {
            name: '5678',
            fat: "yyyyyy - Porto",
            morada: "Rua João Paulo 7º",
            imagem: require('@/assets/trese.png'),
            telefone: "999999998"
          },
          {
            name: '9101',
            fat: "zzzzzz - Setubal",
            morada: "Rua João Paulo 12º",
            imagem: require('@/assets/dos.jpg'),
            telefone: "999999997"
          },
        ],
      }
    },
    async created() { 
      try {
        this.addresses = store.currentClient.addresses
        console.log(store.currentClient)
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
            value: 'id',
            class: 'my-header-style'
          },
          {
            text: 'Morada',
            value: 'address',
            align: "center",
            class: 'my-header-style'
          },
          {
            text: 'Código postal',
            value: 'postalCode',
            align: "center",
            class: 'my-header-style'
          },
          { text: "", value: "actions", sortable: false, class: 'my-header-style'},
        ]
      },
      
    },
    methods: {
      getRequest(item) {
        console.log(item)
        store.clientAddress = item
        this.$router.push({name: 'storeInfo'});
      },

      addStore: async function () {
      try {
        this.address.id = this.storeID
        this.address.address = this.storeAdress
        this.address.postalCode = this.storePostCode
        console.log(this.address)
        await Backend.addStore(store.currentClient.id, this.address)
        var allClients = await Backend.getClients()
        allClients.forEach(element => {
          if(element.id == store.currentClient.id){
            this.addresses = element.addresses
          }
        });
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
  border-radius: 0px;
  background-color: rgb(241, 241, 241) !important;
}
</style>