<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" :height="$vuetify.breakpoint.lg
                ? '600px' 
                : '900px'" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
          <v-row no-gutters justify="start" class="pt-2 pl-2">
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

              <v-btn-toggle v-model="icon" dark dense mandatory>
              <v-btn color="#6e4e5d" value="left" height="60" width="170">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
            <v-row justify="center" align="center" class="d-flex flex-column mb-10 mt-3">
              <v-avatar color="#6e4e5d" size="100" class="mb-6">
                <v-icon size="60" dark>
                  mdi-account-circle
                </v-icon>
              </v-avatar>
              Nome Empregado Safilo
            </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12">
                  <v-icon size="50" dark class="d-flex"> mdi-currency-usd </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 2 class="d-flex flex-column ml-10" align="left"> Plafond
                <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> 500
                <v-icon size="15"> mdi-currency-eur </v-icon> </v-main> </v-col>    
             </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4 pt-7' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12">
                  <v-icon size="30" dark class="d-flex"> mdi-email </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 4 class="d-flex flex-column ml-10" align="left"> Email 
                <v-main class="grey lighten-2 text-no-wrap rounded-pill" align="center"> xyz@gmail.com </v-main> 
            </v-col>  
             </v-row>
             <v-row justify="left" align="left" :class="$vuetify.breakpoint.lg
                ? 'd-flex ml-4 pt-7' 
                : 'd-flex ml-4 mt-16'">
              <v-col cols = 1>
                <v-avatar color="#6e4e5d" size="50" class="d-flex ml-12">
                  <v-icon size="35" dark class="d-flex"> mdi-lock </v-icon>
                </v-avatar>
              </v-col>
              <v-col cols = 3 class="d-flex flex-column ml-10" align="left"> Password
                <v-main class="grey lighten-2 text-no-wrap rounded-pill" width="" align="center"> ****** </v-main> 
            </v-col>
            <v-btn class="d-flex mt-9 white--text" color="#6e4e5d" height="25" elevation="2" rounded  @click="$router.push('passwordChange')"> Alterar 
              <v-icon class="pl-1" size="15" dark> mdi-cog-outline </v-icon>
            </v-btn>    
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
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>