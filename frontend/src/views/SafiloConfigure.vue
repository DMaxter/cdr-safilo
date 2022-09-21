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
          <v-btn height="60" width="500" class="mb-3 customGradient" dark @click="$router.push('imageUpload')"> Imagens </v-btn>
          <v-btn height="60" width="500" class="mb-3 customGradient" dark @click="$router.push('plafondChange')"> Plafond </v-btn>
          <v-btn height="60" width="500" class="mb-3 customGradient" dark @click="$router.push('brandChange')"> Marcas </v-btn>
          <v-dialog
      v-model="dialog1"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn height="60" width="500" class="mb-3 customGradient" dark v-bind="attrs" v-on="on"> Registar Comercial </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Registar Comercial </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Email"
                  required
                  v-model="comercialEmail"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Nome"
                  required
                  v-model="comercialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Palavra Passe"
                  required
                  v-model="password1"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
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
            @click="dialog1 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="addComercial(); dialog1 = false;"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 190px;">
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
import RegisterDto from '@/models/RegisterDto';

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        dialog1: false,
        store,
        comercialEmail: null,
        comercialName: null,
        password1: null,
        password2: null,
        user: new RegisterDto()
      }
    },

methods: {
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
        }
      } catch (error) {
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