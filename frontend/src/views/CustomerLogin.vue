<template>
<v-app style="
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
  ">

  <v-container fill-height>
     <v-row justify="center" align="center">
      <v-img :src="myImage" contain height="150px" width="150px"></v-img>
    </v-row>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="400" width="450" style="border-radius: 15px">
            <v-card-title class="justify-center">Iniciar Sessão</v-card-title>
            <v-divider></v-divider>
            <v-card-text>

              <v-form>
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
              </v-form>
              </v-card-text>
                <v-card-actions>
                  <div justify="center" align="center">
                  <v-btn color="#6e4e5d" text @click="$router.push('recoverCode')">
                    Esqueceu-se da palavra passe?
                  </v-btn>
                  <v-dialog
        transition="dialog-bottom-transition"
        max-width="600"
        id="dialogo"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            width="33%" class="mt-16 white--text" color="#6e4e5d"
            v-bind="attrs"
            v-on="on"
            @click="$router.push('profile')"
          >Entrar</v-btn>
        </template>
        <template v-slot:default="dialog">
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12">O nome de utilizador ou palavra-passe está incorreto</div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog.value = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
                </div>
                </v-card-actions>

          </v-card>
        </v-col>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>

import Backend from '@/router/backend'
import AuthDto from '@/models/AuthDto';

export default {
  name: 'App',

  components: {
  },

  data: () => ({
    myImage: require('@/assets/logo.png'),
    auth: new AuthDto()
  }),



  methods: {
    login: async function () {
      try {
        await Backend.login(this.auth)
        this.$router.push("profile")
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    }
  }
};
</script>
