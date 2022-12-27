<script setup lang="ts">

import {SensorDto} from "@/models/SensorDto";
import {ref} from "vue";
import {getSensors, getActuators} from "@/services/api";
import {ActuatorDto} from "@/models/ActuatorDto";

var sensors = ref<SensorDto[]>([]);
var actuators = ref<ActuatorDto[]>([]);

async function fetchSensors() {
    try {
        sensors.value = await getSensors();
    } catch (error) {
    }
}

async function fetchActuators() {
    try {
        actuators.value = await getActuators();
    } catch (error) {
    }
}

fetchSensors();
fetchActuators()
</script>

<template>
    <h1>Sensors</h1>
    <v-table density="compact">
      <thead>
      <tr>
        <th class="text-left">
          Name
        </th>
        <th class="text-left">
          Value
        </th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="item in sensors"
          :key="item.name"
      >
        <td>{{ item.name }}</td>
        <td>{{ item.value }}</td>
      </tr>
      </tbody>
    </v-table>

  <h1>Actuators</h1>
  <v-table density="compact">
    <thead>
    <tr>
      <th class="text-left">
        Name
      </th>
      <th class="text-center">
        Interval
      </th>
      <th class="text-center">
        On
      </th>
      <th class="text-center">
        Damaged
      </th>
    </tr>
    </thead>
    <tbody>
    <tr
        v-for="item in actuators"
        :key="item.name"
    >
      <td>{{ item.name }}</td>
      <td class="text-center">{{ item.interval }}</td>
      <td class="text-center">
        <v-icon v-if="item.on" color="green">mdi-checkbox-blank-circle</v-icon>
        <v-icon v-else color="red">mdi-checkbox-blank-circle</v-icon>
      </td>
      <td class="text-center">
        <v-icon v-if="item.damaged" color="orange darken-2">mdi-alert</v-icon>
        <p v-else>-</p>
      </td>
    </tr>
    </tbody>
  </v-table>
</template>