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
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 330px">
          <v-col cols="auto">
          <v-select style="width: 200px;"
          :items="brands"
          v-model="brand"
          label="Marca"
          class="mt-10"
          dense
          outlined
          hide-details
          ></v-select>
          </v-col>
          <v-col cols="auto" class="d-flex mt-16">
            <v-img :src="myImage" contain height="80px" width="80px" @click.stop="handleFileImport"></v-img>
            <input 
            ref="uploader" 
            class="d-none" 
            type="file" 
            @change="onFileChanged"
            >
                <v-col cols="auto" class="text-decoration-underline mb-15">
                  Escolher Imagem
                </v-col>
          </v-col>
          </v-row>
<v-dialog
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-row justify="end" align="end" class="d-flex flex-column mt-10 mr-5">
            <v-col cols="auto">
              <v-btn
              class="d-flex flex-column mt-16"
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        allBrands: null,
        brands: [],
        brand: null,
        selectedFile: null,
        store,
        myImage: require('@/assets/default-placeholder.png'),
      }
    },

methods: {
  handleFileImport() {
                this.$refs.uploader.click();
            },
            onFileChanged(e) {
                this.selectedFile = e.target.files[0];

                console.log(this.selectedFile)
            },

      getBrands: async function () {
      try {
        this.allBrands = await Backend.getBrands()
        console.log(this.allBrands)
        this.allBrands.forEach(element => {
          this.brands.push(element.name)
        });
        console.log(this.brands)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
},
    created: async function () {
      this.getBrands()
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