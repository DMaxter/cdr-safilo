<template>
  <Container>
    <v-row justify="center" align="center" class="d-flex flex-column mt-2">
      <v-data-table-virtual
        :headers="headers"
        :items="filteredRequests"
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
        <template
          v-for="(header, i) in headers"
          v-slot:[`header.${header.key}`]="{ column, getSortIcon, toggleSort }"
        >
          <div class="v-data-table-header__content">
            <span>{{ header.title }}</span>
            <TableFilter
              v-if="header.searchable"
              :type="header.type ? header.type : undefined"
              :items="header.items ? header.items : undefined"
              :itemTitle="header.itemTitle ? header.itemTitle : undefined"
              :itemValue="header.itemValue ? header.itemValue : undefined"
              v-model="searchFilter[header.filterKey]"
              @filter="updateFilterURL"
            />
            <v-icon class="v-data-table-header__sort-icon" :icon="getSortIcon(column)"></v-icon>
          </div>
        </template>
        <template v-slot:item.status="{ value }">
          <v-chip
            variant="elevated"
            :prepend-icon="getStatusIcon(value)"
            :color="getStatusColor(value)"
            >{{ value }}</v-chip
          >
        </template>
        <template v-slot:item.created="{ value }">
          <span>
            {{ date.format(value, "keyboardDateTime") }}
          </span>
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
      </v-data-table-virtual>
      <RequestSummary v-model="showing" :request="toSummarize" @updated="refreshRequests" />
      <CancelRequest v-model="cancelling" :request="toCancel" @cancelled="refreshRequests" />
    </v-row>
  </Container>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useDate } from "vuetify";

import { statusMap, statusItems, Status } from "@/maps";
import { store } from "@/store.js"; // FIXME: DELETE
import { useUserStore } from "@stores/user";
import RequestDto from "@models/RequestDto";
import Backend from "@/router/backend";

const user = useUserStore();
await user.init();

const canManipulate = user.isSafilo() || user.isCdr() || user.isAdmin();

const route = useRoute();
const router = useRouter();
const date = useDate();

const requests = reactive([]);
const commercials = reactive([]);
const clients = reactive([]);
const ids = reactive([]);

onMounted(async () => {
  await refreshRequests();
});

// Request summary
const toSummarize = ref(new RequestDto());
const showing = ref(false);

// Cancel request
const toCancel = ref(0);
const cancelling = ref(false);

const filteredRequests = computed(() =>
  requests.filter((r) => {
    let include = true;

    if (searchFilter["id"].length > 0 && !searchFilter["id"].includes(r.id)) {
      include = false;
    } else if (searchFilter["status"].length > 0 && !searchFilter["status"].includes(r.status)) {
      include = false;
    } else if (searchFilter["client"].length > 0 && !searchFilter["client"].includes(r.client.id)) {
      include = false;
    } else if (
      searchFilter["commercial"].length > 0 &&
      !searchFilter["commercial"].includes(r.user)
    ) {
      include = false;
    } else if (
      searchFilter["creationDate"].length > 0 &&
      !date.isWithinRange(r.created, [
        date.setMinutes(date.setHours(searchFilter["creationDate"][0], 0), 0),
        date.setMinutes(
          date.setHours(searchFilter["creationDate"][searchFilter["creationDate"].length - 1], 23),
          59,
        ),
      ])
    ) {
      include = false;
    }

    return include;
  }),
);

// Custom properties being injected:
// - searchable - whether or not the column values can be searched
// - type - for searchable columns, the type of search to perform (only date supported - leave empty for other)
// - items - for searchable columns, if there is a subset to choose from
// - itemTitle - if items is set, what should be displayed to the user to select
// - itemValue - if items is set, what should be the value returned from selection
// - filterKey - for searchable columns, the property name in searchFilter to store the filter selection
const headers = [
  {
    title: "ID",
    align: "center",
    key: "id",
    value: "id",
    sortable: true,
    searchable: true,
    items: ids,
    filterKey: "id",
    order: "desc",
  },
  {
    title: "Estado",
    align: "center",
    key: "status",
    value: (value) => statusMap[value.status],
    searchable: true,
    sortable: true,
    items: statusItems,
    itemTitle: "name",
    itemValue: "value",
    filterKey: "status",
  },
  {
    title: "Cliente",
    align: "center",
    value: "client.name",
    key: "client",
    searchable: true,
    sortable: true,
    items: clients,
    itemTitle: "name",
    itemValue: "id",
    filterKey: "client",
  },
  {
    title: "Comercial",
    align: "center",
    key: "user",
    value: "user",
    searchable: true,
    sortable: true,
    items: commercials,
    filterKey: "commercial",
  },
  {
    title: "Data de Criação",
    align: "center",
    key: "created",
    value: "created",
    searchable: true,
    type: "date",
    sortable: true,
    filterKey: "creationDate",
  },
  {
    title: "Custo",
    align: "center",
    key: "cost",
    value: (value) => value.cost.toFixed(2),
    searchable: false,
    sortable: true,
  },
  {
    value: "actions",
    sortable: false,
    searchable: false,
  },
];

// Search filters
const searchFilter = reactive({
  id: [],
  status: [],
  client: [],
  commercial: [],
  creationDate: [],
});

// Parse route path
if (route.query.id) {
  searchFilter["id"] = [route.query.id].flat().map((id) => {
    try {
      return Number(id);
    } catch (e) {
      console.error("Invalid request id");
    }
  });
}
if (route.query.status) {
  searchFilter["status"] = [route.query.status].flat();
}
if (route.query.client) {
  searchFilter["client"] = [route.query.client].flat().map((id) => {
    try {
      return Number(id);
    } catch (e) {
      console.error("Invalid client id");
    }
  });
}
if (route.query.commercial) {
  searchFilter["commercial"] = [route.query.commercial].flat();
}
if (route.query.creationDate) {
  searchFilter["creationDate"] = [route.query.creationDate].flat().map((date) => {
    try {
      return Date.parse(date);
    } catch (e) {
      console.error("Invalid creation date");
    }
  });
}

async function refreshRequests() {
  requests.length = 0;
  commercials.length = 0;
  clients.length = 0;
  ids.length = 0;

  let retrieved = await Backend.getRequests();

  requests.unshift(...retrieved);

  const clientIds = new Set();

  clients.unshift(
    ...requests
      .filter((r) => {
        const seen = clientIds.has(r.client.id);
        clientIds.add(r.client.id);

        return !seen;
      })
      .map((r) => ({
        id: r.client.id,
        name: `${r.client.id} - ${r.client.name} - ${r.client.city}`,
      })),
  );

  commercials.unshift(...new Set(requests.map((r) => r.user)));
  ids.unshift(...retrieved.map((r) => r.id));
}

function showSummary(item) {
  toSummarize.value = item;
  showing.value = true;
}

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

function updateFilterURL() {
  let query = {};

  if (searchFilter["id"]) {
    query["id"] = searchFilter["id"];
  }
  if (searchFilter["status"]) {
    query["status"] = searchFilter["status"];
  }
  if (searchFilter["client"]) {
    query["client"] = searchFilter["client"];
  }
  if (searchFilter["commercial"]) {
    query["commercial"] = searchFilter["commercial"];
  }
  if (searchFilter["creationDate"]) {
    query["creationDate"] = searchFilter["creationDate"];
  }

  router.push({ query: query });
}

/***/
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
/***/
</script>

<style lang="scss" scoped></style>
