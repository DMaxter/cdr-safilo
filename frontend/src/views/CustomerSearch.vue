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

              <v-btn color="#6e4e5d" value="center2" class="v-btn--active">
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
          <v-row justify="center" align="center" class="d-flex flex-column mt-16">
            <v-col cols="auto">
                Procurar Cliente
            </v-col>
            <v-col cols="auto" >
            <v-text-field
            label=""
            placeholder="Nome/Id"
            filled
            rounded
            dense
          ></v-text-field>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-7">
            <v-col cols="auto">
                Procurar por Estado
            </v-col>
            <v-col cols="auto" >
          <v-select
          :items="items"
          label="Estado"
          dense
          outlined
        ></v-select>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-15">
            <v-col cols="auto">
              <v-btn
              class="d-flex flex-column"
              outlined
              x-large
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
  name: 'CustomerSearch',

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