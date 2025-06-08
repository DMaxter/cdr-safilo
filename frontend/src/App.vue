<template>
  <v-app id="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
    <v-row
      id="footnote"
      style="position: absolute; bottom: 20px; right: 20px"
      class="d-flex flex-column"
    >
      <span style="font-size: 12px; color: white"
        >© 2022-2025 Casa dos Reclamos, Todos os direitos reservados.</span
      >
    </v-row>
  </v-app>
</template>

<script setup lang="ts">
import { RouterView } from "vue-router";
import "@/style.scss";
</script>
