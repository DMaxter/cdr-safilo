<template>
<v-app>

  <v-container fill-height>
    <v-row justify="center" align="center" class="d-print-none">
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
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 470px">
          <h3 class="mb-2"> PEDIDO {{store.pedidoAtual.cod}}</h3>
            <v-card elevation="12" color="#FAFAFA" height="400" width="400" tile>
              <v-row justify="space-between" align="start" class="d-flex flex-column fill-height mt-0">
              <v-col cols="auto" class="ml-4"> Data:  {{store.pedidoAtual.data}}</v-col>
              <v-col cols="auto" class="ml-4"> Marca: {{store.pedidoAtual.marca}} </v-col>
              <v-col cols="auto" class="ml-4"> Modelo: {{store.pedidoAtual.modelo}} </v-col>
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
              <v-col cols="auto" class="d-flex ml-4"> Estado: {{store.pedidoAtual.estado}}
                <template>
    <v-dialog
      v-if="this.show"
      v-model="dialog1"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="d-flex ml-5 customGradient" small dark tile v-bind="attrs" v-on="on"> Alterar 
              <v-icon class="pl-1" size="15" dark> mdi-cog-outline </v-icon>
        </v-btn>  
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <span class="text-h5"> Alterar estado do pedido </span>
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
                ></v-select>
              </v-col>
              <v-col
                cols="12"
              >
              Estado atual: {{store.pedidoAtual.estado}}
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
            @click="updateStatus(); dialog1 = false;"
          >
            Alterar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
              </v-col>
              </v-row>
            </v-card>
          </v-row>
          <v-row no-gutters align="end" justify="space-between" class="d-flex mt-4" style="height: 28px">
           <v-col cols="auto" class="pl-4">
            <v-btn
              @click="$router.go(-1)"
              class="d-flex flex-column customGradient"
              small
              tile
              dark
            > <v-icon style="transform: rotate(180deg);">mdi-play</v-icon>
            Voltar
            </v-btn>
           </v-col>
           <v-col cols="auto" class="ml-4">
            <v-btn
              class="d-flex flex-column customGradient"
              small
              tile
              dark
              @click="print()"
            > <v-icon >mdi-printer</v-icon>
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
    <!-- FOR PRINTING STUFF !!!!!!!!!!!!!!!!!-->
    
    <v-containter class="d-none" :class="req1 && 'd-print-block'">
      <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face {{store.pedidoAtual.marca.length}}</h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
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
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex"> 
        <img :src="myImage2" style="object-fit:contain;" height="150" width="150">
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>
    </v-containter>

    <v-containter v-if="req2" fill-height class="d-none" :class="req2 && 'd-print-block'">
    <br>
    <br>
    <br>
    <br>
  
      <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face 1</h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
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
    <v-row style="position: fixed; bottom: 10px; right: 5px;" class="d-flex"> 
        <img :src="myImage2" style="object-fit:contain;" height="150" width="150">
    </v-row>
    <v-row style="position: fixed; bottom: 30px; right: 25px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>

    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face 2 </h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[1]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[1].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[1].width}} cm </v-col>
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
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    </v-containter>

    <v-containter v-if="(req3 || req4)" fill-height class="d-none" :class="(req3 || req4) && 'd-print-block'">
    <br>
    <br>
    <br>
    <br>
  
      <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face A</h3>
            <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[0]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[0].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[0].width}} cm </v-col>
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
    <v-row style="position: fixed; bottom: 10px; right: 5px;" class="d-flex"> 
        <img :src="myImage2" style="object-fit:contain;" height="150" width="150">
    </v-row>
    <v-row style="position: fixed; bottom: 30px; right: 25px;" class="d-flex flex-column"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
    </v-row>

    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face B </h3>
            <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[1]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[1].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[1].width}} cm </v-col>
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
    <br>
    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face C </h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[2]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[2].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[2].width}} cm </v-col>
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
    <br>
    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face D </h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[3]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[3].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[3].width}} cm </v-col>
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
    <br>
    <br>
    <br>
    <br>
    <br>

    <v-row justify="center" align="center">
        <v-col cols="auto" >
          <v-card elevation="12" height="920" width="785" tile style="background-color: #E0E0E0; bottom: 40px;">
          <v-row justify="center" align="center" class="fill-height d-flex flex-column" style="height: 250px">
          <h3 class="mb-6 mt-6"> PEDIDO {{store.pedidoAtual.cod}} - {{store.pedidoAtual.modelo}}</h3>
          <h3 class="mb-6"> Face E </h3>
          <v-card color="#FAFAFA" height="70" width="700" class="mt-3" tile>
              <v-row no-gutters justify="space-around" align="start" class="d-flex mt-0">
              <v-col cols="auto" class="mt-6"> Marca: {{store.pedidoAtual.marca}}</v-col>
              <v-col cols="auto" class="mt-6"> Material: {{store.pedidoAtual.material[4]}}</v-col>
              <v-col cols="auto" class="mt-6"> Altura: {{store.pedidoAtual.dimensoes[4].height}} cm </v-col>
              <v-col cols="auto" class="mt-6"> Largura: {{store.pedidoAtual.dimensoes[4].width}} cm </v-col>
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
              <h3> Endereço de entrega: </h3>
              {{store.address}}, {{store.postalCode}}

            </v-col>

            <v-col justify="center" class="mb-6 d-flex flex-column"> 
              <h3> Data: </h3>
              {{store.pedidoAtual.data}}

            </v-col>
        </v-row>
          </v-card>         
        </v-col>
    </v-row>

    <br>
    <br>
    <br>
    <br>

    </v-containter>

    <!-- NOT FOR PRINTING STUFF !!!!!!!!!!!!!!!!!-->

    <v-row style="position: absolute; bottom: 0px; right: 0px;" class="d-flex d-print-none"> 
        <v-img :src="myImage2" contain height="180" width="180"></v-img>
    </v-row>
    <v-row style="position: absolute; bottom: 20px; right: 20px;" class="d-flex flex-column d-print-none"> 
      <span style="font-size: 10px;">© 2022 Casa dos Reclamos, Todos os direitos reservados.</span>
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
        myImage2: require('@/assets/logologo1.png'),
        store,
        show: false,
        profile: new UserDto(),
        picked: null,
        dialog1: false,
        menu1: false,
        menu2: false,
        menu3: false,
        req1: false,
        req2: false,
        req3: false,
        req4: false,
        theme: (store.pedidoAtual.modelo == "OneFace" || store.pedidoAtual.modelo == "TwoFaces"),
        available: [
          "IN_PRODUCTION",
          "DONE",
          "CANCELLED"
        ]
      }
    },
