<template>
  <v-app>

    <v-container fill-height>
      <v-row justify="center" align="center">
        <v-col cols="auto">
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
            <v-row no-gutters justify="start" class="pt-2 pl-2">
              <template v-if="!this.menu1">
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
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="130" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="130" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="130" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="130" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('clients')" height="64" width="130" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Imagens</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else>
              <v-menu :offset-x="true" tile>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn height="64" width="100" tile class="white--text customGradient" color="#6e4e5d" v-bind="attrs"
                    v-on="on">
                    Menu
                  </v-btn>
                </template>

                <v-btn-toggle v-model="icon" tile dark borderless>
                  <v-btn color="#6e4e5d" value="left" height="64" width="160" @click="$router.push('profile')"
                    class="customGradient">
                    <span class="white--text" style="font-size: 12px">Perfil</span>

                    <v-icon right>
                      mdi-account-circle
                    </v-icon>
                  </v-btn>

                  <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160"
                    class="v-btn--active customGradient">
                    <span class="white--text" style="font-size: 12px">Clientes</span>

                    <v-icon right>
                      mdi-account-group
                    </v-icon>
                  </v-btn>

                  <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160"
                    class="customGradient">
                    <span class="white--text" style="font-size: 12px">Procurar</span>

                    <v-icon right>
                      mdi-magnify
                    </v-icon>
                  </v-btn>

                  <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160"
                    class="customGradient">
                    <span class="white--text" style="font-size: 12px">Configurar</span>

                    <v-icon right>
                      mdi-cog
                    </v-icon>
                  </v-btn>

                </v-btn-toggle>
              </v-menu>
            </template>
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-2 mt-2">

            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
              <v-data-table :headers="headers" :items="allClients" fixed-header hide-default-footer
                tile :search="dessertFilterValue" disable-pagination item-key="id" :height="menu1 == true ? '318' : '400'" style="width: 600px;"
                class="elevation-1 my-header-style">
                <template v-slot:top>

                  <!-- v-container, v-col and v-row are just for decoration purposes. -->
                  <v-container fluid>
                    <v-row>
                      <v-col cols="6">
                        <v-row class="pa-2">
                          <v-text-field style="width: 150px; border-radius: 0px;" v-model="dessertFilterValue"
                            label="Procurar" hide-details outlined dense></v-text-field>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>

                </template>
                <template v-slot:[`item.actions`]="{ item }">
                  <v-icon @click="getSpecificClient(item)">mdi-plus</v-icon>
                </template>
              </v-data-table>
              <v-row>
                <template v-if="menu1">
                  <v-dialog v-model="dialog2" persistent content-class="rounded-0" max-width="500px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn height="50" width="150" class="mt-11 customGradient" dark tile v-bind="attrs" v-on="on"
                        @click="added = false; failed = false"> <span style="font-size: 11px;">Carregar Clientes
                        </span></v-btn>
                    </template>
                    <v-card tile>
                      <v-card-title class="justify-center">
                        <span v-show="!added && !failed">Carregar Clientes</span>
                      </v-card-title>
                      <div align="center">
                        <span class="text-h5" v-show="added"> Clientes carregados com sucesso! </span>
                        <span class="text-h5" v-show="failed"> Ocorreu um erro a carregar os Clientes </span>
                        <v-img :src="myImage2" v-show="!added && !failed" contain height="60px" width="60px"
                          class="mb-6 mt-3" style="cursor:pointer;" @click.stop="handleFileImport"></v-img>
                        <input ref="uploader" class="d-none" type="file" name="file" @change="onFileChanged">
                        <span v-show="!fileSelected && !added && !failed">
                          Selecione um ficheiro
                        </span>
                        <span v-show="fileSelected && !added && !failed">
                          ficheiro selecionado : {{ this.selectedFile.name }}
                        </span>
                      </div>

                      <v-card-actions class="justify-center">
                        <v-btn color="blue darken-1" text
                          @click="dialog2 = false; fileSelected = false; changeSelectedFile()">
                          Voltar
                        </v-btn>
                        <v-btn color="blue darken-1" text v-show="fileSelected && !added && !failed"
                          @click="addClients()">
                          Adicionar
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </template>

              <v-btn v-if="menu1" height="50" width="180" class="mt-11 ml-3 customGradient" dark tile  @click="getRequests()"> 
                <span style="font-size: 11px;">Descarregar pedidos</span> </v-btn>
                    

                <template v-if="menu1">
                  <v-dialog v-model="dialog1" persistent content-class="rounded-0" max-width="500px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn height="50" width="145" class="mb-3 mt-11 ml-3 customGradient" dark tile v-bind="attrs"
                        v-on="on" @click="added = false; failed = false;"> <span style="font-size: 11px;">Adicionar
                          Cliente </span></v-btn>
                    </template>
                    <v-card tile>
                      <v-card-title class="justify-center pt-2 pb-1">
                        <span class="text-h5 pt-1 pb-1" v-show="!added && !failed" > Adicionar Cliente </span>
                      </v-card-title>
                      <v-card-text class="pb-0">
                        <v-container>
                          <v-row justify="center">
                            <span class="text-h5" v-show="added"> Cliente adicionada com sucesso! </span>
                            <span class="text-h5" v-show="failed"> Ocorreu um erro a adicionar o Cliente </span>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Código do cliente" required v-model="clientCode"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Nome do cliente" required v-model="clientName"></v-text-field>
                            </v-col>
                            <v-col cols="12" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Email do cliente" required v-model="clientEmail"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="NIF do cliente" required v-model="clientNIF"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Número de telefone do cliente" required
                                v-model="clientPhone"></v-text-field>
                            </v-col>
                            <v-col cols="12" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Endereço do cliente" required v-model="clientAddress"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Código postal do cliente" required
                                v-model="clientPostalCode"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Banner do cliente" required v-model="clientBanner"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="Cidade do cliente" required
                                v-model="clientCity"></v-text-field>
                            </v-col>
                            <v-col cols="6" v-show="!added && !failed" class="pb-0">
                              <v-text-field label="País do cliente" required
                                v-model="clientCountry"></v-text-field>
                            </v-col>
                          </v-row>
                        </v-container>
                      </v-card-text>
                      <v-card-actions class="justify-center">
                        <v-btn hide-details color="blue darken-1" text @click="dialog1 = false; clientCode = null; clientName = null; clientAddress = null; clientEmail = null; clientNIF = null; clientBanner = null; clientPhone = null; clientPostalCode = null">
                          Voltar
                        </v-btn>
                        <v-btn hide-details color="blue darken-1" text v-show="!added && !failed" @click="addClient();  clientCode = null; clientName = null; clientAddress = null; clientEmail = null; clientNIF = null; clientBanner = null; clientPhone = null; clientPostalCode = null; clientCity = null; clientCountry = null;">
                          Adicionar
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </template>
              </v-row>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" :style="menu1 == true ? 'height: 60px' : 'height: 80px'">
              <v-col cols="auto" class="pl-4">
                <v-btn @click="$router.push('profile')" class="d-flex flex-column customGradient" small dark tile>
                  <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
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

  data() {
    return {
      myImage: require('@/assets/logologo1.png'),
      myImage2: require('@/assets/icon.png'),
      allClients: [],
      selectedBanner: null,
      clientName: null,
      clientEmail: null,
      clientCode: null,
      clientPhone: null,
      clientNIF: null,
      clientCity: null,
      clientCountry: null,
      banners: [],
      dialog1: false,
      dialog2: false,
      dialog3: false,
      selectedFile: { name: "" },
      fileSelected: false,
      added: false,
      failed: false,
      dessertFilterValue: '',
      file: null,
      store,
      estadosList: [
        { text: "Qualquer", value: null },
        { text: "Sim", value: "sim" },
        { text: "Não", value: "não" },
      ],
      marcasList: [
        { text: "Qualquer", value: null },
        { text: "Sim", value: "sim" },
        { text: "Não", value: "não" },
      ],
      // Filter models.
      dates: [],
      menu: false,
      client: new ClientDto(),
      clientPostalCode: null,
      clientBanner: null,
      clientAddress: null,
      menu1: true,
    }
  },
  async created() {
    try {
        this.allClients = await Backend.getClients();
      this.allClients.forEach(element => {
        if (!this.banners.includes(element.banner)) {
          this.banners.push(element.banner)
        }
      });

      store.currentUser = await Backend.getProfile()
      console.log(store.currentUser)
      if(store.currentUser.roles[0] == 'COMMERCIAL' || store.currentUser.roles[0] == 'ADMIN'){ 
        this.menu1 = false
      } 

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
          class: 'my-header-style',
        },
        {
          text: 'Nome',
          value: 'name',
          align: "center",
          class: 'my-header-style',
        },
        { text: "", value: "actions", align: "right", sortable: false, class: 'my-header-style' },
      ]
    },

  },
  methods: {
    handleFileImport() {
      this.$refs.uploader.click();
    },
    changeSelectedFile() {
      this.selectedFile = { name: "" }
    },
    onFileChanged(e) {
      this.selectedFile = null
      this.selectedFile = e.target.files[0];
      this.fileSelected = true
    },
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
        this.client.city = this.clientCity
        this.client.country = this.clientCountry
        await Backend.addClient(this.client)
        this.allClients = await Backend.getClients()
        this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
    },
  
    getSpecificClient(item) {
      this.allClients.forEach(element => {
        if (item.name == element.name) {
          store.currentClient = element
        }
      });
      this.$router.push({ name: 'clientInfo' });
    },

    addClients: async function () {
      try {
        await Backend.addClients(this.selectedFile)
        this.allClients = await Backend.getClients()
        this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
    },
    getRequests: async function () {
      try {
        await Backend.getAllRequests()
        this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
    }
  },
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
