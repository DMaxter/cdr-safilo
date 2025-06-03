<template>
  <v-container fluid class="d-flex flex-column align-center justify-center fill-height">
    <v-row justify="center" align="center" style="max-width: 100%">
      <v-col cols="auto" style="padding: 0">
        <v-card
          elevation="12"
          height="800"
          width="1400"
          max-width="100%"
          color="#FAFAFA"
          style="border-radius: 15px"
        >
          <Menu />
          <slot />
        </v-card>
      </v-col>
    </v-row>
    <v-row style="position: absolute; bottom: 0px; right: 0px" class="d-flex">
      <v-img :src="CDRLogo" contain height="180" width="180"></v-img>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import CDRLogo from "@/assets/logo-slogan-white.png";
</script>
