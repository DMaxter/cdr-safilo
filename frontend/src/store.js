import { reactive } from 'vue'

export const store = reactive({
  isActive1: false,
  isActive2: false,
  isActive3: false,
  facesDefault:[
    {
      face: "A",
      src: require('@/assets/A.png'),
    },
          {
      face: "B",
      src: require('@/assets/B.png'),
    },
          {
      face: "C",
      src: require('@/assets/C.png'),
    },
          {
      face: "D",
      src: require('@/assets/D.png'),
    },
          {
      face: "E",
      src: require('@/assets/E.png'),
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
  
})
