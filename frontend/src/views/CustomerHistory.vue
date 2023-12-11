<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="space-between" class="pt-2 pl-2 pr-2">
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
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="130" class=" customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="130" class="v-btn--active customGradient">
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

              <v-btn color="#808080" value="right" @click="$router.push('clients')" height="64" width="130" class="customGradient">
                <span class="white--text" style="font-size: 12px">Imagens</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          <v-menu
            :offset-x="true"
            left
            tile
            scale
            :close-on-content-click="false"
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
                Filtros
              </v-btn>
            </template>
            <v-card tile max-width="600">
             <v-container fluid>
                <v-row>

                    <v-col cols="5">
                        <v-row class="pa-2">
                             <v-select
                                    style="width: 150px;"
                                    :items="marcasList"
                                    v-model="marcasFilterValue"
                                    label="Marca"
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
                                    label="Estado"
                            ></v-select>
                        </v-row>
                    </v-col>

                    <v-col cols="5">
                        <v-row class="pa-2">
                            <!-- Filter for calories -->
                            <v-menu
                              ref="menu"
                              v-model="menu"
                              :close-on-content-click="false"
                              :return-value.sync="dates"
                              transition="scale-transition"
                              offset-y
                              min-width="auto"
                            >
                              <template v-slot:activator="{ on, attrs }">
                                <v-combobox
                                  v-model="dates"
                                  multiple
                                  chips
                                  small-chips
                                  label="Data"
                                  prepend-icon="mdi-calendar"
                                  readonly
                                  v-bind="attrs"
                                  v-on="on"
                                ></v-combobox>
                              </template>
                              <v-date-picker
                                v-model="dates"
                                range
                                v-on:input="limiter"
                                no-title
                                scrollable
                              >
                                <v-spacer></v-spacer>
                                <v-btn
                                  text
                                  color="primary"
                                  @click="menu = false; dates = []; $refs.menu.save(dates);"
                                >
                                  Cancel
                                </v-btn>
                                <v-btn
                                  text
                                  color="primary"
                                  @click="$refs.menu.save(dates);"
                                >
                                  OK
                                </v-btn>
                              </v-date-picker>
                            </v-menu>
                        </v-row>
                    </v-col>
                    <v-col cols="4">
                        <v-row class="pa-2">
                            <!-- Filter for calories -->
                            <v-text-field
                                    style="width: 150px;"
                                    v-model="dessertFilterValue"
                                    label="Cliente"
                            ></v-text-field>
                        </v-row>
                    </v-col>
                    <v-col cols="4">
                        <v-row class="pa-2">
                            <!-- Filter for calories -->
                            <v-text-field
                                    style="width: 150px;"
                                    v-model="nomeFilterValue"
                                    label="Comercial"
                            ></v-text-field>
                        </v-row>
                    </v-col>

                </v-row>
            </v-container>
          </v-card>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
            </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="requests" fixed-header item-key="id" disable-pagination hide-default-footer height="380" style="width: 750px;" class="elevation-1 my-header-style mt-5">
        
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="getRequest(item)" class="mr-1">mdi-plus</v-icon>
            <v-icon @click="getRequestNewTab(item)" class="ml-1">mdi-open-in-new</v-icon>
            <v-icon @click="dialog = true; reqtocan = item" class="ml-1">mdi-close</v-icon>
            <v-icon @click="editRequest(item)" class="ml-1">mdi-pen</v-icon>
          </template>
    </v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 90px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient"
              small
              tile
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
    <v-dialog
      v-model="dialog"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
  
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added && !failed"> Cancelar pedido </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <span class="text-h5" v-show="added"> Pedido cancelado com sucesso! </span>
              <span class="text-h5" v-show="failed"> Ocorreu um erro a cancelar o pedido </span>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added && !failed && !toDelete && !toDelete2"
            @click="cancelRequest(reqtocan)"
          >
            Cancelar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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

