<script setup lang="ts">

import {SensorDto} from "@/models/SensorDto";
import {ref} from "vue";
import {getSensors, getActuators, createSensor, createActuator} from "@/services/api";
import {ActuatorDto} from "@/models/ActuatorDto";
import {NewSensorDto} from "@/models/NewSensorDto";
import {NewActuatorDto} from "@/models/NewActuatorDto";

var sensors = ref<SensorDto[]>([]);
var actuators = ref<ActuatorDto[]>([]);
var dialogSensor = ref(false);
var dialogActuator = ref(false);

const sensorName = ref("");
const sensorPublickey = ref("");

const actuatorName = ref("");
const actuatorPublickey = ref("");
const actuatorPingInterval = ref(0);

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

async function onSubmitSensor() {
    try {
        const sensor = new NewSensorDto(sensorName.value, sensorPublickey.value.replace(/\s/g, ''));
        await createSensor(sensor);
        fetchSensors();
        dialog.value = false;
    } catch (error) {
    }
}

async function onSubmitActuator() {
    try {
        const actuator = new NewActuatorDto(actuatorName.value, actuatorPingInterval.value, actuatorPublickey.value.replace(/\s/g, ''));
        await createActuator(actuator);
        fetchActuators();
        dialog.value = false;
    } catch (error) {
    }
}

fetchSensors();
fetchActuators();
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
  <div class="text-center">
    <v-dialog
        v-model="dialogSensor"
    >
      <template v-slot:activator="{ props }">
        <v-btn
            color="primary"
            v-bind="props"
        >
          New Sensor
        </v-btn>
      </template>

      <v-card>
        <v-form v-model="valid" @submit.prevent="onSubmitSensor">
          <v-container>
            <v-row>
              <v-text-field
                  v-model="sensorName"
                  label="Name"
                  required
              ></v-text-field>
            </v-row>
            <v-row>
              <v-text-field
                  v-model="sensorPublickey"
                  label="Public Key"
                  required
              ></v-text-field>
            </v-row>
            <v-btn
                variant="tonal"
                color="secondary"
                type="submit"
            >Create Sensor</v-btn>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>
  </div>
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
  <div class="text-center">
    <v-dialog
        v-model="dialogActuator"
    >
      <template v-slot:activator="{ props }">
        <v-btn
            color="primary"
            v-bind="props"
        >
          New Actuator
        </v-btn>
      </template>

      <v-card>
        <v-form v-model="valid" @submit.prevent="onSubmitActuator">
          <v-container>
            <v-row>
              <v-text-field
                  v-model="actuatorName"
                  label="Name"
                  required
              ></v-text-field>
            </v-row>
            <v-row>
              <v-text-field
                  v-model="actuatorPublickey"
                  label="Public Key"
                  required
              ></v-text-field>
            </v-row>
            <v-row>
              <v-text-field
                  v-model="actuatorPingInterval"
                  label="Ping Interval"
                  required
              ></v-text-field>
            </v-row>
            <v-btn
                variant="tonal"
                color="secondary"
                type="submit"
            >Create Actuator</v-btn>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>
  </div>
</template>