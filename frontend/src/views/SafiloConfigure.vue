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

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="fill-height d-flex flex-column mt-16" style="height: 370px">
          <v-btn height="60" width="500" class="mb-3 customGradient" dark tile @click="$router.push('imageUpload')"> Imagens </v-btn>
          <v-btn height="60" width="500" class="mb-3 customGradient" dark tile @click="$router.push('plafondChange')"> Plafond </v-btn>
          <v-btn height="60" width="500" class="mb-3 customGradient" dark tile @click="$router.push('brandChange')"> Marcas </v-btn>
          <v-dialog
      v-model="dialog1"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn height="60" width="500" class="mb-3 customGradient" dark tile v-bind="attrs" v-on="on" @click="added = false; failed = false"> Registar Comercial </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added & !failed"> Registar Comercial </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <span class="text-h5" v-show="added"> Comercial registado com sucesso! </span>
              <span class="text-h5" v-show="failed"> Ocorreu um erro a registar o comercial </span>
              <v-col
                cols="8"
                v-show="!added & !failed"
              >
                <v-text-field
                  label="Email"
                  required
                  v-model="comercialEmail"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added & !failed"
              >
                <v-text-field
                  label="Nome"
                  required
                  v-model="comercialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added & !failed"
              >
                <v-text-field
                  label="Palavra Passe"
                  required
                  v-model="password1"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added & !failed"
              >
                <v-text-field
                  label="Confirmar Palavra Passe"
                  required
                  v-model="password2"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog1 = false; comercialEmail = null; comercialName = null; password1 = null; password2 = null"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added & !failed"
            @click="addComercial()"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="dialog2"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn height="60" width="500" class="mb-3 customGradient" dark tile v-bind="attrs" v-on="on" @click="added = false; failed = false"> Carregar Clientes </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          Carregar Clientes
        </v-card-title>
        <div align="center">
          <v-img :src="myImage2" contain height="60px" width="60px" class="mb-6 mt-3" @click.stop="handleFileImport"></v-img>
          <input 
          ref="uploader" 
          class="d-none" 
          type="file"
          name="file" 
          @change="onFileChanged"
          >
          <span v-show="!fileSelected">
          Selecione um ficheiro
          </span>
          <span v-show="fileSelected">
          ficheiro selecionado : {{this.selectedFile.name}}
          </span>
        </div>
  
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog2 = false; fileSelected = false; changeSelectedFile()"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="fileSelected"
            @click="addClients()"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 98px;">
             <v-col cols="auto" class="pl-4">
              <v-btn
                @click="$router.push('profile')"
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
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'
import RegisterDto from '@/models/RegisterDto';

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        myImage2: require('@/assets/icon.png'),
        dialog1: false,
        dialog2: false,
        store,
        selectedFile: {name: ""},
        fileSelected: false,
        comercialEmail: null,
        comercialName: null,
        password1: null,
        password2: null,
        user: new RegisterDto(),
        added: false,
        failed: false
      }
    },

methods: {
  handleFileImport() {
                this.$refs.uploader.click();
            },
            changeSelectedFile() {
                this.selectedFile = {name: ""}
            },
            onFileChanged(e) {
                this.selectedFile = e.target.files[0];
                this.fileSelected = true
                console.log(this.selectedFile)
            },
  getPedido() {
    if(store.pedidoAtual.modelo == "montra"){
      this.$router.push({name: 'detailsMontra'});
    } else {
      this.$router.push({name: 'detailsOneOrTwo'});
    }
  },
  async addComercial() {
    try {
        if(this.password1 == this.password2){
          this.user.email = this.comercialEmail
          this.user.password = this.password1
          this.user.name = this.comercialName
          await Backend.addComercial(this.user)
          this.added = true
        } else { 
          this.failed = true
        }
      } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
      }
  },
  async addClients() {
    try {
          await Backend.addClients(this.selectedFile)
          this.added = true
        } catch (error) {
        this.failed = true
        // TODO: Show something
        console.error(error)
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