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
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('profile')">
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
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
        <v-data-table :headers="headers" :items="desserts" fixed-header item-key="name" hide-default-footer height="210" style="width: 600px;" class="elevation-1">
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
                                multiple
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

        </template>
    </v-data-table>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 80px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
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

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
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
          },
          {
            name: 'ABC 2022.06.01 1245',
            calories: "em produção",
            fat: "CAROLINA HERRERA",
          },
          {
            name: 'DEF 2022.06.08 1425',
            calories: "novo",
            fat: "HUGO BOSS",
          },
          {
            name: 'GH1 2022.06.15 1524',
            calories: "em produção",
            fat: "TOMMY HILFIGER",
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

          },
        ]
      },
      
    },
    methods: {
      limiter(e) {
        if(e.length > 2) {
          e.pop()
        }
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
        console.log(converted.setHours(0,0,0,0))
        console.log(new Date(this.dates[0]))
        return converted >= (new Date(this.dates[0]).setHours(0,0,0,0)) && converted <= (new Date(this.dates[1]).setHours(0,0,0,0))
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