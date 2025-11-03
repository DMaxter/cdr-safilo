import { fileURLToPath, URL } from "node:url"

import tailwindcss from "@tailwindcss/vite"
import { defineConfig } from "vite"
import vue from "@vitejs/plugin-vue"
import vueDevTools from "vite-plugin-vue-devtools"

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    tailwindcss(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
      "@components": fileURLToPath(new URL("./src/components", import.meta.url)),
      "@models": fileURLToPath(new URL("./src/models", import.meta.url)),
      "@router": fileURLToPath(new URL("./src/router", import.meta.url)),
      "@stores": fileURLToPath(new URL("./src/stores", import.meta.url)),
      "@styles": fileURLToPath(new URL("./src/styles", import.meta.url)),
      "@views": fileURLToPath(new URL("./src/views", import.meta.url))
    },
  },
  server: {
    host: "0.0.0.0",
    port: 8081,
  }
})
