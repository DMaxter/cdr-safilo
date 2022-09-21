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
                                  @click="menu = false"
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
                Nome Comerciante
              </v-row>
              <v-row justify="center" align="center" class="d-flex flex-column mt-2">
          <v-data-table :headers="headers" :items="requests" fixed-header item-key="name" hide-default-footer height="380" style="width: 800px;" class="elevation-1 my-header-style">
          
          <template v-slot:[`item.actions`]="{ item }">
              <v-icon @click="getRequest(item)">mdi-plus</v-icon>
            </template>
      </v-data-table>
              </v-row>
              <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 90px;">
             <v-col cols="auto" class="pl-4">
              <v-btn
                @click="$router.push('profileSafilo')"
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
          store,
          estadosList: [
            {text: "Qualquer", value: null},
            {text: "Encomendado", value: "ORDERED"},
            {text: "Em Produção", value: "IN_PRODUCTION"},
            {text: "Terminado", value: "DONE"},
            {text: "Cancelado", value: "CANCELLED"},
          ],
          marcasList: [
            {text: "Qualquer", value: null},
            {text: "HUGO BOSS", value: "hugo boss"},
            {text: "CAROLINA HERRERA", value: "carolina herrera"},
            {text: "TOMMY HILFIGER", value: "tommy hilfiger"},
          ],
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
          myImage: require('@/assets/logologo1.png'),
        }
      },
      async created() {
        this.requests = await Backend.getRequests()
        this.requests.forEach(element => {
          var date = element.created
          date = date.slice(0, 10)
          element.created = date
        });
        this.dessertFilterValue = store.currentClient.name
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
              value: 'client',
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
            { text: "", value: "actions", sortable: false, class: 'my-header-style'},
            { text: "", value: "modelo", align: ' d-none', sortable: false },
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
        getRequest(item) {
          console.log(item)
          if(item.type.type == "OneFace"){
          store.pedidoAtual = {
              cod: item.id,
              data: item.created,
              marca: [item.type.cover.brand.name],
              modelo: item.type.type,
              material: item.type.cover.material.name,
              dimensoes: item.type.cover.measurements,
              estado: item.status,
              images: [item.type.cover.image.link],
              quantity: item.quantidade,
              observations: item.observations,
              cost: item.cost,
              application: item.application
            }
          } else if(item.type.type == "TwoFaces"){
            store.pedidoAtual = {
              cod: item.id,
              data: item.created,
              marca: [item.type.cover.brand.name, item.type.back.brand.name],
              modelo: item.type.type,
              material: [item.type.cover.material.name, item.type.back.material.name],
              dimensoes: [item.type.cover.measurements, item.type.back.measurements],
              estado: item.status,
              images: [item.type.cover.image.link, item.type.back.image.link],
              quantity: item.quantidade,
              observations: item.observations,
              cost: item.cost,
              application: item.application
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
              marca: [item.type.top.brand.name, item.type.bottom.brand.name, item.type.left.brand.name, item.type.right.brand.name, item.type.side.brand.name],
              modelo: item.type.type,
              material: [item.type.top.material.name, item.type.bottom.material.name, item.type.left.material.name, item.type.right.material.name, item.type.side.material.name],
              dimensoes: [item.type.top.measurements, item.type.bottom.measurements, item.type.left.measurements, item.type.right.measurements, item.type.side.measurements],
              estado: item.status,
              images: [item.type.top.image.link, item.type.bottom.image.link, item.type.left.image.link, item.type.right.image.link, item.type.side.image.link],
              quantity: item.quantidade,
              observations: item.observations,
              cost: item.cost,
              application: item.application
            }
          }
          this.$router.push({name: 'details'});
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
          return value.toLowerCase().includes(this.marcasFilterValue.toLowerCase());
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
  
  .my-header-style {
    background-color: rgb(225, 225, 225) !important;
  }
  #app {
    background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
  }
  .customGradient {
  background-image: linear-gradient(#616161, grey);
}

  </style>