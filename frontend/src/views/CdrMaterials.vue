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

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('notes')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Notas</span>

                <v-icon right>
                  mdi-note-multiple-outline
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
          <v-row justify="center" align="center" class="fill-height d-flex flex-column mt-10" style="height: 400px">
          <v-row class="mt-15">
          <template>
    <v-dialog
      v-model="dialog"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 mr-1 customGradient" dark v-bind="attrs" v-on="on" @click="added1 = false; failed1 = false; getFinishes(); getFinishGroups();"> Adicionar Materiais </v-btn>
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
              <v-select
                  :items=availableFinishGroups
                  label="Acabamentos obrigatórios"
                  required
                  multiple
                  v-model = mandatoryFinishGroups
                  v-show="!added1 && !failed1 && !toDelete"
                ></v-select>
                </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
              <v-select
                  :items=availableFinishes
                  label="Acabamentos adicionais"
                  required
                  multiple
                  v-model = mandatoryFinishes
                  v-show="!added1 && !failed1 && !toDelete"
                ></v-select>
                </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false; materialName = null; mandatoryFinishGroups = [], mandatoryFinishes = []"
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
      <v-btn height="60" width="310" tile class="mb-3 ml-1 customGradient" dark v-bind="attrs" v-on="on" @click="getMaterials(); getFinishes(); getFinishGroups(); added2 = false, failed2 = false"> Alterar Materiais </v-btn>
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
                  @change="getFinishesUsed(); selected=true; "
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
              <v-select
                  :items=availableFinishGroups
                  label="Acabamentos obrigatórios"
                  required
                  multiple
                  v-model = mandatoryFinishGroups
                ></v-select>
                </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
              <v-select
                  :items=availableFinishes
                  label="Acabamentos adicionais"
                  required
                  multiple
                  v-model = mandatoryFinishes
                ></v-select>
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
            @click="updateMaterial(); selected = false; picked = null; newMaterialName = null; mandatoryFinishGroups = [], mandatoryFinishes = []"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
</v-row>
<v-row>
<v-dialog
      v-model="dialog3"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 mr-1 customGradient" dark v-bind="attrs" v-on="on" @click="getMaterials(); getFinishes(); added1 = false; failed1 = false"> Adicionar preço </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added1 && !failed1"> Adicionar preço por medida</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="12" v-show="added1">
              <span class="text-h5"> O preço foi adicionado com sucesso! </span> 
              </v-col>
              <v-col cols="12" v-show="failed1">
              <span class="text-h5"> Ocorreu um erro a adicionar o preço </span> 
              </v-col>
              <v-col
                cols="6"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Preço por m2"
                  required
                  v-model="height"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Preço fixo"
                  required
                  v-model="width"
                ></v-text-field>
              </v-col>
              <v-col
                cols="6"
                v-show="!added1 && !failed1"
              >
                <v-autocomplete
                  label="Material"
                  :items=available
                  required
                  v-model="materialToAdd"
                ></v-autocomplete>
              </v-col>
              <v-col
                cols="6"
              >
                <v-select
                  :items=availableFinishes
                  label="Acabamentos"
                  required
                  v-model = pickedFinish
                  multiple
                  v-show="!added1 && !failed1"
                ></v-select>
              </v-col>

            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog3 = false; height = null; width = null; price = null, materialToAdd = null, pickedFinish = null"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added1 && !failed1"
            @click="addPrice();"
          >
            Adicionar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <template>
    <v-dialog
      v-model="dialog4"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 ml-1 customGradient" dark v-bind="attrs" v-on="on" @click="getPrices(), getMaterials(), getFinishes(), added2 = false, failed2 = false"> Alterar preço </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added2 && !failed2 && !toDelete && !toDelete2"> Preços por medida </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="auto" v-show="added2">
              <span class="text-h5"> O preço foi alterado com sucesso! </span> 
              </v-col>
              <v-col cols="auto" v-show="failed2">
              <span class="text-h5"> Ocorreu um erro a alterar o preço </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete">
              <span class="text-h6"> Tem a certeza que pretende eliminar o preço para a medida da base de dados? (pressione o botão novamente para apagar) </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete2">
              <span class="text-h5"> O preço foi eliminado com sucesso! </span> 
              </v-col>
              <v-col
                cols="10"
              >
                <v-select
                  :items=pricesList
                  required
                  v-model = picked
                  @change="selected=true; getPriceForSpecific()"
                  v-show="!added2 && !failed2 && !toDelete && !toDelete2"
                ></v-select>
              </v-col>
              <v-col
                cols="9"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
              Preço atual por m2: {{currPrice.costPerSquareMeter}}
              Preço fixo: {{currPrice.fixedCost}}
              Material: {{currPrice.material}}
              Acabamentos:
              <v-select
                  :items=availableFinishes
                  required
                  multiple
                  v-model = pickedFinish
                  v-show="!added2 && !failed2 && !toDelete && !toDelete2"
                ></v-select>
            </v-col>
              <v-col
                cols="5"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  label="Novo preço por m2"
                  required
                  v-model="height"
                ></v-text-field>
              </v-col>
              <v-col
                cols="5"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  label="Novo custo fixo"
                  required
                  v-model="width"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
              <v-autocomplete
                  label="Material"
                  :items=available
                  required
                  v-model="materialToAdd"
                ></v-autocomplete>
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
            @click="dialog4 = false; selected = false; picked = null; toDelete = false, toDelete2 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  v-show="toDelete"
                  @click="deletePrice(); toDelete = false; toDelete2 = true;"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
            @click="updatePrice(); selected = false; picked = null; newPrice = null; newWidth = null; newHeight = null"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
