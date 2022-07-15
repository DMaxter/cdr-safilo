<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="600" width="800" style="border-radius: 15px">
          <v-row no-gutters justify="center">

            <v-btn-toggle v-model="icon" dark dense mandatory rounded style="border-radius: 15px">
              <v-btn color="#6e4e5d" value="left">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('history')">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('order')">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-4 mt-5">
              <v-avatar color="#6e4e5d" size="100" class="mb-6">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              Nome Comerciante
            </v-row>
            <v-divider></v-divider>
            <v-row justify="center" align="center" class="d-flex flex-column mt-2">
              <v-avatar color="#6e4e5d" size="40" class="mb-2">
                <v-icon size="24" dark>
                  mdi-lock
                </v-icon>
              </v-avatar>
              Alterar Palavra Passe
            <v-col cols="auto" class="mt-6">
              Palavra passe nova
              <v-text-field
                              style="width: 300px"
                              class="mt-2"
                              label="Nova Palavra Passe"
                              type="text"
                              rounded
                              outlined
                              hide-details
                              v-model="username"></v-text-field>
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
                              v-model="username"></v-text-field>
            </v-col>
            </v-row>
            <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 60px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('profile')"
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

export default {
  name: 'CustomerProfile',

  components: {
  },

  data: () => ({

  }),
};
</script>

<style>

#app {
  background: url('@/assets/bg_grande.jpg') center center fixed !important;
  background-size: cover;
}
</style>