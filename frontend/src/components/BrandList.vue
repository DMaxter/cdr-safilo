<template>
  <P-Dialog modal v-model:visible="enabled" class="w-[800px]">
    <template #header>Marcas</template>
    <div class="h-85/100">
      <P-DataTable
        paginator
        scrollable
        removableSort
        class="max-w-98/100"
        scrollHeight="flex"
        filterDisplay="row"
        :value="brandStore.brands"
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50, 100]"
        v-model:filters="filters"
      >
        <template #empty>Não existem marcas registadas</template>

        <P-Column sortable field="id" header="ID">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="ID" />
          </template>
        </P-Column>
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="Nome" />
          </template>
        </P-Column>
        <P-Column>
          <template #body="{ data }">
            <Icon icon="image" @click="openImageManagement(data)" v-tooltip="'Editar imagens'" />
            <Icon icon="edit" @click="openBrandManagement(data)" v-tooltip="'Editar marca'" />
            <!-- TODO: Implement -->
            <Icon icon="delete" @click="" v-tooltip="'Eliminar marca'" />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="">Adicionar</P-Button>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <ImageManagement v-model="manageImages" :addAction="addImage" :obsoleteAction="obsoleteImage" :images="selectedBrand.images" />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { Brand } from "@router/backend/services/brand/types";
import { Image } from "@router/backend/services/image/types";
import { useBrandStore } from "@stores/brands";

const TITLE = "Lista de Marcas";
const IMAGE_TITLE = "Imagens da Marca";

const enabled = defineModel<boolean>();
const brandStore = useBrandStore();
const toast = useToast();

const selectedBrand = ref<Brand>(new Brand());

const manageImages = ref(false);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS }
});

onMounted(async () => {
  await refresh();
});

// TODO: Support multiple images
async function addImage(link: string) {
  try {
    let final_link;
    if (link.includes("https://drive.google.com/file/d/") && (link.includes("/view?usp=sharing") || link.includes("/view?usp=drive_link"))) {
      var code = link.replace('file/d/', 'thumbnail?id=');
      code = code.replace('/view?usp=sharing', '');
      code = code.replace('/view?usp=drive_link', '');
      final_link = code + "&sz=w1080";
    } else {
      final_link = link;
    }

    const response = await brandStore.addBrandImage(selectedBrand.value.id, final_link);
    if (!response.success) {
      throw Error(response.content);
    }

    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: "Imagem adicionada com sucesso",
      life: 10000
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: "Ocorreu um erro ao adicionar a imagem",
      life: 10000
    });
  }
}

// TODO: Support multiple images
async function obsoleteImage(image: Image) {
  try {
    const response = await brandStore.makeImageObsolete(image.id);
    if (!response.success) {
      throw Error(response.content);
    }

    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: "Imagem marcada como obsoleta",
      life: 10000
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: "Ocorreu um erro ao marcar a imagem como obsoleta",
      life: 10000
    });
  }
}

async function refresh() {
  const response = await brandStore.getBrands();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de marcas",
      life: 10000
    });
  }
}

function openImageManagement(brand: Brand) {
  manageImages.value = true;
  selectedBrand.value = brand;
}

function close() {
  enabled.value = false;
} 
</script>
