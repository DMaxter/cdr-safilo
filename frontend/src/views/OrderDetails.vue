<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center" class="d-print-none">
        <v-col cols="auto" >
          <v-card elevation="12" :height="$vuetify.breakpoint.height > 620
                ? '600px' 
                : '480px'" width="800" tile style="background-color: #E0E0E0">
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
              <v-btn color="#808080" value="left" @click="$router.push('profile')" height="64" width="160" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center1" @click="$router.push('history')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Histórico</span>

                <v-icon right>
                  mdi-clock
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#808080" value="right" @click="$router.push('orderClient')" height="64" width="160" class="customGradient">
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
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="customGradient">
                  <span class="white--text" style="font-size: 12px">Perfil</span>

                <v-icon right>
                  mdi-account-circle
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
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

              <v-btn color="#6e4e5d" value="right" @click="$router.push('materiais')" height="64" width="160" class="customGradient">
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
              <v-btn color="#6e4e5d" value="left" height="64" @click="$router.push('profile')" width="160" class="customGradient">
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

              <v-btn color="#6e4e5d" value="center2" @click="$router.push('search')" height="64" width="160" class="v-btn--active customGradient">
                <span class="white--text" style="font-size: 12px">Procurar</span>

                <v-icon right>
                  mdi-magnify
                </v-icon>
              </v-btn>

              <v-btn color="#6e4e5d" value="right" @click="$router.push('configure')" height="64" width="160" class="customGradient">
                <span class="white--text" style="font-size: 12px">Configurar</span>

                <v-icon right>
                  mdi-cog
                </v-icon>
              </v-btn>

            </v-btn-toggle>
          </v-menu>
        </template>
          </v-row>
          
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" :style="$vuetify.breakpoint.height > 620
                ? 'height: 480px' 
                : 'height: 350px'">
                <v-dialog
              v-model="nota"
              transition="dialog-bottom-transition"
              max-width="800"
              id="dialogo"
            >
              <template v-if="show">
                <v-card>
                  <v-card-title class="justify-center"> <span v-show="!added && !failed"> {{ this.currClient.name }}; {{ this.currClient.address }}; {{ this.currClient.postalCode }} </span></v-card-title>
                  <v-card-text>
                    <div class="text-h6 mb-5" v-show="!added && !failed">Notas:</div>
                    <v-textarea tile outlined style="pointer-events: none;" hide-details auto-grow v-model="noteToAdd"></v-textarea>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn
                      text
                      class="mr-2"
                      @click="nota = false"
                    >Voltar</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          <h3> PEDIDO {{store.pedidoAtual.cod}} - CÓD. CLIENTE 
            <span v-if="show" style="color: #e361b0" @click="dialog5 = true"> <u> {{ store.pedidoAtual.codClient }} </u></span> 
            <span v-else> {{ store.pedidoAtual.codClient }} </span>
          </h3>
            <v-card elevation="12" color="#FAFAFA" :height="$vuetify.breakpoint.height > 620
                ? '450px' 
                : '320px'" :width="$vuetify.breakpoint.height > 620
                ? '400px' 
                : '600px'" tile>
          <template v-if="$vuetify.breakpoint.height > 620">
          
              <v-row justify="space-between" align="start" class="d-flex flex-column fill-height mt-0">
              <v-col cols="auto" class="ml-4"> Data:  {{store.pedidoAtual.data}}</v-col>
              <v-col cols="auto" class="ml-4"> Marca: {{store.pedidoAtual.marca}} </v-col>
              <v-col cols="auto" class="ml-4"> Quantidade:  {{store.pedidoAtual.amount}}</v-col>
              <v-col cols="auto" class="ml-4"> Modelo: {{store.pedidoAtual.modelo}} </v-col>
              <v-col cols="auto" class="ml-4"> Material:</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-2': theme}"
              >
                <v-slide-item
                  v-for="n in store.pedidoAtual.material.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.pedidoAtual.material[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              <v-col cols="auto" class="ml-4"> Acabamentos:</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-2': theme}"
              >
                <v-slide-item
                  v-for="n in store.finishes.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.finishes[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              <v-col cols="auto" class="ml-4"> Dimensões (altura x largura, em cm):</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-2': theme}"
              >
                <v-slide-item
                  v-for="n in store.pedidoAtual.dimensoes.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.pedidoAtual.dimensoes[n-1].height}}, {{store.pedidoAtual.dimensoes[n-1].width}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              </v-row>
            </template>
            <template v-else>
              <v-row justify="space-between" align="start" class="d-flex flex-column mt-0">
              <v-row justify="center" align="center" class="d-flex">
              <v-col cols="auto" class="ml-7"> Data:  {{store.pedidoAtual.data}}</v-col>
              <v-col cols="auto" class="ml-7"> Marca: {{store.pedidoAtual.marca}} </v-col>
              <v-col cols="auto" class="ml-7"> Quantidade: {{store.pedidoAtual.amount}} </v-col>
              <v-col cols="auto" class="ml-7"> Modelo: {{store.pedidoAtual.modelo}} </v-col>
              </v-row>
              <v-col cols="auto" class="ml-4"> Material:</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-4': theme}"
              >
                <v-slide-item
                  v-for="n in store.pedidoAtual.material.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.pedidoAtual.material[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              <v-col cols="auto" class="ml-4"> Acabamentos:</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-4': theme}"
              >
                <v-slide-item
                  v-for="n in store.finishes.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.finishes[n-1]}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              <v-col cols="auto" class="ml-4"> Dimensões (altura x largura, em cm):</v-col>
              <v-slide-group
                multiple
                show-arrows
                :class="{'ml-4': theme}"
              >
                <v-slide-item
                  v-for="n in store.pedidoAtual.dimensoes.length"
                  :key="n"
                  v-slot="{ active, toggle }"
                >
                  <v-btn
                    :input-value="active"
                    active-class="purple white--text"
                    depressed
                    class="ml-5 mr-5"
                    dense
                    @click="toggle"
                  >
                    {{store.pedidoAtual.dimensoes[n-1].height}}, {{store.pedidoAtual.dimensoes[n-1].width}}
                  </v-btn>
                </v-slide-item>
              </v-slide-group>
              </v-row>
            </template>
            </v-card>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex mt-4" :style="$vuetify.breakpoint.height > 620
                ? 'height: 28px' 
                : 'height: 38px'">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="goBack()"
              class="d-flex flex-column customGradient"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
           <v-col cols="auto" class="ml-4 d-flex">
            <v-btn
              class="mr-2 d-flex flex-column customGradient"
              small
              tile
              dark
              @click="print()"
            > <v-icon >mdi-printer</v-icon>
            </v-btn>
            <v-btn
              v-if="!this.hasWaybill && this.show"
              class="ml-2 d-flex flex-column customGradient"
              small
              tile
              dark
              @click="added = false, failed = false, dialog2 = true;"
            > <v-icon >mdi-truck-delivery</v-icon>
            </v-btn>
            <v-btn
              v-if="this.hasWaybill && this.show"
              class="mr-1 d-flex flex-column customGradient"
              small
              tile
              dark
              @click="added = false, failed = false, dialog3 = true;"
            > <v-icon >mdi-download</v-icon>
            </v-btn>
            <v-btn
              v-if="this.hasWaybill && this.show"
              class=" ml-1 d-flex flex-column customGradient"
              small
              tile
              dark
              @click="added = false, failed = false, dialog4 = true;"
            > <v-icon >mdi-close</v-icon>
            </v-btn>
            </v-col>
            <v-col cols="auto" class="pr-4">
            <v-btn
              class="d-flex flex-column customGradient"
              small
              tile
              dark
              @click="getPedido()"
            > Ver Pedido <v-icon >mdi-play</v-icon>
            </v-btn>
            </v-col>
           </v-row>
          </v-card>         
        </v-col>
    </v-row>