async created(){
  console.log(store.pedidoAtual)
  console.log(store.address)
  console.log(store.postalCode)
  if(store.pedidoAtual.modelo == "OneFace"){
    this.req1 = true;
  } else if(store.pedidoAtual.modelo == "TwoFaces"){
    this.req2 = true;
  } else if(store.pedidoAtual.modelo == "RightShowcase"){
    this.req3 = true;
  } else if(store.pedidoAtual.modelo == "LeftShowcase"){ 
    this.req4 = true;
  }
  this.profile = await Backend.getProfile()
  if(this.profile.roles[0] == 'CDR'){
    this.menu2 = true
    this.show = true
  } else if(this.profile.roles[0] == 'COMMERCIAL' || this.profile.roles[0] == 'ADMIN'){ 
    this.menu1 = true
  } else if(this.profile.roles[0] == 'MANAGER'){
    this.menu3 = true
  }
  console.log(this.show)
},
methods: {
  print(){
    window.print();
  },
  getPedido() {
    if(store.pedidoAtual.modelo == "OneFace" || store.pedidoAtual.modelo == "TwoFaces"){
      this.$router.push({name: 'detailsOneOrTwo'});
    } else {
      this.$router.push({name: 'detailsMontra'});
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

.customGradient {
  background-image: linear-gradient(#616161, grey);
}
</style>