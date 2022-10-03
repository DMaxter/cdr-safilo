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

              <v-btn-toggle v-model="icon" tile dark borderless>
              <v-btn color="#6e4e5d" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center1" @click="$router.push('clients')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Clientes</span>

                <v-icon right>
                  mdi-account-group
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-3">
            <v-col cols="auto">
                Comerciante
            </v-col>
            <v-col cols="auto" >
              <v-autocomplete
              style="width: 250px"
            v-model="user"
            :items="userNames"
            dense
            outlined
            label="nome"
          ></v-autocomplete>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Marca
            </v-col>
            <v-col cols="auto" >
              <v-autocomplete
              style="width: 250px"
            v-model="brand"
            :items="brandNames"
            dense
            outlined
            label="marca"
          ></v-autocomplete>
            </v-col>
          </v-row>
          <v-row justify="center" align="center" class="d-flex flex-column mt-4">
            <v-col cols="auto">
                Plafond
            </v-col>
            <v-col cols="auto" >
          <v-text-field
            style="width: 250px"
            v-model= valor
            label=""
            placeholder="Quantidade"
            outlined
            tile
            dense
            hide-details
          ></v-text-field>
            </v-col>
            <v-col cols="auto" >
              <v-btn-toggle v-model="toggle_exclusive" tile dark borderless mandatory>
                <v-btn v-model="add" class="customGradient">
                  Acrescentar
                </v-btn>

                <v-btn v-model="remove" class="customGradient">
                  Retirar
                </v-btn>

                <v-btn v-model="change" class="customGradient">
                  Alterar
                </v-btn>
            </v-btn-toggle>
            </v-col>
          </v-row>
          <v-dialog
        transition="dialog-bottom-transition"
        v-model="dialog"
        max-width="600"
        id="dialogo"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-row justify="space-between" class="d-flex mt-3 mr-0">
            <v-col cols="auto pl-7">
              <v-btn
              @click="$router.push('configure')"
              class="d-flex flex-column customGradient"
              tile
              small
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
            </v-col>
            <v-col cols="auto pr-7">
              <v-btn
              class="d-flex flex-column customGradient"
              v-bind="attrs"
              v-on="on"
              tile
              dark
              small
              @click= "success = false; failed = false; changePlafond(); dialog = true"
            > Confirmar <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
          </v-row>
        </template>
        <template v-slot:default="dialog">
          <v-card>
            <v-card-text>
              <div class="text-h6 pt-12" v-show="success"> Plafond alterado com sucesso! </div>
              <div class="text-h6 pt-12" v-show="failed"> Ocorreu um erro a alterar o plafond </div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="dialog.value = false"
              >Voltar</v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
          </v-card>
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'

export default {
  name: 'CustomerSearch',

  components: {
  },

  data: () => ({
    dialog: false,
    myImage2: require('@/assets/logologo1.png'),
    user: null,
    add: false,
    remove: false,
    change: false,
    valor: null,
    brand: null,
    allUsers: null,
    userNames: [],
    brandNames: [],
    allBrands: null,
    store,
    success: false,
    failed: false,
    text: "",
    material: "",
    estado: "",
    collapseOnScroll: true,
    items: ['Novo','Em Produção'],
    items2: ['BACKLIT','BANNER','MICROPERFURADO',
    'PAPEL FOTOGRÁFICO', 'PVC 3 mm', 'PVC 5 mm',
    'TÊXTIL BACKLIT MA', 'VINIL', 'Vinil imp + recort',
    'VINIL INTERIOR MON', 'VINIL METALIZADO', 'VINIL REPOSICIONÁ']
  }),
  async created() { 
       this.initialization()
    },
  methods: { 
    async initialization(){
      try {
        this.userNames = []
        this.brandNames = []
        this.allUsers = await Backend.getUsers()
        this.allBrands = await Backend.getBrands()
        this.allUsers.forEach(element => {
          this.userNames.push(element.name)
        });
        this.allBrands.forEach(element => {
          this.brandNames.push(element.name)
        });
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
    searchFilters() {
      if(this.text == ""){
        console.log("no text")
      } else if (this.text != "") {
        console.log(this.text)
        this.$router.push({name: 'details'})
      }
      if(this.material == ""){
        console.log("no material")
      } else if (this.material != "") {
        store.materialSearch = this.material
        this.$router.push({name: 'searchResults'})
      }
      if(this.estado == ""){
        console.log("no estado")
      } else if (this.estado != "") {
        store.estadoSearch = this.estado
        this.$router.push({name: 'searchResults'})
      }

      // this.$router.push({name: 'searchResults'});
    },
    async changePlafond() {
      var currentUser = null
      var existingUser = false;
      this.allUsers.forEach(element => {
        if(element.name == this.user){
          existingUser = true;
          currentUser = element;
        }
      });
      console.log(currentUser)
      var existingBrand = false;
      var idToUse = null
      var currentPlafond = null;
      this.allBrands.forEach(element => {
        if(element.name == this.brand){
          idToUse = element.id
          existingBrand = true;
          currentUser.credits.forEach(element2 => {
            if (element2.brand == this.brand){
              console.log(element2.amount)
              currentPlafond = element2.amount
            }
          })
        }
      });
      console.log(currentPlafond)
      if(existingUser && existingBrand) {
        if(this.change){
          try{
            console.log(currentUser.email, idToUse, this.valor)
            await Backend.updatePlafond(currentUser.email, idToUse, this.valor)
            this.initialization()
            this.success = true;
          }catch (error) {
            this.failed = true
          }
        } else if (this.add){
          currentPlafond = Number(this.valor) + Number(currentPlafond)
          console.log(currentPlafond)
          try{
            await Backend.updatePlafond(currentUser.email, idToUse, currentPlafond)
            this.initialization()
            this.success = true;
          }catch (error) {
            this.failed = true
          }
        } else if (this.remove){
          currentPlafond = Number(currentPlafond) - Number(this.valor)
          try{
            await Backend.updatePlafond(currentUser.email, idToUse, currentPlafond)
            this.initialization()
            this.success = true;
          }catch (error) {
            this.failed = true
          }
        }
      } else { 
        this.failed = true
      }
    }  
  }
};
</script>

<style>
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}
</style>