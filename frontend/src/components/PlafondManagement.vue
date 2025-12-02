<template>
  <P-Dialog v-model:visible="enabled" modal header="Plafonds" class="w-[500px]">
    <P-DataTable
      :value="desserts2"
      scrollHeight="260px"
      v-model:filters="filters"
      :globalFilterFields="['name', 'fat']"
      class="w-[600px]"
    >
      <template #header>
        <div class="flex justify-start">
          <P-IconField>
            <P-InputIcon>
              <i class="pi pi-search" />
            </P-InputIcon>
            <P-InputText v-model="filters['global'].value" placeholder="Procurar" />
          </P-IconField>
        </div>
      </template>
      <P-Column field="name" header="Nome" sortable />
      <P-Column field="fat" header="E-mail" />
      <P-Column field="yo" header="Plafond" />
      <P-Column>
        <template #body="{ data }">
          <P-Button
            icon="pi pi-plus"
            text
            rounded
            @click="
              getSpecificPlafond(data);
              dialog4 = true;
            "
          />
        </template>
      </P-Column>
    </P-DataTable>
    <template #footer>
      <P-Button label="Voltar" text @click="dialog3 = false" />
      <P-Button label="Alterar Plafonds" text @click="$router.push('plafondChange')" />
    </template>
  </P-Dialog>
</template>

<script lang="ts" setup>
const enabled = defineModel<boolean>();

function getSpecificPlafond(item) {
  const client = allClients.value.find((element) => item.name === element.name);
  if (client) {
    profile.value = client.name!;
    const formattedCredits: PlafondDetail[] = [];
    client.credits?.forEach((element) => {
      formattedCredits.push({
        ...element,
        amount: parseFloat(element.amount.toFixed(2)),
      });
    });
    credits.value = formattedCredits;
  }
}
</script>