</v-row>
<v-row>
<template>
    <v-dialog
      v-model="dialog5"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 mr-1 customGradient" dark v-bind="attrs" v-on="on" @click="getMaterials(); added1 = false; failed1 = false"> Adicionar Acabamentos </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added1 && !failed1"> Adicionar Acabamentos </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col v-show="added1">
              <span class="text-h5"> O Acabamento foi adicionado com sucesso! </span> 
              </v-col>
              <v-col v-show="failed1">
              <span class="text-h5"> Ocorreu um erro a adicionar o acabamento </span> 
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Nome do acabamento"
                  required
                  v-model="finishName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Custo do acabamento"
                  required
                  v-model="finishCost"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-select
                  label="Materiais do acabamento"
                  required
                  multiple
                  :items=available
                  v-model="finishMaterials"
                ></v-select>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog5 = false; finishName = null; finishCost = null; finishMaterials = []"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added1 && !failed1"
            @click="addFinish();"
          >
            Guardar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<template>
    <v-dialog
      v-model="dialog6"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 ml-1 customGradient" dark v-bind="attrs" v-on="on" @click="getFinishes(); added2 = false, failed2 = false, pickedFinish = null"> Alterar Acabamentos </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added2 && !failed2 && !toDelete && !toDelete2"> Acabamentos </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="auto" v-show="added2">
              <span class="text-h5"> O Acabamento foi alterado com sucesso! </span> 
              </v-col>
              <v-col cols="auto" v-show="failed2">
              <span class="text-h5"> Ocorreu um erro a alterar o acabamento </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete">
              <span class="text-h6"> Tem a certeza que pretende eliminar o acabamento da base de dados? (pressione o botão novamente para apagar) </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete2">
              <span class="text-h5"> O Acabamento foi eliminado com sucesso! </span> 
              </v-col>
              <v-col
                cols="8"
              >
                <v-select
                  hide-details
                  :items=availableFinishes
                  required
                  v-model = pickedFinish
                  @change="selected=true; setObsolete()"
                  v-show="!added2 && !failed2 && !toDelete && !toDelete2"
                ></v-select>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  hide-details
                  label="Novo nome do Acabamento"
                  required
                  v-model="newFinishName"
                ></v-text-field>
              </v-col>

              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
              <v-col
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Custo do acabamento"
                  required
                  v-model="newFinishCost"
                ></v-text-field>
              </v-col>
              <v-col
                v-show="!added1 && !failed1"
              >
                <v-select
                  label="Materiais do acabamento"
                  required
                  multiple
                  :items=available
                  v-model="newFinishMaterials"
                ></v-select>
              </v-col>
              <v-col
                class="ml-16"
                v-show="!added1 && !failed1"
              >
                <v-checkbox label="Obsoleto?" v-model="obsolete">

                </v-checkbox>
              </v-col>
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
            @click="dialog6 = false; selected = false; picked = null; toDelete = false, toDelete2 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  v-show="toDelete"
                  @click="deleteFinish(); toDelete = false; toDelete2 = true;"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
            @click="updateFinish(); selected = false; picked = null; newFinishName = null; newFinishCost = null; newfinishMaterials = null;"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
