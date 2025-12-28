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
            <Icon
              icon="edit"
              @click="openBrandManagement(data, ManageMode.Edit)"
              v-tooltip="'Editar marca'"
            />
            <Icon icon="delete" @click="confirmDeletion(data)" v-tooltip="'Eliminar marca'" />
          </template>
        </P-Column>
      </P-DataTable>
    </div>
    <template #footer>
      <P-Button text @click="openBrandManagement(new Brand(), ManageMode.Add)">Adicionar</P-Button>
      <P-Button text @click="close">Voltar</P-Button>
    </template>
  </P-Dialog>
  <BrandManagement v-model="manageMode" :brand="selectedBrand" />
  <ImageManagement
    v-model="manageImages"
    :addAction="addImage"
    :obsoleteAction="obsoleteImage"
    :images="selectedBrand.images"
  />
</template>

<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core/api";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { onMounted, ref } from "vue";

import { Brand } from "@router/backend/services/brand/types";
import { Image } from "@router/backend/services/image/types";
import { useBrandStore } from "@stores/brands";
import { ManageMode } from "@/utils";

const TITLE = "Lista de Marcas";
const IMAGE_TITLE = "Imagens da Marca";

const enabled = defineModel<boolean>();
const brandStore = useBrandStore();
const confirm = useConfirm();
const toast = useToast();

const selectedBrand = ref<Brand>(new Brand());

const manageMode = ref<ManageMode>(ManageMode.None);
const manageImages = ref(false);

const filters = ref({
  id: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(async () => {
  await refresh();
});

// TODO: Support multiple images
async function addImage(link: string) {
  try {
    let final_link;
    if (
      link.includes("https://drive.google.com/file/d/") &&
      (link.includes("/view?usp=sharing") || link.includes("/view?usp=drive_link"))
    ) {
      var code = link.replace("file/d/", "thumbnail?id=");
      code = code.replace("/view?usp=sharing", "");
      code = code.replace("/view?usp=drive_link", "");
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
      life: 10000,
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: "Ocorreu um erro ao adicionar a imagem",
      life: 10000,
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
      life: 10000,
    });
  } catch (error) {
    console.error(error);
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: "Ocorreu um erro ao marcar a imagem como obsoleta",
      life: 10000,
    });
  }
}

async function deleteBrand() {
  const response = await brandStore.deleteBrand(selectedBrand.value.id);

  if (response.success) {
    toast.add({
      severity: "success",
      summary: TITLE,
      detail: "Marca eliminada com sucesso",
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Ocorreu um erro a remover a marca",
      life: 10000,
    });
    console.error(response);
  }
}

async function refresh() {
  const response = await brandStore.getBrands();

  if (!response.success) {
    toast.add({
      severity: "error",
      summary: TITLE,
      detail: "Não foi possível obter a lista de marcas",
      life: 10000,
    });
  }
}

function openBrandManagement(brand: Brand, mode: ManageMode) {
  selectedBrand.value = brand;
  manageMode.value = mode;
}

function openImageManagement(brand: Brand) {
  manageImages.value = true;
  selectedBrand.value = brand;
}

function confirmDeletion(brand: Brand) {
  selectedBrand.value = brand;

  confirm.require({
    message: `Tem a certeza que pretende eliminar a marca '${selectedBrand.value.name}'?`,
    header: "Confirmar remoção de marca",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Eliminar",
      severity: "danger",
    },
    accept: deleteBrand,
  });
}

function close() {
  enabled.value = false;
}
</script>
