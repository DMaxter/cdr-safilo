<template>
  <Container>
    <v-row justify="center" align="center" class="d-flex flex-column mt-2">
      <!-- TODO: Add filters on each column -->
      <v-data-table
        :headers="headers"
        :items="requests"
        fixed-header
        hover
        item-key="id"
        items-per-page-text="Pedidos por página:"
        no-data-text="Não existem pedidos registados"
        height="500"
        items-per-page="50"
        style="max-width: 98%"
        class="elevation-1 my-header-style mt-5"
      >
        <template v-slot:item.created="{ value }">
          <span>
            {{
              value.toLocaleString("pt-PT", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
                hour: "2-digit",
                minute: "2-digit",
              })
            }}
            <v-tooltip activator="parent" interactive>{{
              value.toLocaleString("pt-PT")
            }}</v-tooltip>
          </span>
        </template>
        <template v-slot:item.status="{ value }">
          <v-chip
            variant="elevated"
            :prepend-icon="getStatusIcon(value)"
            :color="getStatusColor(value)"
            >{{ value }}</v-chip
          >
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon @click="showSummary(item)" v-tooltip="'Ver resumo'">$view</v-icon>
          <v-icon @click="openRequestNewTab(item)" v-tooltip="'Ver detalhes'">$open</v-icon>
          <!-- TODO: Implement edit -->
          <v-icon
            v-if="
              (canManipulate || (user.isCommercial() && item.user == user.user.name)) &&
              statusMap[item.status] == Status.Ordered
            "
            @click="editRequest(item)"
            v-tooltip="'Editar'"
            >$edit</v-icon
          >
          <v-icon
            v-if="
              (canManipulate || (user.isCommercial() && item.user == user.user.name)) &&
              statusMap[item.status] == Status.Ordered
            "
            @click="showCancel(item)"
            v-tooltip="'Cancelar'"
            >$cancel</v-icon
          >
        </template>
      </v-data-table>
      <RequestSummary v-model="showing" :request="toSummarize" @updated="refreshRequests" />
      <CancelRequest v-model="cancelling" :request="toCancel" @cancelled="refreshRequests" />
    </v-row>
  </Container>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";

import { statusMap, Status } from "@/maps";
import { store } from "@/store.js";
import { useUserStore } from "@stores/user";
import RequestDto from "@models/RequestDto";
import Backend from "@/router/backend";

const user = useUserStore();
await user.init();

const canManipulate = user.isSafilo() || user.isCdr() || user.isAdmin();

const requests = reactive([]);
const brands = reactive([]);

onMounted(async () => {
  await refreshRequests();
});

async function refreshRequests() {
  requests.length = 0;
  brands.length = 0;

  let retrieved = await Promise.all([Backend.getRequests(), Backend.getBrands()]);

  requests.unshift(...retrieved[0]);
  brands.unshift(...retrieved[1]);
}

// Request summary
const toSummarize = ref(new RequestDto());
const showing = ref(false);

function showSummary(item) {
  toSummarize.value = item;
  showing.value = true;
}

// Cancel request
const toCancel = ref(0);
const cancelling = ref(false);

function showCancel(item) {
  cancelling.value = true;
  toCancel.value = item.id;
}

// Visual stuff
function getStatusIcon(value) {
  if (value == Status.Cancelled) {
    return "cancel";
  } else if (value == Status.Ordered) {
    return "package_2";
  } else if (value == Status.Done) {
    return "check";
  } else {
    return "question_mark";
  }
}

function getStatusColor(value) {
  if (value == Status.Cancelled) {
    return "red";
  } else if (value == Status.Ordered) {
    return "yellow";
  } else if (value == Status.Done) {
    return "green";
  } else {
    return "grey";
  }
}

// TODO: Implement
function editRequest(item) {
  store.currentRequest = item;
  store.isEditing = true;
  if (item.type.type == "OneFace") {
    store.isActive1 = true;
    $router.push({ name: "order2" });
  } else if (item.type.type == "TwoFaces") {
    store.isActive1 = true;
    $router.push({ name: "order2" });
  } else if (item.type.type == "SimpleShowcase") {
    store.isActive4 = true;
    $router.push({ name: "ABC" });
  } else if (item.type.type == "RightShowcase") {
    store.isActive2 = true;
    $router.push({ name: "ABC" });
  } else if (item.type.type == "LeftShowcase") {
    store.isActive3 = true;
    $router.push({ name: "ABC" });
  }
}

function openRequestNewTab(item) {
  let route = $router.resolve({ name: "details", query: { id: item.id } });
  window.open(route.href, "_blank");
}

function nameFilter(value) {
  if (!dessertFilterValue) {
    return true;
  }
  console.log(dessertFilterValue);
  return value.toLowerCase().includes(dessertFilterValue.toLowerCase());
}

function nomeFilter(value) {
  if (!nomeFilterValue) {
    return true;
  }
  console.log(value);
  console.log(nomeFilterValue);
  return value.toLowerCase().includes(nomeFilterValue.toLowerCase());
}

function estadoFilter(value) {
  if (!estadosFilterValue) {
    return true;
  }
  return value.toLowerCase().includes(estadosFilterValue.toLowerCase());
}
function marcasFilter(value) {
  if (!marcasFilterValue) {
    return true;
  }
  return value.includes(String(marcasFilterValue));
}

function dateFilter(value) {
  if (dates.length != 2) {
    return true;
  }
  var gaga = value.split(" ");
  console.log(gaga);
  console.log(value);
  var converted = new Date(gaga).setHours(0, 0, 0, 0);
  var date1;
  var date2;
  if (new Date(dates[0]).setHours(0, 0, 0, 0) > new Date(dates[1]).setHours(0, 0, 0, 0)) {
    date1 = new Date(dates[1]).setHours(0, 0, 0, 0);
    date2 = new Date(dates[0]).setHours(0, 0, 0, 0);
  } else {
    date1 = new Date(dates[0]).setHours(0, 0, 0, 0);
    date2 = new Date(dates[1]).setHours(0, 0, 0, 0);
  }
  console.log(date1, date2, converted);
  return converted >= date1 && converted <= date2;
}
/***/

const headers = [
  {
    title: "ID",
    align: "center",
    value: "id",
    class: "my-header-style",
    key: "id",
    sortable: true,
    order: "desc",
  },
  {
    title: "Estado",
    align: "center",
    key: "status",
    value: (value) => statusMap[value.status],
    class: "my-header-style",
    //filter: estadoFilter,
    sortable: true,
  },
  {
    title: "Cliente",
    align: "center",
    value: "client.name",
    //filter: nameFilter,
    class: "my-header-style",
    sortable: true,
  },
  {
    title: "Comercial",
    align: "center",
    value: "user",
    //filter: nomeFilter,
    class: "my-header-style",
    sortable: true,
  },
  {
    title: "Data de Criação",
    align: "center",
    key: "created",
    value: "created",
    //filter: dateFilter,
    class: "my-header-style",
    sortable: true,
  },
  {
    title: "Custo",
    align: "center",
    key: "cost",
    value: (value) => value.cost.toFixed(2),
    class: "my-header-style",
    sortable: true,
  },
  {
    value: "actions",
    sortable: false,
    class: "my-header-style",
  },
];
</script>

<style lang="scss" scoped>
.my-header-style {
  border-radius: 0px;
  background-color: rgb(243, 243, 243) !important;
}
</style>
