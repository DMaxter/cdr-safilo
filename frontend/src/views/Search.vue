<template>
  <Container>
    <P-DataTable
      :value="filteredRequests"
      dataKey="id"
      paginator
      scrollable
      removableSort
      scrollHeight="500px"
      :rows="50"
      :rowsPerPageOptions="[10, 20, 50, 100]"
      style="max-width: 98%"
      class="mt-5"
      filterDisplay="row"
      paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
      currentPageReportTemplate="{first} to {last} of {totalRecords}"
    >
      <template #empty>Não existem pedidos registados</template>

      <P-Column sortable field="id" header="ID">
        <template #filter>
          <P-InputText v-model="filterModel.value" @input="filterCallback()" placeholder="ID" />
        </template>
      </P-Column>
      <P-Column field="status" header="Estado" sortable>
        <template #filter="{ filterModel, filterCallback }">
          <P-MultiSelect filter v-model="filterModel.value" @input="filterCallback()" :options="states" placeholder="Estado" />
        </template>
        <template #body="{ data }">
          <P-Chip
            :icon="getStatusIcon(data.status)"
            :severity="getStatusColor(data.status)"
            :value="data.status"
          ></P-Chip>
        </template>
      </P-Column>
      <P-Column field="client.name" header="Cliente" sortable>
        <template #filter>
          <TableFilter
            :items="clients"
            itemTitle="name"
            itemValue="id"
            v-model="searchFilter['client']"
            @filter="updateFilterURL"
          />
        </template>
      </P-Column>
      <P-Column field="user" header="Comercial" sortable>
        <template #filter>
          <TableFilter
            :items="commercials"
            v-model="searchFilter['commercial']"
            @filter="updateFilterURL"
          />
        </template>
      </P-Column>
      <P-Column field="created" header="Data de Criação" sortable>
        <template #filter>
          <TableFilter
            type="date"
            v-model="searchFilter['creationDate']"
            @filter="updateFilterURL"
          />
        </template>
        <template #body="{ data }">
          <span>
            {{ new Date(data.created).toLocaleString() }}
          </span>
        </template>
      </P-Column>
      <P-Column field="cost" header="Custo" sortable>
        <template #body="{ data }">
          {{ data.cost.toFixed(2) }}
        </template>
      </P-Column>
      <P-Column>
        <template #body="{ data }">
          <Icon icon="visibility" @click="showSummary(data)" v-tooltip="'Ver resumo'" />
          <Icon icon="open_in_new" @click="console.error('TODO')" v-tooltip="'Ver detalhes'" />
          <!-- TODO: Implement edit -->
          <Icon
            v-if="
              (canManipulate || (authStore.isCommercial() && data.user == authStore.logged?.name)) &&
              data.status!! === Status.Ordered
            "
            icon="edit"
            @click="editRequest(data)"
            v-tooltip="'Editar'"
          />
          <Icon
            v-if="
              (canManipulate || (authStore.isCommercial() && data.user == authStore.logged?.name)) &&
              data.status!! === Status.Ordered
            "
            icon="cancel"
            @click="showCancel(data)"
            v-tooltip="'Cancelar'"
          />
        </template>
      </P-Column>
    </P-DataTable>
    <RequestSummary v-model="showing" :request="toSummarize" @updated="refreshRequests" />
    <CancelRequest v-model="cancelling" :request="toCancel" @cancelled="refreshRequests" />
  </Container>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter, type LocationQueryRaw } from "vue-router";

import { statusItems } from "@/maps";
import { useAuthStore } from "@stores/auth";

const authStore = useAuthStore();

const canManipulate = authStore.isSafilo() || authStore.isCdr() || authStore.isAdmin();

const route = useRoute();
const router = useRouter();

