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
              <v-btn color="#6e4e5d" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="end" align="center" class="fill-height d-flex flex-column" style="height: 400px">
          <h4> Código - Nome - Banner</h4>
          <h4 class="mb-1"> {{store.currentClient.id}} - {{store.currentClient.name}} - {{store.currentClient.banner}} </h4>
          <h4 class="mt-1"> Morada - Código Postal: </h4>
          <h4 class="mb-1"> {{store.currentClient.address}} - {{store.currentClient.postalCode}} </h4>
          <h4 class="mt-1"> Email - Telefone: </h4>
          <h4 class="mb-3"> {{store.currentClient.email}} - {{store.currentClient.phone}} </h4>
          <v-btn height="60" width="500" dark tile class="mb-3 customGradient" @click="$router.push('clientHistory')"> Histórico </v-btn>
          <template>
                  <v-dialog v-model="dialog" persistent content-class="rounded-0" max-width="500px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn height="60" width="500" class="mb-3 customGradient" dark tile v-bind="attrs" v-on="on"
                        @click="added = false; failed = false"> Adicionar Imagem
                        </v-btn>
                    </template>
                    <v-card tile>
                      <v-card-title class="justify-center">
                        <span v-show="!added && !failed">Adicionar Imagem</span>
                      </v-card-title>
                      <div align="center">
                        <span class="text-h5" v-show="added"> Imagem adicionada com sucesso! </span>
                        <span class="text-h5" v-show="failed"> Ocorreu um erro a adicionar a imagem </span>
                        <v-img :src="myImage2" v-show="!added && !failed" contain height="60px" width="60px"
                          class="mb-6 mt-3" style="cursor:pointer;" @click.stop="handleFileImport"></v-img>
                        <input ref="uploader" class="d-none" type="file" name="file" @change="onFileChanged">
                        <span v-show="!fileSelected && !added && !failed">
                          Selecione um ficheiro
                        </span>
                        <span v-show="fileSelected && !added && !failed">
                          ficheiro selecionado : {{ this.selectedFile.name }}
                        </span>
                      </div>

                      <v-card-actions class="justify-center">
                        <v-btn color="blue darken-1" text
                          @click="dialog = false; fileSelected = false; changeSelectedFile()">
                          Voltar
                        </v-btn>
                        <v-btn color="blue darken-1" text v-show="fileSelected && !added && !failed"
                          @click="addImageToClient()">
                          Adicionar
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </template>
                <v-btn
              class="customGradient"
              height="60" width="500" dark tile
              @click="loadImages(); dialog2 = true;"
            >
            Ver Imagens
            </v-btn>
            <v-dialog
      v-model="dialog2"
      persistent
      max-width="800"
    >
      <v-card>
        <v-card-title class="text-h5">
          Imagens
        </v-card-title>
      
        <v-card-text>
          <v-item-group active-class="selected" v-model="picked">
    <v-container>
      <v-row>
        <v-col
          v-for="(image, i) in images"
          :key="i"
          cols="3"
        >
          <v-item v-slot="{ active, toggle }">
            <v-img
              :content-class = "{ obsolete:  image.obsolete}"
              contain
              :src= image.link
              height="150"
              @click="toggle"
            >
                <v-scroll-y-transition>
                <div
                  v-if="active"
                  class="text-h2 flex-grow-1 text-center selected"
                >
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

          <v-btn
            text
            @click="dialog2 = false; picked = null; "
          >
            Voltar
          </v-btn>

          <v-btn
            v-show="picked != null"
            text
            @click="deleteImage()"
          >
            Apagar
          </v-btn>

          <v-btn
            v-show="picked != null"
            text
            @click="obsoleteImage()"
          >
            Obsoleto
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 120px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('clients')"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
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
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
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
        images: null,
        myImage: require('@/assets/logologo1.png'),
        myImage2: require('@/assets/icon.png'),
        selectedFile: { name: "" },
        fileSelected: false,
        added: false,
        failed: false,
        dialog: false,
        dialog2: false
      }
    },

methods: {
  loadImages() {
    this.images = store.currentClient.images
    console.log(this.images)
  },
  getPedido() {
    if(store.pedidoAtual.modelo == "montra"){
      this.$router.push({name: 'detailsMontra'});
    } else {
      this.$router.push({name: 'detailsOneOrTwo'});
    }
  },
  handleFileImport() {
      this.$refs.uploader.click();
    },
    changeSelectedFile() {
      this.selectedFile = { name: "" }
    },
    onFileChanged(e) {
      this.selectedFile = null
      this.selectedFile = e.target.files[0];
      this.fileSelected = true
    },
    async addImageToClient() {
      try{
        console.log(store.currentClient.id, this.selectedFile)
        await Backend.addImageToClient(store.currentClient.id, this.selectedFile)
        this.added = true
      } catch (error) {
        this.failed = true
        console.log(error)
      }
  }
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