<v-dialog v-if="this.show" v-model="dialog2" persistent content-class="rounded-0" max-width="600px">
  <form @submit.prevent="waybill">
    <v-card tile>
      <v-card-title class="d-flex flex-column justify-center pt-3 pb-0">
        <span class="text-h5" v-show="!added && !failed"> Criar carta de porte</span>
        <span style="font-size: 12px" v-show="!added && !failed && !displayLoad"> Campos que começem com * são obrigatórios! </span>
      </v-card-title>
      <v-card-text class="pb-0">
        <v-container>
          <v-row justify="center">
            <span class="text-h5" v-show="added"> Carta de porte criada com sucesso! </span>
                  <span class="text-h5" v-show="failed">Ocorreu um erro a criar a carta de porte:<br/>{{ this.errorMsg }} </span>
            <v-progress-circular indeterminate color="grey" v-show="displayLoad"></v-progress-circular>
            <v-col cols="6" v-show="!added && !failed && !displayLoad">
              <v-autocomplete clearable hide-details label="*Serviço" :items="allServices" item-value="id" item-text="name" required v-model="pickedService"></v-autocomplete>
            </v-col>
            <v-col cols="6" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details type="number" label="*Número de pacotes" min=0 required v-model="numberOfPackages"></v-text-field>
            </v-col>
            <v-col cols="6" v-show="!added && !failed && !displayLoad">
              <v-autocomplete clearable hide-details label="*Tipo de encomenda" :items="packageTypes" item-value="id" item-text="name" required v-model="pickedPackage"></v-autocomplete>
            </v-col>
            <v-col cols="6" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details type="number" label="*Peso Total (Kg)" min=0 step=0.01 required v-model="totalWeight"></v-text-field>
            </v-col>
            <v-col cols="12" v-show="!added && !failed && !displayLoad">
              <v-text-field  hide-details label="*Descrição" required
                v-model="description"></v-text-field>
            </v-col>
            <v-col cols="3" v-show="!added && !failed && !displayLoad">
              <v-autocomplete clearable hide-details label="*Formato" :items="allFormats" item-value="id" item-text="name" required v-model="pickedFormat"></v-autocomplete>
            </v-col>
            <v-col cols="3" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details type="number" label="*Altura (m)" min=0 step=0.01 required
                v-model="packageHeight"></v-text-field>
            </v-col>
            <v-col cols="3" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details type="number" label="*Largura (m)" min=0 step=0.01 required
                v-model="packageWidth"></v-text-field>
            </v-col>
            <v-col cols="3" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details type="number" label="*Comprimento (m)" min=0 step=0.01 required
                v-model="packageLength"></v-text-field>
            </v-col>
            <v-col cols="4" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details label="Nr. de Telefone"
                v-model="phoneNumber"></v-text-field>
            </v-col>
            <v-col cols="4" v-show="!added && !failed && !displayLoad">
              <v-autocomplete clearable hide-details label="País"
                v-model="country" :items="paises"></v-autocomplete>
            </v-col>
            <v-col cols="4" v-show="!added && !failed && !displayLoad">
              <v-text-field hide-details label="Cidade"
                v-model="city"></v-text-field>
            </v-col>
            <v-col cols="6" v-show="!added && !failed && !displayLoad" class="pb-3">
              <v-text-field hide-details label="Morada"
                v-model="address"></v-text-field>
            </v-col>
            <v-col cols="6" v-show="!added && !failed && !displayLoad" class="pb-3">
              <v-text-field hide-details label="Cód. Postal"
                v-model="postalCode"></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions class="justify-center pt-0 pb-1">
        <v-btn color="blue darken-1" text @click="dialog2 = false">
          Voltar
        </v-btn>
        <v-btn color="blue darken-1" text v-show="!added && !failed && !displayLoad" type="submit">
          Criar
        </v-btn>
      </v-card-actions>
    </v-card>
  </form>
