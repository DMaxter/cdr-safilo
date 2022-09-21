<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
            <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                tile
                class="white--text customGradient"
                color="#6e4e5d"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" dark dense tile>
              <v-btn color="#6e4e5d" value="left" height="64" width="170" class="customGradient v-btn--active">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mb-10 mt-3">
              <v-avatar color="#6e4e5d" size="100" class="mb-6 customGradient">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              Nome Empregado CdR
            </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="50" dark class="d-flex"> mdi-currency-usd </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 2 class="d-flex flex-column ml-10" align="left"> Plafond
                <v-main class="text-no-wrap" style="background-color: #cfcfcf;" align="center"> 500
                <v-icon size="15"> mdi-currency-eur </v-icon> </v-main> </v-col>    
             </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4 pt-7' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="30" dark class="d-flex"> mdi-email </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 4 class="d-flex flex-column ml-10" align="left"> Email 
                <v-main class=" text-no-wrap" style="background-color: #cfcfcf;" align="center"> xyz@gmail.com </v-main> 
            </v-col>  
             </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4 pt-7' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12 customGradient">
                  <v-icon size="35" dark class="d-flex"> mdi-lock </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 3 class="d-flex flex-column ml-10" align="left"> Password
                <v-main class="text-no-wrap" style="background-color: #cfcfcf;" align="center"> ****** </v-main> 
            </v-col>
            <v-btn class="d-flex mt-9 white--text customGradient" color="#6e4e5d" height="25" elevation="2" tile  @click="$router.push('passwordChange')"> Alterar 
              <v-icon class="pl-1" size="15" dark> mdi-cog-outline </v-icon>
            </v-btn>    
             </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
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
    myImage: require('@/assets/logologo1.png'),
    items: [
        { title: 'Click Me' },
        { title: 'Click Me' },
        { title: 'Click Me' },
        { title: 'Click Me 2' },
      ],
  }),
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