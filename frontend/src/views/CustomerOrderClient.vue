<template>
  <v-app>

    <v-container fill-height>
      <v-row justify="center" align="center">
        <v-col cols="auto">
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
            <v-row no-gutters justify="start" class="pt-2 pl-2">
              <v-menu :offset-x="true" tile>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn height="64" width="100" class="white--text customGradient" tile color="#808080" v-bind="attrs"
                    v-on="on">
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

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="130" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('clients')" height="64" width="130" class="customGradient">
                <span class="white--text" style="font-size: 12px">Imagens</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
              </v-menu>
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              Confirmar a morada de entrega
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
              <v-data-table :headers="headers" :items="allClients" fixed-header disable-pagination
                :search="dessertFilterValue" tile item-key="id" hide-default-footer height="340" style="width: 750px;"
                class="elevation-1 my-header-style">
                <template v-slot:top>
                  <v-container fluid>
                    <v-row>
                      <v-col cols="5">
                        <v-row class="pa-2">
                          <v-text-field style="width: 150px;
                   border-radius: 0px;" v-model="dessertFilterValue" label="Procurar" hide-details outlined
                            dense></v-text-field>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>
                </template>
                <template v-slot:[`item.actions`]="{ item }">
                  <v-icon @click="getSpecificClient(item)">mdi-plus</v-icon>
                </template>
              </v-data-table>
              <v-dialog v-model="dialog1" persistent max-width="500px">
                <v-card>
                  <v-card-title class="justify-center">
                    <span class="text-h5"> Morada do cliente: </span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-row justify="center">
                        <span class="text-h6">{{ this.selectedClient.address }}, {{ this.selectedClient.postalCode }}</span>
                      </v-row>
                    </v-container>
                  </v-card-text>
                  <v-card-actions class="justify-center">
                    <v-btn color="blue darken-1" text @click="dialog1 = false; selectedAdress = null">
                      Voltar
                    </v-btn>
                    <v-btn color="blue darken-1" text @click="nextPage()">
                      Selecionar
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 68px;">
              <v-col cols="auto" class="pl-4">
                <v-btn @click="$router.push('profile')" class="d-flex flex-column customGradient" small tile dark>
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

export default {
  name: 'CustomerHistory',

  components: {
  },

  data() {
    return {
      myImage: require('@/assets/logologo1.png'),
      items: [],
      selectedAdress: null,
      dessertFilterValue: "",
      allClients: [],
      clientName: null,
      clientEmail: null,
      clientCode: null,
      clientPhone: null,
      clientNIF: null,
      dialog1: false,
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
      selectedClient: { address: "" },
    }
  },
  async created() {
    try {
      this.allClients = await Backend.getClients();
      store.selectedMaterial = []
      store.currentBrandId = []
      store.dimensions = []
      store.images = []
      store.isActive1 = false
      store.isActive2 = false
      store.isActive3 = false
      store.isActive4 = false
      store.face1 = null
      store.face2 = null
      store.address = null
      store.uniqueBrands = null
      store.amount = null
      store.observations = null
      store.currentCost = null
      store.application = false
      store.currentClient = null
      store.currentAddress = null
      store.costPerBrand = null
      store.isEditing = false
      store.pedidoAtual = {
        cod: null,
        data: null,
        marca: null,
        modelo: null,
        material: null,
        dimensoes: null,
        estado: null,
        images: null,
        amount: null,
        observations: null,
        cost: null,
        application: null
      },
        store.facesDefault = [
          {
            face: "A",
            link: require('@/assets/A.png'),
          },
          {
            face: "B",
            link: require('@/assets/B.png'),
          },
          {
            face: "C",
            link: require('@/assets/C.png'),
          },
          {
            face: "D",
            link: require('@/assets/D.png'),
          },
          {
            face: "E",
            link: require('@/assets/E.png'),
          },
        ]
      console.log(store)
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
          text: 'Nome',
          value: 'name',
          align: "center",
          class: 'my-header-style'
        },
        {
          text: 'Morada',
          value: 'address',
          align: "center",
          class: 'my-header-style'
        },
        {
          text: 'Cód. Postal',
          value: 'postalCode',
          align: "center",
          class: 'my-header-style'
        },
        { text: "", value: "actions", align: "right", sortable: false, class: 'my-header-style' },
      ]
    },

  },
  methods: {
    getSpecificClient(item) {
      this.selectedClient = item
      this.dialog1 = true
    },

    nextPage() {
      store.currentAddress = this.selectedClient.address
      store.currentClient = this.selectedClient.id
      this.dialog1 = false
      this.$router.push({ name: 'order' });
    }
  }
}
</script>

<style>
#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}

.customGradient {
  background-image: linear-gradient(#616161, grey);
}

.v-data-table .v-data-table__wrapper{
    overflow-x: hidden;
}
</style>