</v-row>
<v-row>
<template>
    <v-dialog
      v-model="dialog7"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 mr-1 customGradient" dark v-bind="attrs" v-on="on" @click="getFinishes(); added1 = false; failed1 = false"> Adicionar grupo de acabamentos </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added1 && !failed1"> Adicionar grupo de acabamentos </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col v-show="added1">
              <span class="text-h5"> O grupo foi adicionado com sucesso! </span> 
              </v-col>
              <v-col v-show="failed1">
              <span class="text-h5"> Ocorreu um erro a adicionar o grupo </span> 
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-text-field
                  label="Nome do grupo de acabamentos"
                  required
                  v-model="groupFinishName"
                ></v-text-field>
              </v-col>
              <v-col
                cols="8"
                v-show="!added1 && !failed1"
              >
                <v-select
                  label="Acabamentos do grupo"
                  required
                  multiple
                  :items=availableFinishes
                  v-model="pickedFinish"
                ></v-select>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            color="blue darken-1"
            text
            @click="dialog7 = false; groupFinishName = null; pickedFinish = null;"
          >
            Voltar
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="!added1 && !failed1"
            @click="addGroupFinish();"
          >
            Guardar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<template>
    <v-dialog
      v-model="dialog8"
      persistent
      content-class="rounded-0"
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
      <v-btn height="60" width="310" tile class="mb-3 ml-1 customGradient" dark v-bind="attrs" v-on="on" @click="getFinishGroups(); getFinishes(); added2 = false, failed2 = false"> Alterar grupo de acabamentos </v-btn>
      </template>
      <v-card tile>
        <v-card-title class="justify-center">
          <span class="text-h5" v-show="!added2 && !failed2 && !toDelete && !toDelete2"> Grupo de acabamentos </span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="auto" v-show="added2">
              <span class="text-h5"> O grupo foi alterado com sucesso! </span> 
              </v-col>
              <v-col cols="auto" v-show="failed2">
              <span class="text-h5"> Ocorreu um erro a alterar o grupo </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete">
              <span class="text-h6"> Tem a certeza que pretende eliminar o grupo da base de dados? (pressione o botão novamente para apagar) </span> 
              </v-col>
              <v-col cols="auto" v-show="toDelete2">
              <span class="text-h5"> O grupo foi eliminado com sucesso! </span> 
              </v-col>
              <v-col
                cols="8"
              >
                <v-select
                  :items=availableFinishGroups
                  required
                  v-model = pickedFinish
                  @change="selected=true"
                  v-show="!added2 && !failed2 && !toDelete && !toDelete2"
                ></v-select>
              </v-col>
              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
                <v-text-field
                  label="Novo nome do grupo"
                  required
                  v-model="newFinishName"
                ></v-text-field>
              </v-col>

              <v-col
                cols="8"
                v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
              >
              <v-col
                v-show="!added2 && !failed2"
              >
                <v-select
                  label="Acabamentos do grupo"
                  required
                  multiple
                  :items=availableFinishes
                  v-model="pickedFinishes"
                ></v-select>
              </v-col>
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
            @click="dialog8 = false; selected = false; pickedFinish = null; toDelete = false, toDelete2 = false;"
          >
            Voltar
          </v-btn>
          <v-btn
                  class="mx-2 customGradient"
                  fab
                  dark
                  small
                  v-show="toDelete"
                  @click="deleteFinishGroup(); toDelete = false; toDelete2 = true;"
                >
                  <v-icon dark>
                    mdi-delete
                  </v-icon>
                </v-btn>
          <v-btn
            color="blue darken-1"
            text
            v-show="selected && !added2 && !failed2 && !toDelete && !toDelete2"
            @click="updateFinishGroup(); selected = false; picked = null; newFinishName = null; newFinishCost = null; newfinishMaterials = null;"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
</v-row>
          </v-row>
          </v-card>         
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
      <v-img :src="myImage" contain height="180" width="180"></v-img>
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
        obsolete: false,
        dialog: false,
        dialog1: false,
        dialog3: false,
        dialog4: false,
        dialog5: false,
        dialog6: false,
        dialog7: false,
        dialog8: false,
        store,
        materialName: null,
        materialToAdd: null,
        newMaterialName: null,
        allMaterials: null,
        allFinishes: null,
        availableFinishes: [],
        available: [],
        selected: false,
        added1: false,
        picked: null,
        failed1: false,
        added2: false,
        failed2: false,
        toDelete: false,
        toDelete2: false,
        price: null,
        height: null,
        width: null,
        newPrice: null,
        newHeight: null,
        newWidth: null,
        pricesList: [],
        allPrices: null,
        currPrice: 0,
        finishName : null,
        finishCost : null,
        finishMaterials: [],
        newFinishName : null,
        newFinishCost : null,
        newFinishMaterials: [],
        groupFinishName: null,
        pickedFinish: null,
        allFinishGroups: null,
        availableFinishGroups: [],
        pickedFinishes: [],
        mandatoryFinishes: [],
        mandatoryFinishGroups: [],
      }
    },

