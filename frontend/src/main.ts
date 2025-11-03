import { createApp, h } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";
import ToastService from 'primevue/toastservice';
import { definePreset } from '@primeuix/themes';
import Aura from "@primeuix/themes/aura";
import { pt } from "primelocale/js/pt.js";

import App from "@/App.vue";
import router from "@router";

const app = createApp(App);

app.use(ToastService);
app.use(createPinia());

const preset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{pink.50}',
      100: '{pink.100}',
      200: '{pink.200}',
      300: '{pink.300}',
      400: '{pink.400}',
      500: '{pink.500}',
      600: '{pink.600}',
      700: '{pink.700}',
      800: '{pink.800}',
      900: '{pink.900}',
      950: '{pink.950}'
    }
  }
});

app.use(PrimeVue, {
  locale: pt,
  theme: {
    preset: preset,
    options: {
      darkModeSelector: false,
    }
  }
});

app.use(router);

// PrimeVue Components
import Button from "primevue/button";
import Card from "primevue/card";
import Column from 'primevue/column';
import DataTable from "primevue/datatable";
import Dialog from "primevue/dialog";
import FloatLabel from "primevue/floatlabel";
import { Form, FormField } from "@primevue/forms";
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from "primevue/inputtext";
import Menubar from 'primevue/menubar';
import MultiSelect from "primevue/multiselect";
import Toast from "primevue/toast";

app.component("P-Button", Button);
app.component("P-Card", Card);
app.component("P-Column", Column);
app.component("P-DataTable", DataTable);
app.component("P-Dialog", Dialog);
app.component("P-FloatLabel", FloatLabel);
app.component("P-Form", Form);
app.component("P-FormField", FormField);
app.component("P-IconField", IconField);
app.component("P-InputIcon", InputIcon);
app.component("P-InputText", InputText);
app.component("P-Menubar", Menubar);
app.component("P-MultiSelect", MultiSelect);
app.component("P-Toast", Toast);


// Custom Components
// import AddClient from "@components/AddClient.vue";
// import CancelRequest from "@components/CancelRequest.vue";
import Circle from "@components/Circle.vue";
// import ClientNote from "@components/ClientNote.vue";
import Container from "@components/Container.vue";
// import DownloadWaybill from "@components/DownloadWaybill.vue";
// import EditClient from "@components/EditClient.vue";
// import FileUpload from "@components/FileUpload.vue";
import Icon from "@components/Icon.vue";
// import ImageManagement from "@components/ImageManagement.vue";
import Menu from "@components/Menu.vue";
// import Message from "@components/Message.vue";
// import OpenWaybill from "@components/OpenWaybill.vue";
import PasswordChange from "@components/PasswordChange.vue";
import PlafondDetail from "@components/PlafondDetail.vue";
// import PrintRequest from "@components/PrintRequest.vue";
// import PrintSlot from "@components/PrintSlot.vue";
import RecoveryCode from "@components/RecoveryCode.vue";
// import RequestSummary from "@components/RequestSummary.vue";
// import TableFilter from "@components/TableFilter.vue";
// import Waybill from "@components/Waybill.vue";
// 
// app.component("AddClient", AddClient);
// app.component("CancelRequest", CancelRequest);
app.component("Circle", Circle);
// app.component("ClientNote", ClientNote);
app.component("Container", Container);
// app.component("DownloadWaybill", DownloadWaybill);
// app.component("EditClient", EditClient);
// app.component("FileUpload", FileUpload);
app.component("Icon", Icon);
// app.component("ImageManagement", ImageManagement);
app.component("Menu", Menu);
// app.component("Message", Message);
// app.component("OpenWaybill", OpenWaybill);
app.component("PasswordChange", PasswordChange);
app.component("PlafondDetail", PlafondDetail);
// app.component("PrintRequest", PrintRequest);
// app.component("PrintSlot", PrintSlot);
app.component("RecoveryCode", RecoveryCode);
// app.component("RequestSummary", RequestSummary);
// app.component("TableFilter", TableFilter);
// app.component("Waybill", Waybill);

app.mount("#app");
