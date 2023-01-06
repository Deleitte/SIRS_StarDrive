<script setup lang="ts">

import type {LogDto} from "@/models/LogDto";
import {ref} from "vue";
import {getLogs} from "@/services/api";

const logs = ref<LogDto[]>();

async function fetchLogs() {
  try {
    logs.value = await getLogs();
  } catch (error) {
  }
}

fetchLogs();
</script>

<template>
  <v-card>
    <v-card-title>
      <h2>Logs</h2>
    </v-card-title>

    <v-card-text>
      <v-table density="compact">
        <thead>
        <tr>
          <th class="text-left">Timestamp</th>
          <th class="text-center">Author</th>
          <th class="text-center">Message</th>
          <th class="text-center">Verified</th>
        </tr>
        </thead>
        <tbody>

        <tr v-for="item in logs" :key="item.timestamp">
          <td class="text-left">
            {{ item.timestamp }}
          </td>
          <td class="text-center">
            {{ item.author }}
          </td>
          <td class="text-center">
            {{ item.message }}
          </td>
          <td v-if="item.verified" class="text-center">
            <v-icon color="green">mdi-check</v-icon>
          </td>
          <td v-else class="text-center">
            <v-icon color="red">mdi-close</v-icon>
          </td>
        </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>

</template>