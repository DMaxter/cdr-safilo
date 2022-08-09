<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="635" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="start" class="pt-2">
            <v-col cols="2">
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

              <v-btn-toggle v-model="icon" dark dense>
              <v-btn color="#6e4e5d" value="left" height="60" width="170" class="v-btn--active" @click="$router.push('profileComercial')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" @click="$router.push('history')">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" height="60" width="170"  @click="$router.push('search')" >
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" height="60" width="170" @click="$router.push('order')">
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
              <v-avatar color="#6e4e5d" size="100" class="mb-6">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              Nome empregado safilo
            </v-row>
            <v-divider></v-divider>
            </v-col>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
              <v-avatar color="#6e4e5d" size="40" class="mb-2">
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
                              rounded
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
                              rounded
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
                              rounded
                              outlined
                              hide-details
                              v-model="newPassword2"></v-text-field>
            </v-col>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 15px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profileComercial')"
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
              @click="changePassword"
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
import Backend from '@/router/backend'

export default {
  name: 'CustomerProfile',

  components: {
  },

  data: () => ({
    currentPassword: null,
    newPassword: null,
    newPassword2: null
  }),

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

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>