</v-dialog>

<v-dialog v-if="this.show" v-model="dialog3" persistent content-class="rounded-0" max-width="600px">
  <v-card tile>
    <v-card-title class="justify-center">
      <span class="text-h5" v-show="!added && !failed"> Descarregar carta de porte</span>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row justify="center">
          <span class="text-h5" v-show="added"> Carta de porte descarregada com sucesso! </span>
                <span class="text-h5" v-show="failed">Ocorreu um erro a descarregar a carta de porte:<br/>{{ this.errorMsg }} </span>
          <v-progress-circular indeterminate color="grey" v-show="displayLoad"></v-progress-circular>
          <v-col cols="6" v-show="!added && !failed && !displayLoad">
            <v-select hide-details label="Formato" :items="allFormats" item-value="id" item-text="name" required v-model="pickedFormat"></v-select>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions class="justify-center">
      <v-btn color="blue darken-1" text @click="dialog3 = false; pickedFormat = null;">
        Voltar
      </v-btn>
      <v-btn color="blue darken-1" text v-show="!added && !failed && !displayLoad" @click="downloadWaybill(); pickedFormat = null;">
        Descarregar
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>

<v-dialog v-if="this.show" v-model="dialog4" persistent content-class="rounded-0" max-width="600px">
  <v-card tile>
    <v-card-title class="justify-center">
      <span class="text-h5" v-show="!added && !failed"> Remover carta de porte</span>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row justify="center">
          <span class="text-h5" v-show="added"> Carta de porte removida com sucesso! </span>
                <span class="text-h5" v-show="failed">Ocorreu um erro ao remover a carta de porte:<br/>{{ this.errorMsg }}</span>
          <v-progress-circular indeterminate color="grey" v-show="displayLoad"></v-progress-circular>
          <v-col cols="12" v-show="!added && !failed && !displayLoad">
            <span class="text-h6" hide-details> Pretende remover a carta de porte deste pedido? </span>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions class="justify-center">
      <v-btn color="blue darken-1" text @click="dialog4 = false;">
        Voltar
      </v-btn>
      <v-btn color="blue darken-1" text v-show="!added && !failed && !displayLoad" @click="deleteWaybill();">
        Remover
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>

