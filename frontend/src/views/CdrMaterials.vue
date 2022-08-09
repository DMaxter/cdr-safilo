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

              <v-btn-toggle v-model="icon" dark dense mandatory>
              <v-btn color="#6e4e5d" value="left" height="60" width="170">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="60" width="170">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="60" width="170">
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
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="500" class="mb-3" v-bind="attrs" v-on="on"> Adicionar Materiais à base de dados </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Adicionar Material à base de dados</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-text-field
                  label="Nome do Material"
                  required
                  v-model="materialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
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
            @click="dialog = false; materialName = null; materialCost = null"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="addMaterial(); dialog = false;"
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
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="500" class="mb-3" v-bind="attrs" v-on="on" @click="getMaterials"> Alterar Materiais na base de dados </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Materiais </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col
                cols="8"
              >
                <v-select
                  :items=available
                  required
                  v-model = picked
                  @change="selected=true"
                ></v-select>
              </v-col>
              <v-col
                cols="8"
                v-show="selected"
              >
                <v-text-field
                  label="Novo nome do Material"
                  required
                  v-model="newMaterialName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="selected"
              >
                <v-text-field
                  label="Novo custo do Material"
                  required
                  v-model="newMaterialCost"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="selected"
              >
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  small
                  color="primary"
                  @click="deleteMaterial"
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
            @click="dialog1 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="updateMaterial(); dialog1 = false;"
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
        picked: null,
      }
    },

methods: {
  
  addMaterial: async function () {
      try {
        console.log(this.materialName, this.materialCost)
        await Backend.addMaterial(this.materialName, this.materialCost)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },
  
  getMaterials: async function () {
      try {
        this.allMaterials = await Backend.getMaterials()
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
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    }

}
  }
</script>

<style>

#app {
    background: #3A1C71;
    background: -webkit-linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
    background: linear-gradient(180deg, #a54676, #8c4b6c, #6e4e5d);
}
</style>