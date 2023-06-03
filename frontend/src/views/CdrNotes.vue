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
                class="white--text customGradient"
                tile
                color="#808080"
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

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('notes')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Notas</span>

                <v-icon right>
                  mdi-note-multiple-outline
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Materiais</span>

                <v-icon right>
                  mdi-book
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
          </v-row>
          <v-row justify="center" align="center" class="mt-9">
            <v-dialog
              v-model="dialog"
              transition="dialog-bottom-transition"
              max-width="800"
              id="dialogo"
            >
              <template>
                <v-card>
                  <v-card-title class="justify-center"> <span v-show="!added && !failed"> {{ this.currClient.name }}; {{ this.currClient.address }}; {{ this.currClient.postalCode }} </span></v-card-title>
                  <v-card-text>
                    <div class="text-h6 mb-5" v-show="!added && !failed">Notas:</div>
                    <v-textarea tile outlined hide-details auto-grow v-model="noteToAdd" v-show="!added && !failed"></v-textarea>
                    <div class="text-h4 mt-3" v-show="added">Nota adicionada com sucesso.</div>
                    <div class="text-h4 mt-3" v-show="failed">Ocorreu um erro a adicionar a nota.</div>
                  </v-card-text>
                  <v-card-actions class="justify-center">
                    <v-btn
                      text
                      class="mr-2"
                      @click="dialog = false"
                    >Voltar</v-btn>

                    <v-btn
                      text
                      class="ml-2"
                      v-show="!added"
                      @click="saveNote();"
                    >Guardar Nota</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
            <v-data-table :headers="headers" :items="allClients" fixed-header hide-default-footer
                tile :search="dessertFilterValue" disable-pagination item-key="id" height="350" style="width: 600px;"
                class="elevation-1 my-header-style">
                <template v-slot:top>

                  <!-- v-container, v-col and v-row are just for decoration purposes. -->
                  <v-container fluid>
                    <v-row>
                      <v-col cols="6">
                        <v-row class="pa-2">
                          <v-text-field style="width: 150px; border-radius: 0px;" v-model="dessertFilterValue"
                            label="Procurar" hide-details outlined dense></v-text-field>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>

                </template>
                <template v-slot:[`item.actions`]="{ item }">
                  <v-icon @click="addNote(item)">mdi-plus</v-icon>
                </template>
              </v-data-table>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex pr-4" style="height: 90px;">
           <v-col cols="auto" class="pl-3">
            <v-btn
              @click="$router.push('profile')"
              class="d-flex flex-column customGradient mb-2"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
            <v-col cols="auto">
            </v-col>
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
import Backend from "@/router/backend";
import { store } from '@/store.js'
export default {
  name: 'CdrNotes',

  components: {
  },

data () {
      return {
        myImage: require('@/assets/logologo1.png'),
        store,
        allClients: [],
        dessertFilterValue: '',
        dialog: false,
        currClient: "",
        noteToAdd: "",
        added: false,
        failed:false
      }
    },
  async created () {
    this.allClients = await Backend.getClients();
  },
  methods: {
    addNote(item){
      this.currClient = item
      this.noteToAdd = this.allClients.find(x => x.id == this.currClient.id).note
      //this.noteToAdd = client.note
      this.added = false
      this.failed = false
      this.dialog = true
    },
    async saveNote(){
      try{
        await Backend.addNote(this.currClient.id, this.noteToAdd)
        this.added = true
        this.noteToAdd = ""
        this.allClients = await Backend.getClients();
      } catch(error) {
        this.failed = true
      }
    }
  },
  computed: {
    headers() {
      return [
        {
          text: 'Código',
          align: 'left',
          sortable: false,
          value: 'id',
          class: 'my-header-style',
        },
        {
          text: 'Nome',
          value: 'name',
          align: "center",
          class: 'my-header-style',
        },
        {
          text: 'Morada',
          value: 'address',
          align: "center",
          class: 'my-header-style',
        },
        {
          text: 'Cód. Postal',
          value: 'postalCode',
          align: "center",
          class: 'my-header-style',
        },
        { text: "", value: "actions", align: "right", sortable: false, class: 'my-header-style' },
      ]
    },

  },
  }
</script>

<style>
.customGradient {
  background-image: linear-gradient(#616161, grey);
}

#app {
  background: url('@/assets/background.jpg') center center fixed !important;
  background-size: cover;
}

.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}
</style>