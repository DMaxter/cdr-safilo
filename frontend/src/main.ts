import { createApp, h } from "vue";
import { createPinia } from "pinia";
import PrimeVue from "primevue/config";
import ConfirmationService from "primevue/confirmationservice";
import Tooltip from "primevue/tooltip";
import ToastService from "primevue/toastservice";
import { definePreset } from "@primeuix/themes";
import Aura from "@primeuix/themes/aura";
import { pt } from "primelocale/js/pt.js";

import App from "@/App.vue";
import router from "@router";

const app = createApp(App);

app.use(ToastService);
app.use(ConfirmationService);
app.use(createPinia());

const preset = definePreset(Aura, {
  semantic: {
    primary: {
      50: "{pink.50}",
      100: "{pink.100}",
      200: "{pink.200}",
      300: "{pink.300}",
      400: "{pink.400}",
      500: "{pink.500}",
      600: "{pink.600}",
      700: "{pink.700}",
      800: "{pink.800}",
      900: "{pink.900}",
      950: "{pink.950}",
    },
  },
});

app.use(PrimeVue, {
  locale: pt,
  theme: {
    preset: preset,
    options: {
      darkModeSelector: false,
    },
  },
});

app.use(router);
app.directive("tooltip", Tooltip);

// PrimeVue Components
import Button from "primevue/button";
import Card from "primevue/card";
import Column from "primevue/column";
import ConfirmDialog from "primevue/confirmdialog";
import DataTable from "primevue/datatable";
import DatePicker from "primevue/datepicker";
import Dialog from "primevue/dialog";
import PFileUpload from "primevue/fileupload";
import FloatLabel from "primevue/floatlabel";
import { Form, FormField } from "@primevue/forms";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import InputNumber from "primevue/inputnumber";
import InputText from "primevue/inputtext";
import ListBox from "primevue/listbox";
import Menubar from "primevue/menubar";
import MultiSelect from "primevue/multiselect";
import Select from "primevue/select";
import Tag from "primevue/tag";
import Textarea from "primevue/textarea";
import Toast from "primevue/toast";

app.component("P-Button", Button);
app.component("P-Card", Card);
app.component("P-Column", Column);
app.component("P-ConfirmDialog", ConfirmDialog);
app.component("P-DataTable", DataTable);
app.component("P-DatePicker", DatePicker);
app.component("P-Dialog", Dialog);
app.component("P-FileUpload", PFileUpload);
app.component("P-FloatLabel", FloatLabel);
app.component("P-Form", Form);
app.component("P-FormField", FormField);
app.component("P-IconField", IconField);
app.component("P-InputIcon", InputIcon);
app.component("P-InputNumber", InputNumber);
app.component("P-InputText", InputText);
app.component("P-ListBox", ListBox);
app.component("P-Menubar", Menubar);
app.component("P-MultiSelect", MultiSelect);
app.component("P-Select", Select);
app.component("P-Tag", Tag);
app.component("P-Textarea", Textarea);
app.component("P-Toast", Toast);

// Custom Components
import BrandList from "@components/BrandList.vue";
import BrandManagement from "@components/BrandManagement.vue";
// import CancelRequest from "@components/CancelRequest.vue";
import Circle from "@components/Circle.vue";
import ClientNote from "@components/ClientNote.vue";
import Container from "@components/Container.vue";
import ClientManagement from "@components/ClientManagement.vue";
// import DownloadWaybill from "@components/DownloadWaybill.vue";
import FileUpload from "@components/FileUpload.vue";
import Icon from "@components/Icon.vue";
import ImageManagement from "@components/ImageManagement.vue";
import ItemGroup from "@components/ItemGroup.vue";
import Menu from "@components/Menu.vue";
// import OpenWaybill from "@components/OpenWaybill.vue";
import PasswordChange from "@components/PasswordChange.vue";
import PlafondDetail from "@components/PlafondDetail.vue";
import PlafondList from "@components/PlafondList.vue";
import PlafondManagement from "@components/PlafondManagement.vue";
// import PrintRequest from "@components/PrintRequest.vue";
// import PrintSlot from "@components/PrintSlot.vue";
import RecoveryCode from "@components/RecoveryCode.vue";
// import RequestSummary from "@components/RequestSummary.vue";
// import Waybill from "@components/Waybill.vue";

app.component("BrandList", BrandList);
app.component("BrandManagement", BrandManagement);
// app.component("CancelRequest", CancelRequest);
app.component("Circle", Circle);
app.component("ClientManagement", ClientManagement);
app.component("ClientNote", ClientNote);
app.component("Container", Container);
// app.component("DownloadWaybill", DownloadWaybill);
app.component("FileUpload", FileUpload);
app.component("Icon", Icon);
app.component("ImageManagement", ImageManagement);
app.component("ItemGroup", ItemGroup);
app.component("Menu", Menu);
// app.component("OpenWaybill", OpenWaybill);
app.component("PasswordChange", PasswordChange);
app.component("PlafondDetail", PlafondDetail);
app.component("PlafondList", PlafondList);
app.component("PlafondManagement", PlafondManagement);
// app.component("PrintRequest", PrintRequest);
// app.component("PrintSlot", PrintSlot);
app.component("RecoveryCode", RecoveryCode);
// app.component("RequestSummary", RequestSummary);
// app.component("Waybill", Waybill);

app.mount("#app");
