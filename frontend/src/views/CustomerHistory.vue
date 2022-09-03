<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="space-between" class="pt-2 pl-2 pr-2">
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

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" class="v-btn--active">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('order')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          <v-menu
            :offset-y="true"
            left
            tile
            scale
            :close-on-content-click="false"
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
                Filtros
              </v-btn>
            </template>
            <v-card>
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

                    <v-col cols="7">
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

                </v-row>
            </v-container>
          </v-card>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="desserts" fixed-header item-key="name" hide-default-footer height="380" style="width: 600px;" class="elevation-1 my-header-style">
        
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="getRequest(item)">mdi-plus</v-icon>
          </template>
    </v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 80px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profileComercial')"
              class="d-flex flex-column"
              outlined
              rounded
              color="#6e4e5d"
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
              class="d-flex flex-column"
              outlined
              rounded
              color="#6e4e5d"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          </v-card>
        </v-col>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        store,
        estadosList: [
          {text: "Qualquer", value: null},
          {text: "Novo", value: "novo"},
          {text: "Em Produção", value: "em produção"},
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
      }
    },
    computed: {
      headers() {
        return [
          {
            text: 'COD/DATA/NUM',
            align: 'left',
            sortable: false,
            value: 'name',
            filter: this.dateFilter,
            class: 'my-header-style'
          },
          {
            text: 'Estado',
            value: 'calories',
            align: ' d-none',
            filter: this.estadoFilter,
          },
          {
            text: 'Marca',
            value: 'fat',
            filter: this.marcasFilter,
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
        store.pedidoAtual = {
            cod: item.name.split(" ")[0],
            data: item.name.split(" ")[1],
            marca: item.fat,
            modelo: item.modelo,
            material: item.material,
            dimensoes: item.dimensões,
            estado: item.calories,
            images: item.images,
            quantity: item.quantidade,
            observations: item.observacoes,
            cost: item.custo,
            application: item.aplicacao
          },
  
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
        return value.toLowerCase().includes(this.dessertFilterValue.toLowerCase());
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
        var converted = new Date(gaga[1])
        var date1
        var date2
        if(new Date(this.dates[0]).setHours(0,0,0,0) > new Date(this.dates[1]).setHours(0,0,0,0)){
          date1 = new Date(this.dates[1]).setHours(0,0,0,0)
          date2 = new Date(this.dates[0]).setHours(0,0,0,0)
        } else { 
          date1 = new Date(this.dates[0]).setHours(0,0,0,0)
          date2 = new Date(this.dates[1]).setHours(0,0,0,0)
        }
        return converted >= date1 && converted <= date2
       }
    }
  }
</script>

<style>

.my-header-style {
  background-color: rgb(225, 225, 225) !important;
}
#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>