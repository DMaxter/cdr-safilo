<template>
  <v-app
    style="
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
  "
  >
    <v-container fill-height>
      <v-row justify="center" align="center">
        <v-img :src="myImage" contain height="150px" width="150px"></v-img>
      </v-row>
      <v-row justify="center" align="center">
        <v-col cols="auto">
          <v-card elevation="12" outlined color="#FAFAFA" height="400" style="border-radius: 15px">
            <v-card-title class="justify-center">Iniciar Sessão</v-card-title>
            <v-divider></v-divider>
            <v-card-text>
              <v-form>
                <v-text-field label="Username" type="text" rounded outlined v-model="username"></v-text-field>
                <v-text-field
                  label="Password"
                  type="password"
                  rounded
                  outlined
                  hide-details
                  v-model="password"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <div justify="center" align="center">
                <v-btn color="#6e4e5d" text>Esqueceu-se da palavra passe?</v-btn>
                <v-btn width="33%" class="mt-16 white--text" color="#6e4e5d" @click="login">Entrar</v-btn>
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

export default {
  name: 'App',

  components: {
  },

  data: () => ({
    myImage: require('@/assets/logo.png'),
    username: "",
    password: ""
  }),



  methods: {
    login: async function () {
      try {
        await Backend.login({ username: this.username, password: this.password })
        this.$router.push("profile")
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    }
  }
};
</script>
