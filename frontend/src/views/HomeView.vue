<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";
import { computed, ref } from "vue";
import { getStats } from "@/services/api";
import { StatsDto } from "@/models/StatsDto";

const authStore = useAuthStore();
const message = computed(() =>
  authStore.token
    ? `Welcome ${authStore.username} (${authStore.role})`
    : "You are not logged in"
);

/*
const teams = ref<TeamDto[]>([]);
async function fetchTeams() {
  try {
    teams.value = await getTeams();
  } catch (error) {
  }
}

fetchTeams();
*/

const stats = ref<StatsDto>();
async function fetchStats() {
  try {
    stats.value = await getStats();
  } catch (error) {
    /* empty */
  }
}

fetchStats();
</script>

<template>
  <v v-if="authStore.token">
    <h1>{{ message }}</h1>
    <v-divider></v-divider>
  </v>
  <div>
    <v-row>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h1>Working sensors</h1></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">{{
              stats?.sensors
            }}</v-col>
          </v-row>
        </v-container>
      </v-col>
      <v-col>
        <v-container>
          <v-row>
            <v-col class="d-flex justify-center align-center"
              ><h1>Working actuators</h1></v-col
            >
          </v-row>
          <v-row>
            <v-col class="d-flex justify-center align-center">{{
              stats?.actuators
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
              ><h1>Energy consumption</h1></v-col
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
              ><h1>Cars in production</h1></v-col
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
  <!--
  <h1>{{ message }}</h1>

  <div>
    <div v-for="team in teams" :key="team.name">
      <v-divider></v-divider>
      <h2>{{ team.name }}</h2>
      <h3>Team Salary: {{ team.salary }}</h3>
      <h3>Team Employees:</h3>
      <ul>
        <li v-for="employee in team.employees" :key="employee.name">
          {{ employee.name }}
        </li>
      </ul>
    </div>
  </div>
  -->
</template>
