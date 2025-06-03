import { createApp, h } from "vue";
import { createPinia } from "pinia";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import "vuetify/styles";
import { VLigatureIcon } from "vuetify/lib/composables/icons.mjs";
import "material-symbols/outlined.scss";

import App from "@/App.vue";
import { router } from "@/router";

const app = createApp(App);

const materialSymbols = {
  component: (props) =>
    h(VLigatureIcon, {
      ...props,
      class: "material-symbols-outlined",
    }),
};

const aliases = {
  collapse: "keyboard_arrow_up",
  complete: "check",
  cancel: "cancel",
  close: "close",
  delete: "cancel",
  clear: "cancel",
  success: "check_circle",
  info: "info",
  warning: "priority_high",
  error: "warning",
  prev: "chevron_left",
  next: "chevron_right",
  checkboxOn: "check_box",
  checkboxOff: "check_box_outline_blank",
  checkboxIndeterminate: "indeterminate_check_box",
  delimiter: "fiber_manual_record",
  sortAsc: "arrow_upward",
  sortDesc: "arrow_downward",
  expand: "keyboard_arrow_down",
  menu: "menu",
  subgroup: "arrow_drop_down",
  dropdown: "arrow_drop_down",
  radioOn: "radio_button_checked",
  radioOff: "radio_button_unchecked",
  edit: "edit",
  ratingEmpty: "star_border",
  ratingFull: "star",
  ratingHalf: "star_half",
  loading: "cached",
  first: "first_page",
  last: "last_page",
  unfold: "unfold_more",
  file: "attach_file",
  plus: "add",
  minus: "remove",
  calendar: "event",
  treeviewCollapse: "arrow_drop_down",
  treeviewExpand: "arrow_right",
  eyeDropper: "colorize",
};

app.use(
  createVuetify({
    components,
    directives,
    icons: {
      defaultSet: "materialSymbols",
      aliases,
      sets: {
        materialSymbols,
      },
    },
  }),
);
app.use(createPinia());
app.use(router);

app.mount("#app");

import Menu from "@components/Menu.vue";
app.component("Menu", Menu);
import Container from "@components/Container.vue";
app.component("Container", Container);
import Message from "@components/Message.vue";
app.component("Message", Message);
import UploadClients from "@components/UploadClients.vue";
app.component("UploadClients", UploadClients);
import AddClient from "@components/AddClient.vue";
app.component("AddClient", AddClient);
import EditClient from "@components/EditClient.vue";
app.component("EditClient", EditClient);
import PlafondDetail from "@components/PlafondDetail.vue";
app.component("PlafondDetail", PlafondDetail);
