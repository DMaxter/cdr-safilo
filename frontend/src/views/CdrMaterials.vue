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

              <v-btn-toggle v-model="icon" dark borderless tile>
              <v-btn color="#6e4e5d" value="left" height="64" width="160" @click="$router.push('profile')" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 330px">
          <template>
    <v-dialog
      v-model="dialog"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="500" tile class="mb-3 customGradient" dark v-bind="attrs" v-on="on" @click="added1 = false; failed1 = false"> Adicionar Materiais à base de dados </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added1 && !failed1"> Adicionar Material à base de dados</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col v-show="added1">
              <span class="text-h5"> O Material foi adicionado com sucesso! </span> 
              </v-col>
              <v-col v-show="failed1">
              <span class="text-h5"> Ocorreu um erro a adicionar o material </span> 
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Nome do Material"
                  required
                  v-model="materialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Custo do Material"
                  required
                  v-model="materialCost"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false; materialName = null; materialCost = null;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added1 && !failed1"
            @click="addMaterial();"
          >
            Guardar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
                    <template>
    <v-dialog
      v-model="dialog1"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="500" tile class="mb-3 customGradient" dark v-bind="attrs" v-on="on" @click="getMaterials(); added2 = false, failed2 = false"> Alterar Materiais na base de dados </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added2 && !failed2 && !toDelete && !toDelete2"> Materiais </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="auto" v-show="added2">
              <span class="text-h5"> O Material foi alterado com sucesso! </span> 
              </v-col>
              <v-col cols="auto" v-show="failed2">
              <span class="text-h5"> Ocorreu um erro a alterar o material </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete">
              <span class="text-h6"> Tem a certeza que pretende eliminar o material da base de dados? (pressione o botão novamente para apagar) </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete2">
              <span class="text-h5"> O Material foi eliminado com sucesso! </span> 
              </v-col>
              <v-col
                cols="8"
              >
                <v-select
                  :items=available
                  required
                  v-model = picked
                  @change="selected=true"
                  v-show="!added2 && !failed2 && !toDelete && !toDelete2"
                ></v-select>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  label="Novo nome do Material"
                  required
                  v-model="newMaterialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  label="Novo custo do Material"
                  required
                  v-model="newMaterialCost"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  @click="toDelete = true"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog1 = false; selected = false; picked = null; toDelete = false, toDelete2 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  v-show="toDelete"
                  @click="deleteMaterial(); toDelete = false; toDelete2 = true;"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
            @click="updateMaterial(); selected = false; picked = null; newMaterialName = null; newMaterialCost = null"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
          </v-row>
          </v-card>         
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
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
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        dialog: false,
        dialog1: false,
        store,
        materialName: null,
        materialCost: null,
        newMaterialName: null,
        newMaterialCost: null,
        allMaterials: null,
        available: [],
        selected: false,
        added1: false,
        picked: null,
        failed1: false,
        added2: false,
        failed2: false,
        toDelete: false,
        toDelete2: false,
      }
    },

methods: {
  
  addMaterial: async function () {
      try {
        console.log(this.materialName, this.materialCost)
        await Backend.addMaterial(this.materialName, this.materialCost)
        this.added1 = true
      } catch (error) {

        this.failed1 = true
        console.error(error)
      }
    },
  
  getMaterials: async function () {
      try {
        this.allMaterials = await Backend.getMaterials()
        this.available = []
        this.allMaterials.forEach(element => {
          this.available.push(element.name)
        });
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    deleteMaterial: async function () {
      try {
        var id = null
        this.allMaterials.forEach(element => {
          if(element.name == this.picked){
            id = element.id
          }
        });
        await Backend.deleteMaterial(id)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    updateMaterial: async function () {
      try {
        var id = null
        this.allMaterials.forEach(element => {
          if(element.name == this.picked){
            id = element.id
          }
        });
        await Backend.updateMaterial(id, this.newMaterialName, this.newMaterialCost)
        this.added2 = true
      } catch (error) {
        this.failed2 = true 
        // TODO: Show something
        console.error(error)
      }
    }

}
  }
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