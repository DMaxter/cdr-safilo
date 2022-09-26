<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" tile height="600" width="800" style="border-radius: 15px; background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2">
            <v-col cols="2">
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

              <v-btn-toggle v-model="icon" tile dark mandatory borderless>
              <v-btn color="#808080" value="left" height="64" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-col>
          <v-col cols="8">
          <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-1">
              <v-avatar color="#6e4e5d" size="100" class="mb-6 customGradient">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              {{this.profile.name}}
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
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mt-2">
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
  }),

  created: async function() {
    this.profile = await Backend.getProfile()
  },

  methods: {
    changePassword: async function () {
      if(this.newPassword == this.newPassword2)
      try {
        await Backend.changePassword(this.currentPassword, this.newPassword)
      } catch (error) {
        // TODO: Show something
        console.error(error)
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