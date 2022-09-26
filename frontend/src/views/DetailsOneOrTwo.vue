<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="600" width="800" tile style="background-color: #E0E0E0">          
            <v-row no-gutters justify="start" class="pt-2 pl-2">
              <template v-if="this.menu1">
            <v-menu
            :offset-x="true"
            tile
            >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                height="64"
                width="100"
                class="white--text customGradient"
                tile
                color="#808080"
                v-bind="attrs"
                v-on="on"
              >
                Menu
              </v-btn>
            </template>

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Novo Pedido</span>

                <v-icon right>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
        <template v-else-if="this.menu2">
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

              <v-btn-toggle v-model="icon" dark borderless tile>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
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
        </template>
        <template v-else-if="this.menu3">
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

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="170" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="170" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="170" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-0">
            <v-col cols="auto" >
          <v-text-field style="width: 200px; pointer-events: none"
          label="Marca"
          :value="store.pedidoAtual.marca[n]"
          dense
          outlined
          hide-details
          ></v-text-field>
          </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-5 mb-3">
                Medidas (cm)
           </v-row>
           <v-row no-gutters justify="center" align="center" class="mt-4">
           <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pr-2">
            <v-text-field style="width: 120px; pointer-events: none"
            label=  "Altura"
            :value="getHeight()"
            dense
            outlined
            hide-details
          ></v-text-field>
            </v-row>
           </v-col>
            <v-col cols="auto">
            <v-row no-gutters justify="space-around" align="center" class="d-flex flex-column pl-2">
            <v-text-field style="width: 120px; pointer-events: none"
            label= "Largura"
            :value="getWidth()"
            outlined
            dense
            hide-details
          ></v-text-field>
            </v-row>
           </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-8">
          <v-text-field style="width: 200px; pointer-events: none;"
          label = "Material"
          :value="getMaterial()"
          dense
          hide-details
          outlined
          ></v-text-field>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-8">
            <v-col cols="auto">
          <v-text-field
            disabled
            :label= store.pedidoAtual.quantity
            filled
            rounded
            hide-details
            dense
          ></v-text-field>
              <v-checkbox
            v-model= store.pedidoAtual.application
            disabled
            hide-details
            :label="`Aplicação?`"
            ></v-checkbox>
          </v-col>
          </v-row>
           <v-row justify="center" align="center" class="d-flex mt-8">
            <v-col cols="auto">
            <v-img :src=store.pedidoAtual.images[n] contain height="80px" width="80px" @click.stop="dialog = true"></v-img>
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
              @click = "$router.go(-1)"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            <v-btn
              @click = "nextScreen()"
              class="d-flex flex-column customGradient"
              small
              dark
              tile
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
        <span style="font-size: 10px;">© 2022 casa dos reclamos, todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'

export default {
  name: 'CustomerOrder2',

  components: {
  },

  data: () => ({
    n: 0,
    store,
    myImage2: require('@/assets/logologo1.png'),
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
    menu1: false,
    menu2: false,
    menu3: false,
  }),
  async created() {
    var profile = await Backend.getProfile()
    if(profile.roles[0] == 'COMMERCIAL' || profile.roles[0] == 'ADMIN'){ 
      this.menu1 = true
    } else if(profile.roles[0] == 'CDR'){ 
      this.menu2 = true
    } else if(profile.roles[0] == 'MANAGER'){
      this.menu3 = true
    }
  },
    methods: {
      getHeight() {
        if(store.pedidoAtual.modelo == "OneFace"){
          return store.pedidoAtual.dimensoes[0].height;
        } else if(store.pedidoAtual.modelo == "TwoFaces") {
          return store.pedidoAtual.dimensoes[this.n].height;
        }
      },
      getWidth() {
        if(store.pedidoAtual.modelo == "OneFace"){
          return store.pedidoAtual.dimensoes[0].width;
        } else if(store.pedidoAtual.modelo == "TwoFaces") {
          return store.pedidoAtual.dimensoes[this.n].width;
        }
      },         
      getMaterial() {
        if(store.pedidoAtual.modelo == "OneFace"){
          return store.pedidoAtual.material[0];
        } else if(store.pedidoAtual.modelo == "TwoFaces") {
          return store.pedidoAtual.material[this.n];
        }
      },   
      nextScreen () {
        if(store.pedidoAtual.modelo == "OneFace"){
          this.$router.push({name: 'detailsOneOrTwoFinal'});
        } else if(store.pedidoAtual.modelo == "TwoFaces"){
            if(this.n == 1){
              this.$router.push({name: 'detailsOneOrTwoFinal'});
            }
          this.n = this.n + 1
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
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

  .customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>