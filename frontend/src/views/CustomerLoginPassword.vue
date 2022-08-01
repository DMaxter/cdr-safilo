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
            <v-card-title class="justify-center">Redefinir Palavra Passe</v-card-title>
            <v-divider></v-divider>
            <v-card-text>

              <v-form>
                <v-text-field
                              class="mt-16"
                              label="Username"
                              type="text"
                              rounded
                              outlined
                              hide-details
                              v-model="username"></v-text-field>
              </v-form>
              </v-card-text>
                <v-card-actions class="justify-center">
                  <v-btn color="#6e4e5d" class="white--text" @click="token">
                    Enviar Código
                  </v-btn>
                </v-card-actions>

          </v-card>
        </v-col>
    </v-row>
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
     username: ""
  }),

  methods: {
    token: async function () {
      try {
        await Backend.redefineWithToken(this.username)
        this.$router.push('redefinePassword')
      } catch(error) {
        // TODO: Show something
        console.log(error)
      }
    }
  }
};
</script>
