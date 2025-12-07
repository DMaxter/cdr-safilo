<template>
  <div id="app-wrapper">
    <Suspense>
      <RouterView />
    </Suspense>
    <span style="position: absolute; bottom: 20px; right: 20px; font-size: 12px; color: white"
      >© 2022-2025 Casa dos Reclamos, Todos os direitos reservados.</span
    >
  </div>
  <P-Toast />
</template>

<script setup lang="ts">
import { RouterView } from "vue-router";
import "@styles/tailwind.css";
import "@styles/global.scss";
</script>
