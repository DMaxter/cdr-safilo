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

// Vuetify
const materialSymbols = {
  component: (props) =>
    h(VLigatureIcon, {
      ...props,
      class: "material-symbols-outlined",
    }),
};

const aliases = {
  calendar: "event",
  cancel: "cancel",
  checkboxIndeterminate: "indeterminate_check_box",
  checkboxOff: "check_box_outline_blank",
  checkboxOn: "check_box",
  clear: "cancel",
  close: "close",
  collapse: "keyboard_arrow_up",
  complete: "check",
  delete: "delete",
  delimiter: "fiber_manual_record",
  download: "download",
  dropdown: "arrow_drop_down",
  edit: "edit",
  error: "warning",
  expand: "keyboard_arrow_down",
  eyeDropper: "colorize",
  file: "attach_file",
  first: "first_page",
  info: "info",
  last: "last_page",
  loading: "cached",
  menu: "menu",
  minus: "remove",
  next: "chevron_right",
  open: "open_in_new",
  plus: "add",
  prev: "chevron_left",
  radioOff: "radio_button_unchecked",
  radioOn: "radio_button_checked",
  ratingEmpty: "star_border",
  ratingFull: "star",
  ratingHalf: "star_half",
  sortAsc: "arrow_upward",
  sortDesc: "arrow_downward",
  subgroup: "arrow_drop_down",
  success: "check_circle",
  treeviewCollapse: "arrow_drop_down",
  treeviewExpand: "arrow_right",
  upload: "upload",
  unfold: "unfold_more",
  view: "visibility",
  warning: "priority_high",
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

// Pinia
app.use(createPinia());

// Router
app.use(router);

app.mount("#app");

// Components
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
import PasswordChange from "@components/PasswordChange.vue";
app.component("PasswordChange", PasswordChange);
import RequestSummary from "@components/RequestSummary.vue";
app.component("RequestSummary", RequestSummary);
import CancelRequest from "@components/CancelRequest.vue";
app.component("CancelRequest", CancelRequest);
import PrintRequest from "@components/PrintRequest.vue";
app.component("PrintRequest", PrintRequest);
import PrintSlot from "@components/PrintSlot.vue";
app.component("PrintSlot", PrintSlot);
import Waybill from "@components/Waybill.vue";
app.component("Waybill", Waybill);
import OpenWaybill from "@components/OpenWaybill.vue";
app.component("OpenWaybill", OpenWaybill);
import DownloadWaybill from "@components/DownloadWaybill.vue";
app.component("DownloadWaybill", DownloadWaybill);
import RecoveryCode from "@components/RecoveryCode.vue";
app.component("RecoveryCode", RecoveryCode);
