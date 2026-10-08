<template>
  <P-Dialog v-model:visible="enabled" modal class="w-[800px]">
    <template #header>Marcas</template>
    <div class="h-85/100">
      <P-DataTable
        v-model:filters="filters"
        paginator
        scrollable
        removable-sort
        class="max-w-98/100"
        scroll-height="flex"
        filter-display="row"
        :value="brandStore.brands"
        :rows="10"
        :rows-per-page-options="[5, 10, 20, 50, 100]"
      >
        <template #empty>Não existem marcas registadas</template>

        <P-Column sortable field="id" header="ID">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" placeholder="ID" @input="filterCallback()" />
          </template>
        </P-Column>
        <P-Column sortable field="name" header="Nome">
          <template #filter="{ filterModel, filterCallback }">
            <P-InputText v-model="filterModel.value" placeholder="Nome" @input="filterCallback()" />
          </template>
        </P-Column>
        <P-Column>
          <template #body="{ data }">
            <Icon v-tooltip="'Editar imagens'" icon="image" @click="openImageManagement(data)" />
            <Icon
              v-tooltip="'Editar marca'"
              icon="edit"
              @click="openBrandManagement(data, ManageMode.Edit)"
            />
            <Icon v-tooltip="'Eliminar marca'" icon="delete" @click="confirmDeletion(data)" />
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
    multiple
    :add-action="addImage"
    :obsolete-action="obsoleteImage"
    :delete-action="confirmImageDeletion"
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
      let code = link.replace("file/d/", "thumbnail?id=");
      code = code.replace("/view?usp=sharing", "");
      code = code.replace("/view?usp=drive_link", "");
      final_link = code + "&sz=w1080";
    } else {
      final_link = link;
    }

    const response = await brandStore.addBrandImage(selectedBrand.value.id, final_link);
    if (!response.success) {
      throw Error(response.content as string);
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

async function obsoleteImage(images: Image[]) {
  const imageIds = images.map((i) => i.id).filter((id) => typeof id === "number" && id > 0);

  if (imageIds.length === 0) {
    return;
  }

  const plural = imageIds.length > 1;
  const errors: string[] = [];

  for (const imageId of imageIds) {
    const response = await brandStore.makeImageObsolete(imageId);
    if (!response.success) {
      errors.push(response.content ?? "Erro desconhecido");
    }
  }

  if (errors.length === 0) {
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${plural ? "Imagens marcadas como obsoletas" : "Imagem marcada como obsoleta"} com sucesso`,
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao marcar ${plural ? "as imagens" : "a imagem"} como ${plural ? "obsoletas" : "obsoleta"}`,
      life: 10000,
    });
    console.error(errors);
  }
}

async function deleteImages(imageIds: number[]) {
  const errors: string[] = [];

  for (const imageId of imageIds) {
    const response = await brandStore.deleteBrandImage(imageId);
    if (!response.success) {
      errors.push(response.content ?? "Erro desconhecido");
    }
  }

  if (errors.length === 0) {
    toast.add({
      severity: "success",
      summary: IMAGE_TITLE,
      detail: `${imageIds.length > 1 ? "Imagens apagadas" : "Imagem apagada"} com sucesso`,
      life: 10000,
    });
  } else {
    toast.add({
      severity: "error",
      summary: IMAGE_TITLE,
      detail: `Ocorreu um erro ao apagar ${imageIds.length > 1 ? "as imagens" : "a imagem"}`,
      life: 10000,
    });
    console.error(errors);
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

function confirmImageDeletion(images: Image[]) {
  const imageIds = images.map((i) => i.id).filter((id) => typeof id === "number" && id > 0);

  if (imageIds.length === 0) {
    return;
  }

  const plural = imageIds.length > 1;

  confirm.require({
    message: `Tem a certeza que pretende apagar ${plural ? "as imagens" : "a imagem"} da marca '${selectedBrand.value.name}'?`,
    header: "Confirmar remoção de imagem",
    rejectProps: {
      label: "Cancelar",
      severity: "secondary",
      outline: true,
    },
    acceptProps: {
      label: "Apagar",
      severity: "danger",
    },
    accept: () => deleteImages(imageIds),
  });
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
