import { createApp } from "vue";
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
import Accordion from "primevue/accordion";
import AccordionContent from "primevue/accordioncontent";
import AccordionHeader from "primevue/accordionheader";
import AccordionPanel from "primevue/accordionpanel";
import Button from "primevue/button";
import Card from "primevue/card";
import Chip from "primevue/chip";
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
import Message from "primevue/message";
import MultiSelect from "primevue/multiselect";
import Password from "primevue/password";
import Select from "primevue/select";
import ToggleSwitch from "primevue/toggleswitch";
import Tag from "primevue/tag";
import Textarea from "primevue/textarea";
import Toast from "primevue/toast";
import Stepper from "primevue/stepper";
import StepList from "primevue/steplist";
import StepPanels from "primevue/steppanels";
import StepPanel from "primevue/steppanel";
import Step from "primevue/step";

app.component("PAccordion", Accordion);
app.component("PAccordionContent", AccordionContent);
app.component("PAccordionHeader", AccordionHeader);
app.component("PAccordionPanel", AccordionPanel);
app.component("PButton", Button);
app.component("PCard", Card);
app.component("PChip", Chip);
app.component("PColumn", Column);
app.component("PConfirmDialog", ConfirmDialog);
app.component("PDataTable", DataTable);
app.component("PDatePicker", DatePicker);
app.component("PDialog", Dialog);
app.component("PFileUpload", PFileUpload);
app.component("PFloatLabel", FloatLabel);
app.component("PForm", Form);
app.component("PFormField", FormField);
app.component("PIconField", IconField);
app.component("PInputIcon", InputIcon);
app.component("PInputNumber", InputNumber);
app.component("PInputText", InputText);
app.component("PListBox", ListBox);
app.component("PMenubar", Menubar);
app.component("PMessage", Message);
app.component("PMultiSelect", MultiSelect);
app.component("PPassword", Password);
app.component("PSelect", Select);
app.component("PToggleSwitch", ToggleSwitch);
app.component("PTag", Tag);
app.component("PTextarea", Textarea);
app.component("PToast", Toast);
app.component("PStepper", Stepper);
app.component("PStepList", StepList);
app.component("PStepPanels", StepPanels);
app.component("PStepPanel", StepPanel);
app.component("PStep", Step);

// Custom Components
import BrandList from "@components/BrandList.vue";
import BrandManagement from "@components/BrandManagement.vue";
import Circle from "@components/Circle.vue";
import ClientNote from "@components/ClientNote.vue";
import ClientPicker from "@components/orders/ClientPicker.vue";
import Container from "@components/Container.vue";
import ClientManagement from "@components/ClientManagement.vue";
import FileUpload from "@components/FileUpload.vue";
import FinishingGroupList from "@components/FinishingGroupList.vue";
import FinishingGroupManagement from "@components/FinishingGroupManagement.vue";
import FinishingList from "@components/FinishingList.vue";
import FinishingManagement from "@components/FinishingManagement.vue";
import Icon from "@components/Icon.vue";
import ImageManagement from "@components/ImageManagement.vue";
import ImagePickerDialog from "@components/orders/ImagePickerDialog.vue";
import ItemGroup from "@components/ItemGroup.vue";
import MaterialList from "@components/MaterialList.vue";
import MaterialManagement from "@components/MaterialManagement.vue";
import Menu from "@components/Menu.vue";
import PasswordChange from "@components/PasswordChange.vue";
import PlafondDetail from "@components/PlafondDetail.vue";
import PlafondList from "@components/PlafondList.vue";
import PlafondManagement from "@components/PlafondManagement.vue";
import PriceList from "@components/PriceList.vue";
import PriceManagement from "@components/PriceManagement.vue";
import PrintRequest from "@components/PrintRequest.vue";
import PrintSlot from "@components/PrintSlot.vue";
import RecoveryCode from "@components/RecoveryCode.vue";
import RequestActions from "@components/RequestActions.vue";
import RequestDetails from "@components/RequestDetails.vue";
import RequestSummary from "@components/RequestSummary.vue";
import SlotForm from "@components/orders/SlotForm.vue";
import UserManagement from "@components/UserManagement.vue";
import Waybill from "@components/Waybill.vue";

app.component("BrandList", BrandList);
app.component("BrandManagement", BrandManagement);
app.component("Circle", Circle);
app.component("ClientManagement", ClientManagement);
app.component("ClientNote", ClientNote);
app.component("ClientPicker", ClientPicker);
app.component("Container", Container);
app.component("FileUpload", FileUpload);
app.component("FinishingGroupList", FinishingGroupList);
app.component("FinishingGroupManagement", FinishingGroupManagement);
app.component("FinishingList", FinishingList);
app.component("FinishingManagement", FinishingManagement);
app.component("Icon", Icon);
app.component("ImageManagement", ImageManagement);
app.component("ImagePickerDialog", ImagePickerDialog);
app.component("ItemGroup", ItemGroup);
app.component("MaterialList", MaterialList);
app.component("MaterialManagement", MaterialManagement);
app.component("Menu", Menu);
app.component("PasswordChange", PasswordChange);
app.component("PlafondDetail", PlafondDetail);
app.component("PlafondList", PlafondList);
app.component("PlafondManagement", PlafondManagement);
app.component("PriceList", PriceList);
app.component("PriceManagement", PriceManagement);
app.component("PrintRequest", PrintRequest);
app.component("PrintSlot", PrintSlot);
app.component("RecoveryCode", RecoveryCode);
app.component("RequestActions", RequestActions);
app.component("RequestDetails", RequestDetails);
app.component("RequestSummary", RequestSummary);
app.component("SlotForm", SlotForm);
app.component("UserManagement", UserManagement);
app.component("Waybill", Waybill);

app.mount("#app");
