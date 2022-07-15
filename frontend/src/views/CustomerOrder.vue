<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="600" width="800" style="border-radius: 15px">
                    <v-row no-gutters justify="center">

            <v-btn-toggle dark dense rounded style="border-radius: 15px">
              <v-btn color="#6e4e5d" value="left" @click="$router.push('profile')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon dark right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('history')">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2"  @click="$router.push('search')">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" class="v-btn--active">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-row>
          <v-row justify="space-around" align="center" class="d-flex flex-column mt-2">
           <v-col cols="auto">
            Selecione o tipo de pedido
           </v-col>
          </v-row>
           <v-row justify="space-around" align="center" class="d-flex flex-column mt-10">
           <v-col cols="auto">
            Pedido normal (1 ou 2 faces)
           </v-col>
           <v-col cols="auto">
            <v-checkbox hide-details style="transform: scale(3);" class="ml-6 mt-4" :ripple="false"
              value
            ></v-checkbox>
           </v-col>
          </v-row>
          <v-row justify="space-around" align="center" no-gutters style="height: 100px;" class="mt-5">
        <v-col cols = "auto"
          v-for="n in 2"
          :key="n"
        >
        <span align="center"> <v-icon> mdi-circle </v-icon> </span>
        </v-col>
        <v-col cols = "auto">
        Ou
        </v-col>
        <v-col cols = "auto"
          v-for="n in 2"
          :key="n"
        >
        <span align="center"> <v-icon> mdi-circle </v-icon> </span>
        </v-col>
        </v-row>
          <v-row no-gutters align="center" class="d-flex flex-column mt-3">
            Pedido de Montra
            </v-row>
           <v-row no-gutters justify="space-around" align="center">
           <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column">
            <v-checkbox style="transform: scale(3);" class="ml-6 mt-14" :ripple="false"
              value
            ></v-checkbox>
            imagem à direita
            </v-row>
           </v-col>
            <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column">
            <v-checkbox style="transform: scale(3);" class="ml-6 mt-14" :ripple="false"
              value
            ></v-checkbox>
            imagem à esquerda
            </v-row>
           </v-col>
           </v-row>
           <v-row no-gutters align="end" justify="end" class="d-flex flex-column pr-4" style="height: 80px;">
           <v-col cols="auto">
            <v-btn
              @click="$router.push('order2')"
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
  name: 'CustomerOrder',

  components: {
  },

  data: () => ({
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
  }),
};
</script>

<style>

#app {
  background: url('@/assets/bg_grande.jpg') center center fixed !important;
  background-size: cover;
}

</style>