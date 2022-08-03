<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" color="#FAFAFA" height="600" width="800" style="border-radius: 15px; background-color: rgba(235,235,238, 0.6);">
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

              <v-btn-toggle v-model="icon" dark dense>
              <v-btn color="#6e4e5d" value="left" height="60" width="170" @click="$router.push('profileComercial')">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" height="60" width="170" @click="$router.push('history')">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" height="60" width="170"  @click="$router.push('search')" >
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" class="v-btn--active" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-0">
            <v-col cols="auto" >
          <v-select style="width: 300px;"
          disabled
          :label=store.pedidoAtual.marca
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
            <v-text-field style="width: 120px;"
            :label= getHeight()
            disabled
            filled
            rounded
            dense
            hide-details
          ></v-text-field>
            </v-row>
           </v-col>
            <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pl-2">
            <v-text-field style="width: 120px;"
            :label= getWidth()
            disabled
            filled
            rounded
            dense
            hide-details
          ></v-text-field>
            </v-row>
           </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-2">
          <v-select style="width: 200px;"
          disabled
          :label = getMaterial()
          dense
          hide-details
          outlined
          ></v-select>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-8">
            <v-col cols="auto">
          <v-text-field
            disabled
            :label= store.pedidoAtual.quantity[this.n]
            filled
            rounded
            hide-details
            dense
          ></v-text-field>
              <v-checkbox
            v-model= store.pedidoAtual.application[this.n]
            disabled
            hide-details
            :label="`Aplicação?`"
            ></v-checkbox>
          </v-col>
          </v-row>
           <v-row justify="center" align="center" class="d-flex mt-8">
            <v-col cols="auto">
            <v-img :src=store.pedidoAtual.images[this.n] contain height="80px" width="80px" @click.stop="dialog = true"></v-img>
            </v-col>
            <v-col cols=3> 
              <v-row justify="center" align="start" class="d-flex flex-column">
                <v-col cols="auto">
                  Imagem Face {{this.n + 1}}
                </v-col>
                <v-col cols="auto" class="text-decoration-underline">
                  Escolher Imagem
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="2">
            </v-col>
           </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 55px;">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click = "$router.push('order')"
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
              @click = nextScreen()
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
import { store } from '@/store.js'

export default {
  name: 'CustomerOrder2',

  components: {
  },

  data: () => ({
    n: 0,
    store,
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    myImage: require('@/assets/default-placeholder.png'),
    dialog: false,
    ex4: false,
    picked: null,
    images: [
        {
          src: require('@/assets/uno.jpg'),
        },
        {
          src: require('@/assets/dos.jpg'),
        },
        {
          src: require('@/assets/trese.png'),
        },
        {
          src: require('@/assets/quatre.png'),
        },
      ],

  }),
    watch: {
        n: function () {
          this.getHeight()
          this.getWidth()
          this.getMaterial()
        } 
    },
    methods: {
      getHeight() {
        return store.pedidoAtual.dimensoes[this.n].split(" ")[0]
      },
      getWidth() {
        return store.pedidoAtual.dimensoes[this.n].split(" ")[2]
      },         
      getMaterial() {
        return store.pedidoAtual.material[this.n]
      },   
      nextScreen () {
        if(store.pedidoAtual.material.length > 1 && this.n < 1 ){
          this.n++
        } else {
          this.$router.push({name: 'detailsOneOrTwoFinal'});
        }
      },

      getFace () {
        this.myImage = this.images[this.picked]
        store.face1 = this.myImage
      }
    }
};

</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}

.selected {
    background-image: repeating-linear-gradient(-45deg,
                        rgba(255,0,0,.25),
                        rgba(255,0,0,.25) 5px,
                        rgba(0,0,255,.25) 5px,
                        rgba(0,0,255,.25) 10px
                      );
  }
</style>