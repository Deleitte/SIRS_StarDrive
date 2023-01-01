<script setup lang="ts">
import { SensorDto } from "@/models/SensorDto";
import { ref } from "vue";
import {
  getSensors,
  getActuators,
  createSensor,
  createActuator,
  turnOffActuator,
  turnOnActuator,
} from "@/services/api";
import { ActuatorDto } from "@/models/ActuatorDto";
import { NewSensorDto } from "@/models/NewSensorDto";
import { NewActuatorDto } from "@/models/NewActuatorDto";

const sensors = ref<SensorDto[]>([]);
const actuators = ref<ActuatorDto[]>([]);
const dialogSensor = ref(false);
const dialogActuator = ref(false);

const sensorName = ref("");
const sensorPublickey = ref("");

const actuatorName = ref("");
const actuatorPublickey = ref("");
const actuatorPingInterval = ref(0);

async function fetchSensors() {
  try {
    sensors.value = await getSensors();
  } catch (error) {
    /* empty */
  }
}

async function fetchActuators() {
  try {
    actuators.value = await getActuators();
  } catch (error) {
    /* empty */
  }
}

async function onSubmitSensor() {
  try {
    const sensor = new NewSensorDto(
      sensorName.value,
      sensorPublickey.value.replace(/\s/g, "")
    );
    await createSensor(sensor);
    await fetchSensors();
    dialogSensor.value = false;
  } catch (error) {
    /* empty */
  }
}

async function onSubmitActuator() {
  try {
    const actuator = new NewActuatorDto(
      actuatorName.value,
      actuatorPingInterval.value,
      actuatorPublickey.value.replace(/\s/g, "")
    );
    await createActuator(actuator);
    //await fetchActuators();
    dialogActuator.value = false;
  } catch (error) {
    /* empty */
  }
}

async function onToggleActuator(actuator: ActuatorDto) {
  try {
    if (actuator.on) await turnOffActuator(actuator);
    else await turnOnActuator(actuator);
    await fetchActuators();
  } catch (error) {
    /* empty */
  }
}

setInterval(fetchSensors, 1000);
setInterval(fetchActuators, 1000);
</script>

<template>
  <v-card>
    <v-card-title>
      <v-container>
        <v-row>
          <v-col>
            <h2>Sensors</h2>
          </v-col>
          <v-col class="d-flex justify-end">
            <div class="text-center">
              <v-dialog v-model="dialogSensor">
                <template v-slot:activator="{ props }">
                  <v-btn color="primary" v-bind="props"> New Sensor </v-btn>
                </template>

                <v-card>
                  <v-form @submit.prevent="onSubmitSensor">
                    <v-container>
                      <v-row>
                        <v-text-field
                          v-model="sensorName"
                          label="Name"
                          required
                        ></v-text-field>
                      </v-row>
                      <v-row>
                        <v-textarea
                          v-model="sensorPublickey"
                          label="Public Key"
                          required
                        ></v-textarea>
                      </v-row>
                      <v-btn variant="tonal" color="secondary" type="submit"
                        >Create Sensor</v-btn
                      >
                    </v-container>
                  </v-form>
                </v-card>
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
            <th class="text-left">Name</th>
            <th class="text-left">Value</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in sensors" :key="item.name">
            <td>{{ item.name }}</td>
            <td>{{ item.value }}</td>
          </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>

  <v-card class="mt-4">
    <v-card-title>
      <v-container>
        <v-row>
          <v-col>
            <h2>Actuators</h2>
          </v-col>
          <v-col class="d-flex justify-end">
            <div class="text-center">
              <v-dialog v-model="dialogActuator">
                <template v-slot:activator="{ props }">
                  <v-btn color="primary" v-bind="props"> New Actuator </v-btn>
                </template>

                <v-card>
                  <v-form @submit.prevent="onSubmitActuator">
                    <v-container>
                      <v-row>
                        <v-text-field
                          v-model="actuatorName"
                          label="Name"
                          required
                        ></v-text-field>
                      </v-row>
                      <v-row>
                        <v-textarea
                          v-model="actuatorPublickey"
                          label="Public Key"
                          required
                        ></v-textarea>
                      </v-row>
                      <v-row>
                        <v-text-field
                          v-model="actuatorPingInterval"
                          label="Ping Interval"
                          required
                        ></v-text-field>
                      </v-row>
                      <v-btn variant="tonal" color="secondary" type="submit"
                        >Create Actuator</v-btn
                      >
                    </v-container>
                  </v-form>
                </v-card>
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
            <th class="text-left">Name</th>
            <th class="text-center">Interval</th>
            <th class="text-center">On</th>
            <th class="text-center">Damaged</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in actuators" :key="item.name">
            <td>{{ item.name }}</td>
            <td class="text-center">{{ item.interval }}</td>
            <td class="text-center">
              <v-btn icon variant="plain" @click="onToggleActuator(item)">
                <v-icon :color="item.on ? 'green' : 'red'"
                  >mdi-checkbox-blank-circle</v-icon
                >
              </v-btn>
            </td>
            <td class="text-center">
              <v-icon v-if="item.damaged" color="orange darken-2"
                >mdi-alert</v-icon
              >
              <p v-else>-</p>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>
</template>
