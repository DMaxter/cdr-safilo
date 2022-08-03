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
              <v-btn color="#6e4e5d" value="left" height="60" width="170">
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
          <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto">
                ID do Comerciante
            </v-col>
            <v-col cols="auto" >
            <v-text-field
            v-model= text
            label=""
            placeholder="Nome/Id"
            filled
            rounded
            dense
          ></v-text-field>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Marca
            </v-col>
            <v-col cols="auto" >
          <v-text-field
            v-model= text
            label=""
            placeholder="Marca"
            filled
            rounded
            dense
          ></v-text-field>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Plafond
            </v-col>
            <v-col cols="auto" >
          <v-text-field
            v-model= text
            label=""
            placeholder="Nome/Id"
            filled
            rounded
            dense
          ></v-text-field>
            </v-col>
          </v-row>
          <v-dialog
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-row justify="end" align="end" class="d-flex flex-column mt-4 mr-5">
            <v-col cols="auto">
              <v-btn
              class="d-flex flex-column"
              outlined
              v-bind="attrs"
              v-on="on"
              x-large
              rounded
              color="#6e4e5d"
              @click= "dialog.value = true"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
          </v-row>
        </template>
        <template v-slot:default="dialog">
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12">Alterado com sucesso!</div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog.value = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
          </v-card>
        </v-col>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'

export default {
  name: 'CustomerSearch',

  components: {
  },

  data: () => ({
    store,
    text: "",
    material: "",
    estado: "",
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    items2: ['BACKLIT','BANNER','MICROPERFURADO',
    'PAPEL FOTOGRÁFICO', 'PVC 3 mm', 'PVC 5 mm',
    'TÊXTIL BACKLIT MA', 'VINIL', 'Vinil imp + recort',
    'VINIL INTERIOR MON', 'VINIL METALIZADO', 'VINIL REPOSICIONÁ']
  }),
  methods: { 
    searchFilters() {
      if(this.text == ""){
        console.log("no text")
      } else if (this.text != "") {
        console.log(this.text)
        this.$router.push({name: 'details'})
      }
      if(this.material == ""){
        console.log("no material")
      } else if (this.material != "") {
        store.materialSearch = this.material
        this.$router.push({name: 'searchResults'})
      }
      if(this.estado == ""){
        console.log("no estado")
      } else if (this.estado != "") {
        store.estadoSearch = this.estado
        this.$router.push({name: 'searchResults'})
      }

      // this.$router.push({name: 'searchResults'});
    }  
  }
};
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>