const requests: RequestDto[] = reactive([]);
const commercials: String[] = reactive([]);
const clients: SimpleClient[] = reactive([]);
const ids: Number[] = reactive([]);

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
      !searchFilter["commercial"].includes(r.user!!)
    ) {
      include = false;
    } else if (searchFilter["creationDate"].length > 0) {
      const createdDate = new Date(r.created);
      const startDate = new Date(searchFilter["creationDate"][0]);
      startDate.setHours(0, 0, 0, 0);

      const endDate = new Date(
        searchFilter["creationDate"][searchFilter["creationDate"].length - 1],
      );
      endDate.setHours(23, 59, 59, 999);

      if (createdDate < startDate || createdDate > endDate) {
        include = false;
      }
    }

    return include;
  }),
);

// Search filters
const searchFilter: SearchViewFilter = reactive({
  id: [],
  status: [],
  client: [],
  commercial: [],
  creationDate: [],
});

// Parse route path
if (route.query.id) {
  searchFilter["id"] = [route.query.id!!].flat().map((id) => {
    try {
      return Number(id);
    } catch (e) {
      console.error("Invalid request id");
    }
  });
}
if (route.query.status) {
  searchFilter["status"] = [route.query.status!!].flat();
}
if (route.query.client) {
  searchFilter["client"] = [route.query.client!!].flat().map((id) => {
    try {
      return Number(id);
    } catch (e) {
      console.error("Invalid client id");
    }
  });
}
if (route.query.commercial) {
  searchFilter["commercial"] = [route.query.commercial!!].flat();
}
if (route.query.creationDate) {
  searchFilter["creationDate"] = [route.query.creationDate!!].flat().map((date: string) => {
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

  let retrieved: RequestDto[] = await Backend.getRequests();

  requests.unshift(...retrieved);

  const clientIds: Set<Number> = new Set();

  clients.unshift(
    ...requests
      .filter((r) => {
        const seen = clientIds.has(r.client!!.id!!);
        clientIds.add(r.client!!.id!!);

        return !seen;
      })
      .map(
        (r) =>
          new SimpleClient({
            id: r.client!!.id!!,
            name: `${r.client!!.id} - ${r.client!!.name} - ${r.client!!.city}`,
          }),
      ),
  );

  commercials.unshift(...new Set(requests.map((r) => r.user!!)));
  ids.unshift(...retrieved.map((r) => r.id!!));
}

function showSummary(item: RequestDto) {
  toSummarize.value = item;
  showing.value = true;
}

function showCancel(item: RequestDto) {
  cancelling.value = true;
  toCancel.value = item.id!!;
}

// Visual stuff
function getStatusIcon(value: Status) {
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

function getStatusColor(value: Status) {
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

// FIXME: Update
function updateFilterURL() {
  let query: LocationQueryRaw = {};

  if (searchFilter["id"]) {
    query["id"] = searchFilter["id"];
  }
  if (searchFilter["status"]) {
    query["status"] = searchFilter["status"].map((s) => s.toString());
  }
  if (searchFilter["client"]) {
    query["client"] = searchFilter["client"];
  }
  if (searchFilter["commercial"]) {
    query["commercial"] = searchFilter["commercial"];
  }
  if (searchFilter["creationDate"]) {
    query["creationDate"] = searchFilter["creationDate"].map((d) => d.toString());
  }

  router.push({ query: query });
}
// END FIXME:

/***/
// TODO: Implement
function editRequest(item: RequestDto) {}
//function editRequest(item) {
//  store.currentRequest = item;
//  store.isEditing = true;
//  if (item.type.type == "OneFace") {
//    store.isActive1 = true;
//    $router.push({ name: "order2" });
//  } else if (item.type.type == "TwoFaces") {
//    store.isActive1 = true;
//    $router.push({ name: "order2" });
//  } else if (item.type.type == "SimpleShowcase") {
//    store.isActive4 = true;
//    $router.push({ name: "ABC" });
//  } else if (item.type.type == "RightShowcase") {
//    store.isActive2 = true;
//    $router.push({ name: "ABC" });
//  } else if (item.type.type == "LeftShowcase") {
//    store.isActive3 = true;
//    $router.push({ name: "ABC" });
//  }
//}
//
//function openRequestNewTab(item) {
//  let route = $router.resolve({ name: "details", query: { id: item.id } });
//  window.open(route.href, "_blank");
//}
/***/
</script>

<style lang="scss" scoped></style>
