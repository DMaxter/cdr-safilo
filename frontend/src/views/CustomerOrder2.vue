<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" outlined color="#FAFAFA" height="600" width="800" style="border-radius: 15px">
          <v-row justify="center" align="center" class="d-flex flex-column mt-16">
            <v-col cols="auto" >
          <v-select style="width: 200px;"
          :items="items"
          label="Marca"
          dense
          outlined
          hide-details
          ></v-select>
          </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-5 mb-3">
                Medidas (cm)
           </v-row>
           <v-row no-gutters justify="center" align="center" class="mt-4">
           <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pr-2">
            <v-text-field style="width: 100px;"
            label=""
            placeholder="Largura"
            filled
            rounded
            dense
          ></v-text-field>
            </v-row>
           </v-col>
            <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pl-2">
            <v-text-field style="width: 100px;"
            label=""
            placeholder="Altura"
            filled
            rounded
            dense
          ></v-text-field>
            </v-row>
           </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-2">
          <v-select style="width: 200px;"
          :items="items"
          label="Material"
          dense
          outlined
          hide-details
          ></v-select>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-8">
            <v-col cols="auto">
          <v-text-field
            label=""
            placeholder="Quantidade"
            filled
            rounded
            hide-details
            dense
          ></v-text-field>
          <v-checkbox
            v-model="checkbox"
            hide-details
            :label="`Aplicação?`"
          ></v-checkbox>
          </v-col>
          </v-row>
           <v-row justify="center" align="center" class="d-flex mt-8">
            <v-col cols="auto">
            <v-img :src="myImage" contain height="80px" width="80px"></v-img>
            </v-col>
            <v-col cols=6> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Face 1
                </v-col>
                <v-col cols="auto" class="text-decoration-underline">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
           </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 60px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.push('order')"
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
  name: 'CustomerSearch',

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