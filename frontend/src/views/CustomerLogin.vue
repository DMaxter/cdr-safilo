<template>
  <v-app>

    <v-container fill-height>
      <v-row justify="center" align="center">
        <v-img :src="myImage" contain height="150px" width="150px"></v-img>
      </v-row>
      <v-row justify="center" align="center">
        <v-col cols="auto">
          <v-card elevation="12" outlined color="#FAFAFA" height="400" width="450" style="border-radius: 15px">
            <v-form @submit.prevent="login">
              <v-card-title class="justify-center">Iniciar Sessão</v-card-title>
              <v-divider></v-divider>
              <v-card-text>

                <v-text-field
                    label="Username"
                    type="text"
                    rounded
                    outlined
                    v-model="auth.email"></v-text-field>
                <v-text-field
                    label="Password"
                    type="password"
                    rounded
                    outlined
                    hide-details
                    v-model="auth.password"></v-text-field>
              </v-card-text>
              <v-card-actions>
                <div justify="center" align="center">
                  <v-btn color="#616161" text @click="$router.push('recoverCode')">
                    Esqueceu-se da palavra passe?
                  </v-btn>

                  <v-btn
                      width="33%" tile class="mt-16 white--text customGradient"
                      type="submit"
                  >Entrar
                  </v-btn>

                  <v-dialog
                      v-model="dialog"
                      transition="dialog-bottom-transition"
                      max-width="600"
                      id="dialogo"
                  >
                    <template>
                      <v-card>
                        <v-card-text>
                          <div class="text-h6 pt-12">O nome de utilizador ou palavra-passe está incorreto</div>
                        </v-card-text>
                        <v-card-actions class="justify-end">
                          <v-btn
                              text
                              @click="dialog = false"
                          >Voltar
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </template>
                  </v-dialog>
                </div>
              </v-card-actions>
            </v-form>

          </v-card>
        </v-col>
      </v-row>
    </v-container>

  </v-app>
</template>

<script>

import Backend from '@/router/backend'
import AuthDto from '@/models/AuthDto';
import {store} from '@/store.js'

export default {
  name: 'App',

  components: {},

  data: () => ({
    dialog: false,
    myImage: require('@/assets/logo.png'),
    auth: new AuthDto()
  }),


  methods: {
    login: async function () {
      try {
        await Backend.login(this.auth)
        store.currentUser = await Backend.getProfile()
        this.$router.push("profile")
      } catch (error) {
        this.dialog = true
        console.error(error)
      }
    }
  }
};
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
