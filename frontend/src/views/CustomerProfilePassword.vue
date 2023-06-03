<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" tile height="600" width="800" style="background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2">
            <v-col cols="2">
              <template v-if="this.menu1">
            <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                class="white--text customGradient mr-4"
                tile
                color="#808080"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="160" class="v-btn--active customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu2">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient mr-4"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" dark borderless tile>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="v-btn--active customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('notes')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Notas</span>

                <v-icon right>
                  mdi-note-multiple-outline
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu3">
          <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient mr-4"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="v-btn--active customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
          </v-col>
          <v-col cols="8">
          <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-1">
              <v-avatar color="#6e4e5d" size="100" class="mb-6 customGradient">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              {{this.profile}}
            </v-row>
            <v-divider></v-divider>
            </v-col>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
              <v-avatar color="#6e4e5d" size="40" class="mb-2 customGradient">
                <v-icon size="24" dark>
                  mdi-lock
                </v-icon>
              </v-avatar>
              Alterar Palavra Passe
              <v-col cols="auto" class="mt-4">
              Palavra passe atual
              <v-text-field
                              style="width: 300px"
                              class="mt-2"
                              label="Palavra Passe"
                              type="text"
                              tile
                              dense
                              outlined
                              hide-details
                              v-model="currentPassword"></v-text-field>
            </v-col>
            <v-col cols="auto">
              Palavra passe nova
              <v-text-field
                              style="width: 300px"
                              class="mt-2"
                              label="Nova Palavra Passe"
                              type="text"
                              tile
                              dense
                              outlined
                              hide-details
                              v-model="newPassword"></v-text-field>
            </v-col>
            <v-col cols="auto">
              Confirmar palavra passe nova
              <v-text-field
                              style="width: 300px"
                              class="mt-2"
                              label="Nova Palavra Passe"
                              type="text"
                              tile
                              dense
                              outlined
                              hide-details
                              v-model="newPassword2"></v-text-field>
            </v-col>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mt-4">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
              class="d-flex flex-column customGradient"
              small
              tile
              dark
              @click="changePassword"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-dialog
        v-model="dialog1"
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template>
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12">A Palavra Passe atual está incorreta ou as Palavras Passes novas não coincídem</div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog1 = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
      <v-dialog
        v-model="dialog2"
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template>
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12">A Palavra Passe foi alterada com sucesso!</div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog2 = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
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
import Backend from '@/router/backend'
import { store } from '@/store.js';

export default {
  name: 'CustomerProfile',

  components: {
  },

  data: () => ({
    myImage: require('@/assets/logologo1.png'),
    currentPassword: null,
    newPassword: null,
    newPassword2: null,
    profile: null,
    menu1: false,
    menu2: false,
    menu3: false,
    dialog1: false,
    dialog2: false,
  }),

  created: async function() {
    if(store.currentUser == null){
        store.currentUser = await Backend.getProfile()
      }
    this.profile = store.currentUser.name
    if(store.currentUser.roles[0] == 'COMMERCIAL' || store.currentUser.roles[0] == 'ADMIN'){ 
      this.menu1 = true
    } else if(store.currentUser.roles[0] == 'CDR'){ 
      this.menu2 = true
    } else if(store.currentUser.roles[0] == 'MANAGER'){
      this.menu3 = true
    }
  },

  methods: {
    changePassword: async function () {
      if(this.newPassword == this.newPassword2){
      try {
        await Backend.changePassword(this.currentPassword, this.newPassword)
        this.dialog2 = true;
        this.newPassword = null;
        this.currentPassword = null;
        this. newPassword2 = null;
      } catch (error) {
        this.dialog1 = true;
        this.newPassword = null;
        this.currentPassword = null;
        this. newPassword2 = null;
        console.error(error)
      }
    } else { 
      this.dialog1 = true;
      this.newPassword = null;
      this.currentPassword = null;
      this. newPassword2 = null;
    }
  }
  }
};
</script>

<style>
.customGradient {
  background-image: linear-gradient(#616161, grey);
}


#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
</style>