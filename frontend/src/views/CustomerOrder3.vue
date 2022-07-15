<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="600" width="800" style="border-radius: 15px">
          <v-row justify="end" align="center" class="mt-5 mr-5">
          <v-card elevation="2" outlined color="#FAFAFA" height="60" style="width: 180px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column mt-1">
            <v-col cols="auto" class="pa-0">
            Créditos atuais:
            </v-col>
            <v-col cols="auto" class="pa-0">
              3000 <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
          </v-row> 
          <v-row justify="center" align="center" no-gutters class="d-flex flex-column mt-5">
          <v-col cols="auto" class="mb-5">
          <v-img :src="myImage" contain height="170px" width="170px"></v-img>
          </v-col>
          <v-col cols="auto" >
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" style="width: 400px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Referência do Pedido:
            </v-col>
            <v-col>
              AE3456
            </v-col>
            </v-row>
            </v-card>
          <v-card elevation="2" outlined color="#FAFAFA" height="80" width="200" class="mt-5" style="width: 400px; border-radius: 15px">
            <v-row justify="center" align="center" class="d-flex flex-column">
            <v-col class="pt-4">
            Créditos a debitar
            </v-col>
             <v-col cols="auto">
              440 <v-icon>mdi-currency-eur</v-icon>
            </v-col>
            </v-row>
            </v-card>
          <v-textarea
          class="mt-5" 
          style="width: 400px; border-radius: 15px"
          rows="2"
          no-resize
          hide-details
          outlined
          name="input-7-4"
          label="Observações sobre o pedido"
          value=""
        ></v-textarea>
          </v-col>

          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4 mb-6" style="height: 45px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('order2')"
              class="d-flex flex-column"
              outlined
              rounded
              color="#6e4e5d"
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
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
  name: 'CustomerOrder3',

  components: {
  },

  data: () => ({
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    myImage: require('@/assets/default-placeholder.png')
  }),
};
</script>

<style>

#app {
  background: url('@/assets/bg_grande.jpg') center center fixed !important;
  background-size: cover;
}
</style>