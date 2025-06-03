import { reactive } from "vue";
import SlotA from "@/assets/A.png";
import SlotB from "@/assets/B.png";
import SlotC from "@/assets/C.png";
import SlotD from "@/assets/D.png";
import SlotE from "@/assets/E.png";

interface Face {
  face: string;
  link: string;
}

interface Pedido {
  cod: number | null;
  data: string | null;
  marca: string | null;
  modelo: string | null;
  material: string | null;
  dimensoes: string | null;
  estado: string | null;
  images: number[] | null;
  amount: number | null;
  observations: string | null;
  cost: number | null;
  application: string | null;
  finishings: string[] | null;
  codClient: number | null;
  nameClient: string | null;
}

interface StoreState {
  isActive1: boolean;
  isActive2: boolean;
  isActive3: boolean;
  isActive4: boolean;
  facesDefault: Face[];
  face1: Face | null;
  face2: Face | null;
  pedidoAtual: Pedido;
  currentClient: unknown | null;
  currentAddress: unknown | null;
  currentCost: number | null;
  currentBrand: unknown | null;
  uniqueBrands: unknown[] | null;
  currentBrandId: (number | string)[];
  observations: string | null;
  costPerBrand: Record<string, number> | null;
  dimensions: unknown[];
  application: string | null;
  selectedMaterial: string[];
  images: number[];
  amount: number | null;
  currentRequest: unknown | null;
  isEditing: boolean;
  clientAddress: unknown | null;
  codeSearch: string | null;
  estadoSearch: string | null;
  materialSearch: string | null;
  address: string | null;
  postalCode: string | null;
  finishes: string[];
  finishesAux: string[];
  pickado: unknown | null;
  pickado2: unknown | null;
  widths: number | number[] | null;
  heights: number | number[] | null;
  finitos: unknown | null;
  materioscas: unknown | null;
  backtracking: boolean;
  selectedItem: unknown | null;
  trackingCode: string | null;
}

export const store = reactive<StoreState>({
  isActive1: false,
  isActive2: false,
  isActive3: false,
  isActive4: false,
  facesDefault: [
    {
      face: "A",
      link: SlotA,
    },
    {
      face: "B",
      link: SlotB,
    },
    {
      face: "C",
      link: SlotC,
    },
    {
      face: "D",
      link: SlotD,
    },
    {
      face: "E",
      link: SlotE,
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
    amount: null,
    observations: null,
    cost: null,
    application: null,
    finishings: null,
    codClient: null,
    nameClient: null,
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
  images: [-1, -1, -1, -1, -1],
  amount: null,
  currentRequest: null,
  isEditing: false,
  clientAddress: null,
  codeSearch: null,
  estadoSearch: null,
  materialSearch: null,
  address: null,
  postalCode: null,
  finishes: [],
  finishesAux: [],
  pickado: null,
  pickado2: null,
  widths: null,
  heights: null,
  finitos: null,
  materioscas: null,
  backtracking: false,
  selectedItem: null,
  trackingCode: null,
});