<v-dialog v-if="this.show" v-model="dialog5" content-class="rounded-0" max-width="600px">
  <v-card tile>
    <v-card-title class="justify-center">
      <span class="text-h6" v-show="!added && !failed"> Informações do Cliente {{currClient.id}}</span>
    </v-card-title>
    <v-card-text class="pb-0">
      <v-container>
        <v-row justify="center">
          <v-col cols="12"> <span class="text-h6"> Nome: </span> <span class="text-body-1"> {{currClient.name}} </span></v-col>
          <v-col cols="12"> <span class="text-h6"> Email: </span> <span class="text-body-1">{{currClient.email}}</span></v-col>
          <v-col cols="12"> <span class="text-h6"> Telefone: </span> <span class="text-body-1">{{currClient.phone}}</span></v-col>
          <v-col cols="12"> <span class="text-h6"> Morada: </span> <span class="text-body-1"> {{currClient.address}}</span></v-col>
          <v-col cols="12"> <span class="text-h6"> Cód. Postal: </span> <span class="text-body-1">{{currClient.postalCode}}</span></v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions class="justify-center">
      <v-btn color="blue darken-1" text @click="dialog5 = false;">
        Voltar
      </v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>
    <!-- FOR PRINTING STUFF !!!!!!!!!!!!!!!!!-->
    
    <v-container class="d-none" :class="req1 && 'd-print-block'">
      <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face 1</h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat}} </v-col>
                </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[0] width="700" height="500" style="object-fit:contain;">
          </v-row>
          <v-row justify="space-around">
            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Observações: </h3>
              {{store.pedidoAtual.observations}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Quantidade: </h3>
              {{store.pedidoAtual.amount}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

              <h3> Custo: </h3>
              {{store.pedidoAtual.cost}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>
    </v-container>

    <v-container v-if="req2" fill-height class="d-none" :class="req2 && 'd-print-block'">
    <br>
    <br>
    <br>
    <br>
  
      <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face 1</h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[0]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[0] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face 2 </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[1]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[1].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[1].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[1]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[1] width="700" height="500" style="object-fit:contain;">
          </v-row>
          <v-row justify="space-around">
            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Observações: </h3>
              {{store.pedidoAtual.observations}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Quantidade: </h3>
              {{store.pedidoAtual.amount}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

              <h3> Custo: </h3>
              {{store.pedidoAtual.cost}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>

    </v-container>

    <v-container v-if="(req3 || req4)" fill-height class="d-none" :class="(req3 || req4) && 'd-print-block'">
    <br>
    <br>
    <br>
    <br>
  
      <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face A</h3>
            <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[0]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[0] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>
    
    <br>
    <br>
    <br>
    <br>


    <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face B </h3>
            <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[1]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[1].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[1].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[1]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[1] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face C </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[2]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[2].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[2].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[2]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[2] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>


    <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face D </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[3]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[3].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[3].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[3]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[3] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face E </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[4]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[4].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[4].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[4]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[4] width="700" height="500" style="object-fit:contain;">
          </v-row>
          <v-row justify="space-around">
            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Observações: </h3>
              {{store.pedidoAtual.observations}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Quantidade: </h3>
              {{store.pedidoAtual.amount}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

              <h3> Custo: </h3>
              {{store.pedidoAtual.cost}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>
    </v-container>

    <v-container v-if="(req5)" fill-height class="d-none" :class="(req5) && 'd-print-block'">
    <br>
    <br>
    <br>
    <br>
  
      <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face A</h3>
            <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[0]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[0] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face B </h3>
            <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[1]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[1].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[1].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[1]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[1] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center" class="bruh">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face C </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[2]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[2].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[2].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[2]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[2] width="700" height="500" style="object-fit:contain;">
          </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>
    
    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}, Cliente: {{ store.pedidoAtual.nameClient }}, Cód: {{ store.pedidoAtual.codClient }}</h3>
          <h3 class="mb-6"> Face D </h3>
          <v-card color="#FAFAFA" height="90" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[3]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[3].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[3].width}} cm </v-col>
              </v-row>
              <v-row no-gutters justify="center" class="d-flex mt-1">
                <v-col cols="auto" class="mt-2"> Acabamentos: {{finiat[3]}} </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row justify="center" class="mt-8"> 
            <h3> Imagem: </h3>
          </v-row>
          <v-row justify="center"> 
            <img :src=store.pedidoAtual.images[3] width="700" height="500" style="object-fit:contain;">
          </v-row>
          <v-row justify="space-around">
            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Observações: </h3>
              {{store.pedidoAtual.observations}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Quantidade: </h3>
              {{store.pedidoAtual.amount}}
            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

              <h3> Custo: </h3>
              {{store.pedidoAtual.cost}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>

    </v-container>

    <!-- NOT FOR PRINTING STUFF !!!!!!!!!!!!!!!!!-->

    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex d-print-none"> 
        <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>

    </v-container>

  </v-app>
</template>

<script>
import { store } from '@/store.js'
import Backend from '@/router/backend'
import UserDto from "@/models/UserDto";

export default {
  name: 'CustomerHistory',

  components: {
  },

data () {
      return {
        errorMsg: "",
        phoneNumber: null,
        address: null,
        city: null,
        country: null,
        postalCode: null,
        paises: ["PT", "ES"],
        myImage2: require('@/assets/logologo1.png'),
        store,
        packageHeight: null,
        packageWidth: null,
        packageLength: null,
        numberOfPackages: null,
        packageTypes: null,
        allFormats: null,
        pickedFormat: null,
        requests: null,
        show: false,
        totalWeight: null,
        description: "",
        profile: new UserDto(),
        picked: null,
        dialog1: false,
        dialog2: false,
        dialog3: false,
        dialog4: false,
        dialog5: false,
        currClient: null,
        noteToAdd: "",
        nota: false,
        menu1: false,
        menu2: true,
        menu3: false,
        req1: false,
        req2: false,
        req3: false,
        req4: false,
        req5: false,
        finiat: null,
        allClients: null,
        allServices: null,
        pickedService: null,
        pickedPackage: null,
        added: false,
        failed: false,
        hasWaybill: false,
        displayLoad: false,
        theme: (store.pedidoAtual.modelo == "OneFace" || store.pedidoAtual.modelo == "TwoFaces"),
        available: [
          "IN_PRODUCTION",
          "DONE",
          "CANCELLED"
        ]
      }
    },
async created(){
  if(this.$route.query.id != null){
        this.requests = await Backend.getRequests()
        this.requests.forEach(element => {
          var date = element.created
          date = date.slice(0, 10)
          element.created = date
          element.cost = element.cost.toFixed(2)
        });
        var item = this.requests.filter(x => x.id == this.$route.query.id)[0]
        store.trackingCode = item.trackingCode
        store.address = item.client.address
        store.postalCode = item.client.postalCode
        if(item.type.type == "OneFace"){
        store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.cover.material.name],
            dimensoes: [item.type.cover.measurements],
            estado: item.status,
            images: [item.type.cover.image.link],
            amount: item.amount,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: item.type.cover.finishings,
            codClient: item.client.id,
            nameClient: item.client.name

          }
        } else if(item.type.type == "TwoFaces"){
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.cover.material.name, item.type.back.material.name],
            dimensoes: [item.type.cover.measurements, item.type.back.measurements],
            estado: item.status,
            images: [item.type.cover.image.link, item.type.back.image.link],
            amount: item.amount,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.cover.finishings, item.type.back.finishings],
            codClient: item.client.id,
            nameClient: item.client.name

          }
        } else if(item.type.type == "RightShowcase" || item.type.type == "LeftShowcase"){
            if(item.type.type == "RightShowcase"){
              store.isActive2 = true
            } else if(item.type.type == "LeftShowcase"){
              store.isActive3 = true
            }
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.top.material.name, item.type.bottom.material.name, item.type.left.material.name, item.type.right.material.name, item.type.side.material.name],
            dimensoes: [item.type.top.measurements, item.type.bottom.measurements, item.type.left.measurements, item.type.right.measurements, item.type.side.measurements],
            estado: item.status,
            images: [item.type.top.image.link, item.type.bottom.image.link, item.type.left.image.link, item.type.right.image.link, item.type.side.image.link],
            amount: item.amount,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.top.finishings, item.type.bottom.finishings, item.type.left.finishings, item.type.right.finishings, item.type.side.finishings],
            codClient: item.client.id,
            nameClient: item.client.name

          }
        } else if(item.type.type == "SimpleShowcase"){
          store.isActive4 = true
          store.pedidoAtual = {
            cod: item.id,
            data: item.created,
            marca: item.brand.name,
            modelo: item.type.type,
            material: [item.type.top.material.name, item.type.bottom.material.name, item.type.left.material.name, item.type.right.material.name],
            dimensoes: [item.type.top.measurements, item.type.bottom.measurements, item.type.left.measurements, item.type.right.measurements],
            estado: item.status,
            images: [item.type.top.image.link, item.type.bottom.image.link, item.type.left.image.link, item.type.right.image.link],
            amount: item.amount,
            observations: item.observations,
            cost: item.cost,
            application: item.application,
            finishings: [item.type.top.finishings, item.type.bottom.finishings, item.type.left.finishings, item.type.right.finishings],
            codClient: item.client.id,
            nameClient: item.client.name

          }
        }
  }
  console.log(store.pedidoAtual)
  store.finishes = []
  var finAux = []
  console.log(store.address)
  console.log(store.postalCode)
  if(store.pedidoAtual.modelo == "OneFace"){
    store.pedidoAtual.finishings.forEach(fin => {
      if(!finAux.includes(fin.name)){
        finAux.push(fin.name)
      }
    })
    store.finishes = finAux
    this.finiat = ""
    finAux.forEach(fin => {
      this.finiat = this.finiat.concat(fin + "; ")
    })
    this.req1 = true;
  } else if(store.pedidoAtual.modelo == "TwoFaces"){
    console.log(store.pedidoAtual.finishings)
    var no = []
    store.pedidoAtual.finishings.forEach(fin => {
      var str = ""
      console.log(fin)
      if(fin.length > 1){
        fin.forEach(f =>{
          str = str.concat(f.name + "; ")

          if(!finAux.includes(f.name)){
            finAux.push(f.name)
          }
        })
      } else if(fin.length == 1){
        str = str.concat(fin[0].name + "; ")
        if(!finAux.includes(fin[0].name)){
        finAux.push(fin[0].name)
        }
        

      }
      console.log(str)
      no.push(str)
    })
    this.finiat = no
    console.log(finAux)
    store.finishes = finAux
    this.req2 = true;
  } else if(store.pedidoAtual.modelo == "RightShowcase"){
    var no1 = []
    store.pedidoAtual.finishings.forEach(fin => {
      var str1 = ""

      if(fin.length > 1){
        fin.forEach(f =>{
          str1 = str1.concat(f.name + "; ")

          if(!finAux.includes(f.name)){
            finAux.push(f.name)
          }
        })
      } else if(fin.length == 1){
        str1 = str1.concat(fin[0].name + "; ")

        if(!finAux.includes(fin[0].name)){
        finAux.push(fin[0].name)
        }
      }
      no1.push(str1)

    })
    this.finiat = no1

    console.log(finAux)
    store.finishes = finAux
    this.req3 = true;
  } else if(store.pedidoAtual.modelo == "LeftShowcase"){ 
    var no2= []
    store.pedidoAtual.finishings.forEach(fin => {
      var str2 = ""
      console.log(fin)
      console.log(fin.length)
      if(fin.length > 1){
        fin.forEach(f =>{
          str2 = str2.concat(f.name + "; ")
          if(!finAux.includes(f.name)){
            console.log(f.name)
            finAux.push(f.name)
          }
        })
      } else if(fin.length == 1){
        str2 = str2.concat(fin[0].name + "; ")
        console.log(fin.name)
        if(!finAux.includes(fin[0].name)){
        finAux.push(fin[0].name)
        

        }
      }
      console.log(finAux)
      no2.push(str2)
    })
    this.finiat = no2
    console.log(this.finiat)
    console.log(finAux)
    store.finishes = finAux
    this.req4 = true;
  } else if (store.pedidoAtual.modelo == 'SimpleShowcase'){
    var no3 = []
    store.pedidoAtual.finishings.forEach(fin => {
      var str3 = ""

      if(fin.length > 1){
        fin.forEach(f =>{
          str3 = str3.concat(f.name + "; ")

          if(!finAux.includes(f.name)){
            finAux.push(f.name)
          }
        })
      } else if(fin.length == 1){
        str3 = str3.concat(fin[0].name + "; ")

        if(!finAux.includes(fin[0].name)){
        finAux.push(fin[0].name)
        }
      }
      no3.push(str3)

    })
    this.finiat = no3

    console.log(finAux)
    store.finishes = finAux
    this.req5 = true;
  }
  console.log(store.finishes)
  this.profile = await Backend.getProfile()
  this.allClients = await Backend.getClients()
  console.log(this.profile.roles[0])
  if(this.profile.roles[0] == 'CDR'){
    this.allServices = await Backend.getServices(store.pedidoAtual.cod);
    this.packageTypes = await Backend.getPackages()
    this.allFormats = await Backend.getFormats()
    console.log(store.trackingCode)
    if(store.trackingCode != null){
      this.hasWaybill = true
    }
    this.menu2 = true
    this.show = true
    var client = this.allClients.find(x => x.id == store.pedidoAtual.codClient)
    this.currClient = client
    if(client.note != null && client.note != ""){
      this.nota = true
      this.noteToAdd = client.note
    }
  } else if(this.profile.roles[0] == 'COMMERCIAL' || this.profile.roles[0] == 'ADMIN'){ 
    this.menu1 = true
    this.menu2 = false
  } else if(this.profile.roles[0] == 'MANAGER'){
    this.menu3 = true
    this.menu2 = false
  }
  console.log(this.show)
},
methods: {
  print(){
    document.getElementById("footnote").setAttribute("hidden", true)
    window.print();
    document.getElementById("footnote").removeAttribute("hidden")
  },
  goBack(){
    if(this.$route.query.id != null){
      this.$router.push({name: 'profile'});
    } else {
      this.$router.go(-1)
    }
  },
  getPedido() {
    if(store.pedidoAtual.modelo == "OneFace" || store.pedidoAtual.modelo == "TwoFaces"){
      this.$router.push({name: 'detailsOneOrTwo'});
    } else {
      this.$router.push({name: 'detailsMontra'});
    }
  },
  async waybill(){
    this.displayLoad = true;
    try{
      var aaa = {service: {id: this.pickedService},
        items: Number(this.numberOfPackages),
        packaging: {id: this.pickedPackage},
        totalWeight: Number(this.totalWeight),
        description: this.description,
        labelFormat: this.pickedFormat,
        dimensions: {height: Number(this.packageHeight), width: Number(this.packageWidth), length: Number(this.packageLength)},
        destination: {name: this.currClient.name, address: {postalCode: this.postalCode, country: this.country, city: this.city, address: this.address}, phone: this.phoneNumber}
      }
      console.log(aaa)
      await Backend.createWaybill(aaa, store.pedidoAtual.cod)
      this.hasWaybill = true
      this.displayLoad = false
      this.added = true
      this.description = ''
      this.phoneNumber = null
      this.country = null
      this.city = null 
      this.address = null 
      this.postalCode = null 
      this.pickedService = null
      this.numberOfPackages = null
      this.pickedPackage = null
      this.totalWeight = null
      this.pickedFormat = null
      this.packageHeight = null
      this.packageWidth = null
      this.packageLength = null
    }catch(e){
      this.errorMsg = e.message
      this.failed = true
      this.displayLoad = false
    }
  },

  async downloadWaybill(){
    try{
      this.displayLoad = true;
      await Backend.downloadWaybill(this.pickedFormat, store.pedidoAtual.cod)
      this.displayLoad = false;
      this.added = true
    }catch(e){
      this.errorMsg = e.message
      this.failed = true
      this.displayLoad = false;
    }
  },

  async deleteWaybill(){
    try{
      this.displayLoad = true;
      await Backend.deleteWaybill(store.pedidoAtual.cod)
      this.displayLoad = false;
      this.added = true
    }catch(e){
      this.errorMsg = e.message
      this.failed = true
      this.displayLoad = false;
    }
  },

  async updateStatus() {
    await Backend.updateStatus(store.pedidoAtual.cod, this.picked)
    store.pedidoAtual.estado =  this.picked
  }
}
  }
</script>

<style>

#app {
  background: url('@/assets/background.jpg') center center !important;
}

@media print {
.page-break { display: block; page-break-before: always; }
}

.bruh {
  page-break-after: always;
}
.customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>
