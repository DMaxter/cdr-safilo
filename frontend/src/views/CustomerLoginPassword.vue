<template>
<v-app>

  <v-container fill-height>
     <v-row justify="center" align="center">
      <v-img :src="myImage" contain height="150px" width="150px"></v-img>
    </v-row>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="400" width="450" style="border-radius: 15px">
            <v-card-title class="justify-center">Redefinir Palavra Passe</v-card-title>
            <v-divider></v-divider>
            <v-card-text>

              <v-form>
                <v-text-field
                              class="mt-16"
                              label="Email"
                              type="text"
                              rounded
                              outlined
                              hide-details
                              v-model="username"></v-text-field>
              </v-form>
              </v-card-text>
                <v-card-actions class="justify-center">
                  <v-btn
                      width="33%" tile class="white--text customGradient"
                      type="submit" @click="token(); dialog = true"
                  >Enviar Link
                  </v-btn>
                </v-card-actions>

          </v-card>
        </v-col>
    </v-row>
    <v-dialog
        v-model="dialog"
        transition="dialog-bottom-transition"
        max-width="600"
    >
      <template>
        <v-card>
          <v-card-text>
            <div class="text-h6 pt-12" v-show="succeeded">Foi enviado um email para {{ username }} com um link para alterar a sua password.</div>
            <div class="text-h6 pt-12" v-show="failed">Ocorreu um erro a alterar a password.</div>

          </v-card-text>
          <v-card-actions class="justify-end">
            <v-btn
                text
                @click="dialog = false; failed = false; succeeded = false"
            >Voltar
            </v-btn>
          </v-card-actions>
        </v-card>
      </template>
    </v-dialog>
    </v-container>

  </v-app>
</template>

<script>
import Backend from '../router/backend';

export default {
  name: 'App',

  components: {
  },

  data: () => ({
     myImage: require('@/assets/logo.png'),
     username: "",
     dialog: false,
     succeeded: false,
     failed: false
  }),

  methods: {
    token: async function () {
      try {
        await Backend.redefineWithToken(this.username)
        this.succeeded = true
      } catch(error) {
        this.failed = true
        console.log(error)
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
