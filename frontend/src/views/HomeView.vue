<script setup lang="ts">
import { ref } from "vue";
import { getEmployeesWorkingShifts, getStats } from "@/services/api";
import type { StatsDto } from "@/models/StatsDto";
import type { EmployeeWorkingShiftsDto } from "@/models/EmployeeWorkingShiftsDto";
import {useAuthStore} from "@/stores/auth";

const authStore = useAuthStore();
const stats = ref<StatsDto>();
const employees = ref<EmployeeWorkingShiftsDto[]>([]);

async function fetchStats() {
  try {
    stats.value = await getStats();
  } catch (error) {
    /* empty */
  }
}

async function fetchEmployees() {
  try {
    employees.value = await getEmployeesWorkingShifts();
  } catch (error) {
    /* empty */
  }
}

fetchStats();
fetchEmployees();
</script>

<template>
  <h1>Production Stats</h1>
  <div>
    <v-row>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h2>Working sensors</h2></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">{{
              stats?.totalSensors
            }}</v-col>
          </v-row>
        </v-container>
      </v-col>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h2>Working actuators</h2></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">{{
              stats?.totalActuators
            }}</v-col>
          </v-row>
        </v-container>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h2>Energy consumption</h2></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">
              <v-progress-circular
                model-value="95"
                :size="100"
                :width="15"
                color="primary"
              >
                {{ stats?.energyConsumption }}
              </v-progress-circular>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h2>Cars in production</h2></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">
              <v-progress-circular
                model-value="80"
                :size="100"
                :width="15"
                color="primary"
              >
                {{ stats?.carsInProduction }}
              </v-progress-circular>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </div>
  <div>
    <h1>Working Shifts</h1>
    <div v-for="employee in employees">
      <v-row>
        <v-col>
          <v-container>
            <v-row>
              <v-col>
                <p>{{ employee.name }}</p>
                <div v-for="shift in employee.workingShifts">
                  <p>
                    {{ shift.weekDay }} : {{ shift.startTime }} -
                    {{ shift.endTime }}
                  </p>
                </div>
              </v-col>
            </v-row>
          </v-container>
        </v-col>
      </v-row>
    </div>
  </div>
</template>
