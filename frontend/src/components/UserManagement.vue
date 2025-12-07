<template>
  <P-Dialog v-model:visible="dialog1" modal header="Registar Comercial" class="w-[500px]">
    <div v-if="added">Comercial registado com sucesso!</div>
    <div v-if="failed">Ocorreu um erro a registar o comercial</div>
    <div v-if="!added && !failed" class="flex flex-col items-center space-y-4">
      <P-InputText v-model="comercialEmail" placeholder="Email" class="w-4/5" />
      <P-InputText v-model="comercialName" placeholder="Nome" class="w-4/5" />
      <P-InputText v-model="password1" placeholder="Palavra Passe" type="password" class="w-4/5" />
      <P-InputText
        v-model="password2"
        placeholder="Confirmar Palavra Passe"
        type="password"
        class="w-4/5"
      />
    </div>
    <template #footer>
      <P-Button label="Voltar" text @click="" />
      <P-Button v-if="!added && !failed" label="Adicionar" text @click="addComercial()" />
    </template>
  </P-Dialog>
</template>

<script setup lang="ts"></script>
