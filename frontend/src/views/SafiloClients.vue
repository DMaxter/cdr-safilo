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

              <v-btn-toggle v-model="icon" dark dense mandatory>
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('safiloProfile')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="60" width="170">
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
          {text: "Sim", value: "sim"},
          {text: "Não", value: "não"},
        ],
        marcasList: [
          {text: "Qualquer", value: null},
        ],
        // Filter models.
        marcasFilterValue: null,
        estadosFilterValue: null,

        desserts: [
          {
            name: '1234',
            calories: "novo",
            fat: "xxxxxx - Lisboa",
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
            name: '5678',
            calories: "em produção",
            fat: "yyyyyy - Porto",
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
            name: '9101',
            calories: "novo",
            fat: "zzzzzz - Setubal",
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
        ],
        dates: [],
        menu: false,
      }
    },
    computed: {
      headers() {
        return [
          {
            text: 'Código',
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
            text: 'Nome',
            value: 'fat',
            align: "center",
            filter: this.marcasFilter,

          },
          { text: "", value: "actions", sortable: false },
          { text: "", value: "modelo", align: ' d-none', sortable: false },
          { text: "", value: "material", align: ' d-none', sortable: false },
          { text: "", value: "dimensões", align: ' d-none', sortable: false },
        ]
      },
      
    },
    methods: {
      limiter(e) {
        if(e.length > 2) {
          e.pop()
        }
      },
      getRequest(item) {
        console.log(item)
        this.$router.push({name: 'clientInfo'});
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