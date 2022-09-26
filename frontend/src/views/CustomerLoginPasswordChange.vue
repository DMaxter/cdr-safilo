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
                              class="mt-13"
                              label="Nova Palavra Passe"
                              type="text"
                              rounded
                              outlined
                              hide-details
                              v-model="username"></v-text-field>
                <v-text-field
                              class="mt-8"
                              label="Confirmar Palavra Passe"
                              type="text"
                              rounded
                              outlined
                              hide-details
                              v-model="username"></v-text-field>
              </v-form>
              </v-card-text>
                <v-card-actions class="justify-center">
                  <v-btn color="#6e4e5d" class="white--text mt-12">
                    Confirmar
                  </v-btn>
                </v-card-actions>

          </v-card>
        </v-col>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>

export default {
  name: 'App',

  components: {
  },

  data: () => ({
     myImage: require('@/assets/logo.png')
  }),
};
</script>
<style>
  #app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
</style>
