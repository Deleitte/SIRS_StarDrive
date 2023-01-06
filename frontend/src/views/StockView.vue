<script setup lang="ts">
import {ref} from "vue";
import type {PartDto} from "@/models/PartDto";
import {buyPart, createPart, getParts} from "@/services/api";
import {NewPartDto} from "@/models/NewPartDto";

const dialogNewPart = ref(false);
const dialogBuyPart = ref(false);

const parts = ref<PartDto[]>([]);
const newPart = ref(new NewPartDto({}));

const quantity = ref(0);
const selectedPart = ref<PartDto>();

async function fetchParts() {
    try {
      parts.value = await getParts();
    } catch (e) {
      /* empty */
    }
}

fetchParts();

async function onSubmitNewPart() {
    try {
      await createPart(newPart.value);
      await fetchParts();
      newPart.value = new NewPartDto({});
      dialogNewPart.value = false;
    } catch (e) {
      /* empty */
    }
}

async function onSubmitBuyPart() {
  try {
    await buyPart(selectedPart.value!, quantity.value);
    await fetchParts();
    dialogBuyPart.value = false;
  } catch (e) {
    /* empty */
  }
}

</script>
<template>
  <v-card>
    <v-card-title>
      <v-container>
        <v-row>
          <v-col>
            <h2>Parts</h2>
          </v-col>
          <v-col class="d-flex justify-end">
            <div class="text-center">
              <v-dialog v-model="dialogBuyPart">
                <template v-slot:activator="{ props }">
                  <v-btn color="secondary" v-bind="props"> Buy Part </v-btn>
                </template>
                <v-form @submit.prevent="onSubmitBuyPart">
                  <v-card>
                    <v-card-text>
                      <v-select
                          v-model="selectedPart"
                          label="Part"
                          :items="parts"
                          item-title="ref"
                          item-value="ref"
                          return-object
                      ></v-select>
                      <v-text-field v-model="quantity" label="Quantity" required />
                    </v-card-text>
                    <div class="text-center">
                      <v-btn color="primary" type="submit"> Buy </v-btn>
                    </div>
                  </v-card>
                </v-form>
              </v-dialog>
            </div>
            <div class="text-center">
              <v-dialog v-model="dialogNewPart">
                <template v-slot:activator="{ props }">
                  <v-btn color="primary" v-bind="props"> New Part </v-btn>
                </template>
                <v-form @submit.prevent="onSubmitNewPart">
                  <v-card>
                    <v-card-text>
                      <v-text-field v-model="newPart.ref" label="Reference" required />
                      <v-text-field v-model="newPart.name" label="Name" required />
                      <v-text-field v-model="newPart.price" label="Price" required />
                    </v-card-text>
                    <div class="text-center">
                      <v-btn color="primary" type="submit"> Create </v-btn>
                    </div>
                  </v-card>
                </v-form>
              </v-dialog>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </v-card-title>

    <v-card-text>
      <v-table density="compact">
        <thead>
        <tr>
          <th class="text-left">Reference</th>
          <th class="text-center">Name</th>
          <th class="text-center">Price</th>
          <th class="text-center">Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in parts" :key="item.name">
          <td class="text-left">
            {{ item.ref }}
          </td>
          <td class="text-center">
            {{ item.name }}
          </td>
          <td class="text-center">
            {{ item.price }}
          </td>
          <td class="text-center">
            {{ item.quantity }}
          </td>
        </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>
</template>
