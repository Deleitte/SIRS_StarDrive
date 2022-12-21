<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";
import { computed, ref } from "vue";
import { getTeams } from "@/services/api";
import { TeamDto } from "@/models/TeamDto";

const authStore = useAuthStore();
const message = computed(() =>
    authStore.token ? `Welcome ${authStore.username} (${authStore.role})` : "You are not logged in");

const teams = ref<TeamDto[]>([]);
async function fetchTeams() {
  try {
    teams.value = await getTeams();
  } catch (error) {
  }
}

fetchTeams();
</script>

<template>
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
</template>
