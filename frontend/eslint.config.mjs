import pluginVue from "eslint-plugin-vue";
import vueTsEslintConfig from "@vue/eslint-config-typescript";
import prettierConfig from "@vue/eslint-config-prettier";

export default [
  {
    ignores: ["dist/**", "node_modules/**"],
  },
  ...pluginVue.configs["flat/recommended"],
  ...vueTsEslintConfig({ supportedScriptLangs: { ts: true, js: true } }),
  {
    rules: {
      // Single-word names are intentional in this project: globally registered
      // UI primitives (Icon, Menu, Container, Circle) and route views
      // (Login, Profile, Configure, Search, Waybill) are used as-is.
      "vue/multi-word-component-names": "off",
      "vue/no-reserved-component-names": "off",
    },
  },
  {
    // Manage dialogs edit prop-passed draft objects in place; the parent owns
    // the state and changes propagate by reference on purpose.
    files: [
      "src/components/FinishingManagement.vue",
      "src/components/FinishingGroupManagement.vue",
      "src/components/PriceManagement.vue",
      "src/components/orders/SlotForm.vue",
    ],
    rules: {
      "vue/no-mutating-props": "off",
    },
  },
  prettierConfig,
];