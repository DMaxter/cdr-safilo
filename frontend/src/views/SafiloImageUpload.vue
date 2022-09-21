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
              <v-btn color="#6e4e5d" value="left" height="64" width="170" @click="$router.push('profileSafilo')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="170" class="customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="v-btn--active customGradient">
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
          <v-row justify="end" align="end" class="d-flex flex-column mt-15 mr-5">
            <v-col cols="auto">
              <v-btn
              class="d-flex flex-column mt-16 customGradient"
              v-bind="attrs"
              v-on="on"
              dark
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
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage2" contain height="180" width="180"></v-img>
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
        allBrands: null,
        myImage2: require('@/assets/logologo1.png'),
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
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>