methods: {
  
  addMaterial: async function () {
      try {
        var finishToUse = []
        this.allFinishes.forEach(finish => {
          if(this.mandatoryFinishes.includes(finish.name))
          finishToUse.push({id: finish.id})
        })
        var finishGroupToUse = []
        this.allFinishGroups.forEach(finish => {
          if(this.mandatoryFinishGroups.includes(finish.name))
          finishGroupToUse.push({id: finish.id})
        })
        var matToAdd = {name: this.materialName, mandatoryFinishings: finishGroupToUse, additionalFinishings: finishToUse}
        console.log(matToAdd) 
        await Backend.addMaterial(matToAdd)
        this.added1 = true
      } catch (error) {

        this.failed1 = true
        console.error(error)
      }
    },

    addFinish: async function () {
      try {
        var matId = [];
        this.allMaterials.forEach(material => {
          if(this.finishMaterials.includes(material.name))
            matId.push({id: material.id})
        })
        var finishToAdd = {name: this.finishName, cost: this.finishCost, materials: matId}
        console.log(finishToAdd)
        await Backend.addFinish(finishToAdd)
        this.added1 = true
      } catch (error) {

        this.failed1 = true
        console.error(error)
      }
    },

    addGroupFinish: async function () {
      try {
        var group = [];
        this.allFinishes.forEach(material => {
          if(this.pickedFinish.includes(material.name))
            group.push({id: material.id})
        })
        console.log(this.groupFinishName, group)
        await Backend.addFinishGroup(this.groupFinishName, group)
        this.added1 = true
      } catch (error) {

        this.failed1 = true
        console.error(error)
      }
    },

    getFinishesUsed(){
      var materi = this.allMaterials.find(material => material.name == this.picked)
      var lista = []
      materi.mandatoryFinishings.forEach(finish => {
        lista.push(finish.name)
      })
      this.mandatoryFinishGroups = lista
      lista = []
      materi.additionalFinishings.forEach(finish => {
        lista.push(finish.name)
      })
      this.mandatoryFinishes = lista
      this.newMaterialName = this.picked
      console.log(this.mandatoryFinishGroups)
    },

    getPriceForSpecific(){
      console.log(this.picked)
      var measures = this.picked.split('id: ')
      var aux = measures[1].split(', ')
      console.log(measures)
      console.log(aux)
      this.currPrice = this.allPrices.find(x => x.id == aux[0])
      if(this.allMaterials.find(x => x.id == this.currPrice.material) !== undefined){
        this.currPrice.material = this.allMaterials.find(x => x.id == this.currPrice.material).name
      }
      console.log(this.currPrice)
      var lista = []
      this.currPrice.finishings.forEach(finish => {
        lista.push(finish.name)
      })
      this.pickedFinish = lista
      this.materialToAdd = this.currPrice.material
      this.height = this.currPrice.costPerSquareMeter
      this.width = this.currPrice.fixedCost
      console.log(this.pickedFinish)
    },

    addPrice: async function () {
      try {
        var matId = null;
        this.allMaterials.forEach(material => {
          if(this.materialToAdd == material.name)
            matId = material.id
            console.log(material.name)
        })
        var finitos = []
        console.log(this.pickedFinish)
        if(this.pickedFinish != null){
        this.allFinishes.forEach(finish => {
          if(this.pickedFinish.includes(finish.name)){
            finitos.push({id: finish.id})
          }
        })
      }
      console.log(finitos)
        var costToAdd = {material: matId, finishings: finitos, costPerSquareMeter: this.height, fixedCost: this.width}
        console.log(costToAdd)
        await Backend.addPrice(costToAdd)
        this.added1 = true
      } catch (error) {

        this.failed1 = true
        console.error(error)
      }
    },
  
  getFinishes: async function () {
      try {
        this.allFinishes = await Backend.getFinishes()
        this.availableFinishes = []
        this.allFinishes.forEach(element => {
          this.availableFinishes.push(element.name)
        });
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    getFinishGroups: async function () {
      try {
        this.allFinishGroups = await Backend.getFinishGroups()
        this.availableFinishGroups = []
        this.allFinishGroups.forEach(element => {
          this.availableFinishGroups.push(element.name)
        });
      } catch (error) {
        // TODO: Show something
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

    getPrices: async function () {
      try {
        this.allPrices = await Backend.getPrices()
        this.pricesList = []
        this.allPrices.forEach(element => {
          this.pricesList.push("id: " + element.id + ", " + element.costPerSquareMeter + "€/m2,  cf: " + element.fixedCost + ", mat: " + this.allMaterials.find(x => x.id === element.material).name)
        });
        console.log(this.allPrices)
        console.log(this.pricesList)
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

    deleteFinish: async function () {
      try {
        var id = null
        this.allFinishes.forEach(element => {
          if(element.name == this.pickedFinish){
            id = element.id
          }
        });
        await Backend.deleteFinish(id)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    deleteFinishGroup: async function () {
      try {
        var id = null
        this.allFinishGroups.forEach(element => {
          if(element.name == this.pickedFinish){
            id = element.id
          }
        });
        await Backend.deleteFinishGroup(id)
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    deletePrice: async function () {
      try {
          await Backend.deletePrice(this.currPrice.id);
          this.getPrices();
      } catch (error) {
        // TODO: Show something
        console.error(error)
      }
    },

    updatePrice: async function () {
      try{
      var measures = this.picked.split('id: ')
      var aux = measures[1].split(', ')
      var matId = null;
        this.allMaterials.forEach(material => {
          if(this.materialToAdd == material.name)
            matId = material.id
        })
        var finitos = []
        this.allFinishes.forEach(finish => {
          if(this.pickedFinish.includes(finish.name))
          finitos.push({id: finish.id})
        })
        var costToAdd = {id: aux[0], material: matId, finishings: finitos, costPerSquareMeter: this.height, fixedCost: this.width}
        console.log(costToAdd)
          await Backend.updatePrice(costToAdd);
          this.getPrices();
        this.added2 = true;
      } catch (error) {
        // TODO: Show something
        console.error(error)
        this.failed2 = true
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
        var finishToUse = []
        this.allFinishes.forEach(finish => {
          if(this.mandatoryFinishes.includes(finish.name))
          finishToUse.push({id: finish.id})
        })
        var finishGroupToUse = []
        this.allFinishGroups.forEach(finish => {
          if(this.mandatoryFinishGroups.includes(finish.name))
          finishGroupToUse.push({id: finish.id})
        })
        var matToAdd = {id: id, name: this.newMaterialName, mandatoryFinishings: finishGroupToUse, additionalFinishings: finishToUse}
        console.log(matToAdd)
        await Backend.updateMaterial(matToAdd)
        this.added2 = true
      } catch (error) {
        this.failed2 = true 
        // TODO: Show something
        console.error(error)
      }
    },

    setObsolete: function () {
      var selFin = this.allFinishes.find(x => x.name == this.pickedFinish)
      this.obsolete = selFin.obsolete
    },  

    updateFinish: async function () {
      try {
        var idToUse = null
        var secondName = null
        this.allFinishes.forEach(element => {
          if(element.name == this.pickedFinish){
            idToUse = element.id
            secondName = element.name
          }
        });
        var matId = [];
        this.allMaterials.forEach(material => {
          if(this.newFinishMaterials.includes(material.name))
            matId.push({id: material.id})
        })
        this.newFinishName = this.newFinishName == null ? secondName : this.newFinishName
        var finishToAdd = {id: idToUse, name: this.newFinishName, cost: this.newFinishCost, materials: matId, obsolete: this.obsolete}
        await Backend.updateFinish(finishToAdd)
        this.obsolete = false
        this.added2 = true
      } catch (error) {
        this.failed2 = true 
        // TODO: Show something
        console.error(error)
      }
    },

    updateFinishGroup: async function () {
      try {
        var idToUse = null
        this.allFinishGroups.forEach(element => {
          if(element.name == this.pickedFinish){
            idToUse = element.id
          }
        });
        var finId = [];
        this.allFinishes.forEach(material => {
          if(this.pickedFinishes.includes(material.name))
            finId.push({id: material.id})
        })
        console.log(finId)
        var finishToAdd = {id: idToUse, name: this.newFinishName, finishings: finId}
        await Backend.updateFinishGroup(finishToAdd)
        this.added2 = true
      } catch (error) {
        this.failed2 = true 
        // TODO: Show something
        console.error(error)
      }
    }


},
created: async function () {
  this.getMaterials();
  this.getPrices();
},
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