data () {
      return {
        store,
        dialog: false,
        reqtocan: null,
        added: false,
        failed: false,
        myImage: require('@/assets/logologo1.png'),
        estadosList: [
          {text: "Qualquer", value: null},
          {text: "Encomendado", value: "ORDERED"},
          {text: "Em Produção", value: "IN_PRODUCTION"},
          {text: "Terminado", value: "DONE"},
          {text: "Cancelado", value: "CANCELLED"},
        ],
        marcasList: [{text: "Qualquer", value: null}],
        // Filter models.
        marcasFilterValue: null,
        estadosFilterValue: null,
        dessertFilterValue: null,
        nomeFilterValue: null,
        desserts: [
          {
            name: 'FCH 2022.06.05 1254',
            calories: "novo",
            fat: "HUGO BOSS",
            modelo: "montra",
            material: ["pvc", "pvc2", "pvc3", "pvc4", "pvc5"],
            dimensões: ["60 x 40", "100 x 50", "100 x 100", "30 x 90", "120 x 80"],
            images: [
              {
                src: require('@/assets/uno.jpg'),
              },
                    {
                src: require('@/assets/trese.png'),
              },
                    {
                src: require('@/assets/quatre.png'),
              },
                    {
                src: require('@/assets/dos.jpg'),
              },
                    {
                src: require('@/assets/dos.jpg'),
              },
            ],
            quantidade: 1,
            observacoes: "Por favor despachem-se",
            custo: 300,
            aplicacao: true, 
          },
           {
            name: 'FCH 2022.06.05 1254',
            calories: "novo",
            fat: "HUGO BOSS",
            modelo: "montra",
            material: ["pvc", "pvc2", "pvc3", "pvc4", "pvc5"],
            dimensões: ["60 x 40", "100 x 50", "100 x 100", "30 x 90", "120 x 80"],
            images: [
              {
                src: require('@/assets/uno.jpg'),
              },
                    {
                src: require('@/assets/trese.png'),
              },
                    {
                src: require('@/assets/quatre.png'),
              },
                    {
                src: require('@/assets/dos.jpg'),
              },
                    {
                src: require('@/assets/dos.jpg'),
              },
            ],
            quantidade: 1,
            observacoes: "Por favor despachem-se",
            custo: 300,
            aplicacao: true, 
          },
          {
            name: 'ABC 2022.06.01 1245',
            calories: "em produção",
            fat: "CAROLINA HERRERA",
            modelo: "normal",
            material: ["laminado"],
            dimensões: ["100 x 50"],
            images: [
                    {
                src: require('@/assets/quatre.png'),
              },
            ],
            quantidade: [2],
            observacoes: "Por favor despachem-se",
            custo: 400,
            aplicacao: [false], 
          },
          {
            name: 'DEF 2022.06.08 1425',
            calories: "novo",
            fat: "HUGO BOSS",
            modelo: "normal",
            material: ["laminado", "backlit"],
            dimensões: ["100 x 50", "80 x 40"],
            images: [
              {
                src: require('@/assets/quatre.png'),
              },
              {
                src: require('@/assets/trese.png'),
              },
            ],
            quantidade: [3, 4],
            observacoes: "Por favor despachem-se",
            custo: 400,
            aplicacao: [true, false], 
          },
          {
            name: 'GH1 2022.06.15 1524',
            calories: "em produção",
            fat: "TOMMY HILFIGER",
            modelo: "montra",
            material: "pvc",
            dimensões: "70 x 40"            
          }
        ],
        dates: [],
        menu: false,
        requests: [],
        allBrands: null,
      }
    },
    async created() {
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
      store.quantity = null
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
        quantity: null,
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
      this.requests = await Backend.getRequests()
      this.allBrands = await Backend.getBrands()
      this.allBrands.forEach(element => {
          this.marcasList.push({ text: element.name, value: element.name })
        });
      console.log(this.marcasList)
      this.requests.forEach(element => {
        var date = element.created
        date = date.slice(0, 10)
        element.created = date
        element.cost = element.cost.toFixed(2)
      });
      console.log(this.requests)
    },
    computed: {
      headers() {
        return [
          {
            text: 'ID', //replace with tracking code
            align: 'left',
            sortable: false,
            value: 'id',
            class: 'my-header-style'
          },
          {
            text: 'Estado',
            value: 'status',
            class: 'my-header-style',
            filter: this.estadoFilter,
          },
          {
            text: 'Cliente',
            value: 'client.name',
            filter: this.nameFilter,
            class: 'my-header-style'

          },
          {
            text: 'Comerciante',
            value: 'user',
            filter: this.nomeFilter,
            class: 'my-header-style'

          },
          {
            text: 'Data',
            value: 'created',
            filter: this.dateFilter,
            class: 'my-header-style'

          },
          {
            text: 'Custo',
            value: 'cost',
            class: 'my-header-style'

          },
          { text: "", value: "actions", sortable: false, class: 'my-header-style'},
          { text: "", value: "modelo", align: ' d-none', sortable: false },
          { text: "", value: "brand.name", align: ' d-none', sortable: false, filter: this.marcasFilter},
          { text: "", value: "material", align: ' d-none', sortable: false },
          { text: "", value: "dimensões", align: ' d-none', sortable: false },
        ]
      },
      
    },
    methods: {
      limiter() {
        if(this.dates.length > 2) {
          this.dates = []
          this.$refs.menu.save(this.dates);
        }
      },
      editRequest(item) {
        store.currentRequest = item
        store.isEditing = true
        if(item.type.type == "OneFace"){
          store.isActive1 = true
          this.$router.push({ name: 'order2' });
        } else if(item.type.type == "TwoFaces"){
          store.isActive1 = true
          this.$router.push({ name: 'order2' });

        } else if(item.type.type == "SimpleShowcase"){
          store.isActive4 = true
          this.$router.push({ name: 'ABC' });

        } else if(item.type.type == "RightShowcase"){
          store.isActive2 = true
          this.$router.push({ name: 'ABC' });

        } else if(item.type.type == "LeftShowcase"){
          store.isActive3 = true
          this.$router.push({ name: 'ABC' });

        }
      },
      async cancelRequest(item) {
        try{
          await Backend.cancelRequest(item.id)
          this.added = true
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
      this.reqtocan = null
      },
      getRequest(item) {
        console.log(item)
        store.address = item.client.address
        store.postalCode = item.client.postalCode
        if(item.type.type == "OneFace"){
        store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.cover.material.name],
            dimensoes: [item.type.cover.measurements],
            estado: item.status,
            images: [item.type.cover.image.link],
            quantity: item.quantidade,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: item.type.cover.finishings,
            codClient: item.client.id,
            nameClient: item.client.name
          }
        } else if(item.type.type == "TwoFaces"){
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.cover.material.name, item.type.back.material.name],
            dimensoes: [item.type.cover.measurements, item.type.back.measurements],
            estado: item.status,
            images: [item.type.cover.image.link, item.type.back.image.link],
            quantity: item.quantidade,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.cover.finishings, item.type.back.finishings],
            codClient: item.client.id,
            nameClient: item.client.name
          }
        } else if(item.type.type == "RightShowcase" || item.type.type == "LeftShowcase"){
            if(item.type.type == "RightShowcase"){
              store.isActive2 = true
            } else if(item.type.type == "LeftShowcase"){
              store.isActive3 = true
            }
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.top.material.name, item.type.bottom.material.name, item.type.left.material.name, item.type.right.material.name, item.type.side.material.name],
            dimensoes: [item.type.top.measurements, item.type.bottom.measurements, item.type.left.measurements, item.type.right.measurements, item.type.side.measurements],
            estado: item.status,
            images: [item.type.top.image.link, item.type.bottom.image.link, item.type.left.image.link, item.type.right.image.link, item.type.side.image.link],
            quantity: item.quantidade,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.top.finishings, item.type.bottom.finishings, item.type.left.finishings, item.type.right.finishings, item.type.side.finishings],
            codClient: item.client.id,
            nameClient: item.client.name

          }
        } else if(item.type.type == "SimpleShowcase"){
          store.isActive4 = true
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.top.material.name, item.type.bottom.material.name, item.type.left.material.name, item.type.right.material.name],
            dimensoes: [item.type.top.measurements, item.type.bottom.measurements, item.type.left.measurements, item.type.right.measurements],
            estado: item.status,
            images: [item.type.top.image.link, item.type.bottom.image.link, item.type.left.image.link, item.type.right.image.link],
            quantity: item.quantidade,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.top.finishings, item.type.bottom.finishings, item.type.left.finishings, item.type.right.finishings],
            codClient: item.client.id,
            nameClient: item.client.name

          }
        }
        this.$router.push({name: 'details'});
      },
      getRequestNewTab(item) {
        let route = this.$router.resolve({name: 'details', query: {id: item.id}});
        window.open(route.href, '_blank')
      },
      /**
       * Filter for dessert names column.
       * @param value Value to be tested.
       * @returns {boolean}
       */
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
            /**
       * Filter for dessert names column.
       * @param value Value to be tested.
       * @returns {boolean}
       */
       nomeFilter(value) {
        // If this filter has no value we just skip the entire filter.
        if (!this.nomeFilterValue) {
          return true;
        }
        // Check if the current loop value (The dessert name)
        // partially contains the searched word.
        console.log(value)
        console.log(this.nomeFilterValue)
        return value.toLowerCase().includes(this.nomeFilterValue.toLowerCase());
      },
        estadoFilter(value) {
        // If this filter has no value we just skip the entire filter.
        if (!this.estadosFilterValue) {
          return true;
        }
        // Check if the current loop value (The calories value)
        // equals to the selected value at the <v-select>.
        return value.toLowerCase().includes(this.estadosFilterValue.toLowerCase());
      },
        marcasFilter(value) {
        // If this filter has no value we just skip the entire filter.
        if (!this.marcasFilterValue) {
          return true;
        }
        // Check if the current loop value (The calories value)
        // equals to the selected value at the <v-select>.
        return value.includes(String(this.marcasFilterValue))
      },
       dateFilter (value) {
        if (this.dates.length != 2) {
          return true;
        }
        var gaga = value.split(" ")
        console.log(gaga)
        console.log(value)
        var converted = new Date(gaga).setHours(0,0,0,0)
        var date1
        var date2
        if(new Date(this.dates[0]).setHours(0,0,0,0) > new Date(this.dates[1]).setHours(0,0,0,0)){
          date1 = new Date(this.dates[1]).setHours(0,0,0,0)
          date2 = new Date(this.dates[0]).setHours(0,0,0,0)
        } else { 
          date1 = new Date(this.dates[0]).setHours(0,0,0,0)
          date2 = new Date(this.dates[1]).setHours(0,0,0,0)
        }
        console.log(date1, date2, converted)
        return converted >= date1 && converted <= date2
       }
    },
  }
</script>

<style>
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}
#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
</style>
