import { reactive } from 'vue'

export const store = reactive({
  isActive1: false,
  isActive2: false,
  isActive3: false,
  facesDefault:[
    {
      face: "A",
      link: require('@/assets/A.png'),
    },
          {
      face: "B",
      link: require('@/assets/B.png'),
    },
          {
      face: "C",
      link: require('@/assets/C.png'),
    },
          {
      face: "D",
      link: require('@/assets/D.png'),
    },
          {
      face: "E",
      link: require('@/assets/E.png'),
    },
  ],
  face1: null,
  face2: null,
  pedidoAtual: {
      cod: null,
      data: null,
      marca: null,
      modelo: null,
      material: null,
      dimensoes: null,
      estado: null,
      images: null,
      quantity: null,
      observations: null,
      cost: null,
      application: null
    },
  currentClient: null,
  currentAddress: null,
  currentCost: null,
  currentBrand: null,
  uniqueBrands: null,
  currentBrandId: [],
  observations: null,
  costPerBrand: null,
  dimensions: [],
  application: null,
  selectedMaterial: [],
  images: [],
  quantity: null,
  currentRequest: null,
  clientAddress: null,
  codeSearch: null,
  estadoSearch: null,
  materialSearch: null,
})
