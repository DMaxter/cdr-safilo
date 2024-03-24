<template>
  <v-app>

    <v-container fill-height>
      <v-row justify="center" align="center">
        <v-col cols="auto">
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
            <v-row no-gutters justify="start" class="pt-2 pl-2">
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
                    class="customGradient">
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
                    class="customGradient v-btn--active">
                    <span class="white--text" style="font-size: 12px">Configurar</span>

                    <v-icon right>
                      mdi-cog
                    </v-icon>
                  </v-btn>

                </v-btn-toggle>
              </v-menu>
            </v-row>
            <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 380px">
              <v-col cols="auto">
                <v-select style="width: 200px;" :items="brands" v-model="brand" label="Marca" class="mt-16" dense outlined
                  hide-details></v-select>
              </v-col>
              <!-- <v-col cols="auto" class="d-flex mt-16">
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
          </v-col> -->
              <v-col class="mt-16 d-flex flex-column">
                Link para a imagem:
                <v-col cols="auto" align="center">
                  <v-text-field style="width: 400px;" v-model="image" dense outlined hide-details></v-text-field>
                </v-col>
              </v-col>
              <v-btn class="d-flex flex-column customGradient" tile small dark @click="dialog = true;">
                Ver Imagens
              </v-btn>
            </v-row>
            <v-dialog v-model="dialog" persistent max-width="800">
              <v-card>
                <v-card-title class="text-h5">
                  Imagens
                </v-card-title>

                <v-autocomplete style="width: 200px;" :items="brands" v-model="brandFilter" label="Marca"
                  class="ml-6 mt-1 mb-1" dense outlined hide-details @change="loadImages()"></v-autocomplete>

                <v-card-text>
                  <v-item-group active-class="selected" v-model="picked">
                    <v-container>
                      <v-row>
                        <v-col v-for="(image, i) in images" :key="i" cols="3">
                          <v-item v-slot="{ active, toggle }">
                            <v-img :content-class="{ obsolete: image.obsolete }" contain :src=image.link height="150"
                              @click="toggle">
                              <v-scroll-y-transition>
                                <div v-if="active" class="text-h2 flex-grow-1 text-center selected">
                                  <span style="opacity: 0">Ahahahahahaha</span>
                                </div>
                              </v-scroll-y-transition>
                            </v-img>
                          </v-item>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-item-group>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>

                  <v-btn text @click="dialog = false; picked = null; images = defaultImages; brandFilter = null;">
                    Voltar
                  </v-btn>

                  <v-btn v-show="picked != null" text @click="deleteImage()">
                    Apagar
                  </v-btn>

                  <v-btn v-show="picked != null" text @click="obsoleteImage()">
                    Obsoleto
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
            <v-dialog transition="dialog-bottom-transition" max-width="600" id="dialogo">
              <template v-slot:activator="{ on, attrs }">
                <v-row justify="space-between" align="center" class="d-flex" style="height: 188px;">
                  <v-col cols="auto pl-7">
                    <v-btn @click="$router.push('configure')" class="d-flex flex-column customGradient mt-16" tile small
                      dark> <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
                      Voltar
                    </v-btn>
                  </v-col>
                  <v-col cols="auto pr-7">
                    <v-btn class="d-flex flex-column mt-16 customGradient" v-bind="attrs" v-on="on" dark tile small
                      @click="addImage()"> Confirmar <v-icon>mdi-play</v-icon>
                    </v-btn>
                  </v-col>
                </v-row>
              </template>
              <template v-slot:default="dialog">
                <v-card>
                  <v-card-text>
                    <div class="text-h6 pt-12" v-show="success"> Imagem adicionada com sucesso!</div>
                    <div class="text-h6 pt-12" v-show="failed"> Ocorreu um erro a adicionar a imagem </div>

                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn text @click="dialog.value = false; success = false; failed = false">Voltar</v-btn>
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
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'
//import axios from 'axios'

export default {
  name: 'CustomerHistory',

  components: {
  },

  data() {
    return {
      allBrands: null,
      myImage2: require('@/assets/logologo1.png'),
      brands: [],
      brand: null,
      selectedFile: null,
      brandFilter: null,
      store,
      success: false,
      failed: false,
      image: null,
      defaultImages: [],
      picked: null,
      images: null,
      dialog: false,
      myImage: require('@/assets/default-placeholder.png'),
    }
  },

  methods: {
    loadImages() {
      this.allBrands.forEach(element => {
        if (this.brandFilter == element.name) {
          this.images = element.images
        }
      });
    },

    async obsoleteImage() {
      console.log(this.images[this.picked].id)
      await Backend.obsoleteImage(this.images[this.picked].id)
      this.getBrands();
    },
    async deleteImage() {
      await Backend.deleteImage(this.images[this.picked].id)
      this.picked = null; this.brandFilter = null;
      this.getBrands();
    },
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
        this.defaultImages = []
        this.brands = []
        this.allBrands.forEach(element => {
          this.brands.push(element.name)
          if (element.images.length > 0) {
            element.images.forEach(element2 => {
              this.defaultImages.push(element2);
            });
          }
        });
        console.log(this.brands)
        this.images = this.defaultImages
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    async addImage() {
      console.log(this.picked)
      var done = false
      this.allBrands.forEach(async element => {
        if (element.name == this.brand) {
          done = true
          var link = "";
          if (String(this.image).includes("https://drive.google.com/file/d/") && String(this.image).includes("/view?usp=sharing")) {
            var code = String(this.image).replace('file/d/', 'thumbnail?id=');
            code = code.replace('/view?usp=sharing', '')
            link = code + "&sz=w1080"
          } else {
            link = String(this.image)
          }

          await Backend.addImage(element.id, link)
          this.getBrands()
        }
      });
      if (done) {
        this.success = true
      } else {
        this.failed = true
      }
    }
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

.selected {
  background-image: linear-gradient(to top, #F0E68C 0%, transparent 72px);
}

.obsolete {
  background-image: linear-gradient(90deg, rgba(139, 139, 139, 0.5) 0%, rgba(139, 139, 139, 0.5) 100%),
    linear-gradient(to top right,
      rgba(0, 0, 0, 0) 0%,
      rgba(0, 0, 0, 0) calc(50% - 3.0px),
      rgba(0, 0, 0, 1) 50%,
      rgba(0, 0, 0, 0) calc(50% + 3.0px),
      rgba(0, 0, 0, 0) 100%);
}


.customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>
