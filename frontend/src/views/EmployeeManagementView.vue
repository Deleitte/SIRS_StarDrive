<script setup lang="ts">
import {ref} from "vue";
import {createEmployee, createTeam, getEmployees, getNewUsers, getTeams} from "../services/api";
import {EmployeeDto} from "../models/EmployeeDto";
import {UserDto} from "@/models/UserDto";
import {TeamDto} from "@/models/TeamDto";
import {AssignEmployeeToTeamDto} from "@/models/AssignEmployeeToTeamDto";
import {NewTeamDto} from "@/models/NewTeamDto";

const employees = ref<EmployeeDto[]>([]);
const dialogAssign = ref(false);
const dialogTeams = ref(false);
const newTeam = ref("");
const salary = ref(0);
const newUsers = ref<UserDto[]>([]);
const teams = ref<TeamDto[]>([]);
const selectedTeam = ref<TeamDto>();
const selectedUser = ref<UserDto>();

async function fetchEmployees() {
  try {
    employees.value = await getEmployees();
  } catch (error) {
    /* empty */
  }
}

async function fetchNewUsers() {
  try {
    newUsers.value = await getNewUsers();
  } catch (error) {
    /* empty */
  }
}

async function fetchTeams() {
  try {
    teams.value = await getTeams();
  } catch (error) {
    /* empty */
  }
}

async function onSubmitAssign() {
   try {
     const assignDto = new AssignEmployeeToTeamDto(selectedUser.value.username, selectedTeam.value.name);
      await createEmployee(assignDto);
      await fetchEmployees();
      dialogAssign.value = false;
   } catch (error) {
     /* empty */
   }
}

async function onSubmitTeam() {
  try {
    const team = new NewTeamDto(newTeam.value, salary.value);
    await createTeam(team);
    await fetchTeams();
    dialogTeams.value = false;
  } catch (error) {
    /* empty */
  }
}

fetchEmployees();
fetchNewUsers();
fetchTeams();
</script>

<template>
  <h1>Teams</h1>
  <v-table density="compact">
    <thead>
    <tr>
      <th class="text-center">
        Name
      </th>
      <th class="text-center">
        Salary
      </th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="item in teams">
      <td class="text-center">
        {{item.name}}
      </td>
      <td class="text-center">
        {{item.salary}}
      </td>
    </tr>
    </tbody>
  </v-table>
  <div class="text-center">
    <v-dialog v-model="dialogTeams">
      <template v-slot:activator="{ props }">
        <v-btn
            color="primary"
            v-bind="props"
        >
          New Team
        </v-btn>
      </template>
      <v-form @submit.prevent="onSubmitTeam">
      <v-card>
        <v-card-text>
          <v-text-field
              v-model="newTeam"
              label="Name"
              required
          />
          <v-text-field
              v-model="salary"
              label="Salary"
              required
          />
        </v-card-text>
        <div class="text-center">
          <v-btn
              color="primary"
              type="submit"
          >
            Create
          </v-btn>
        </div>
      </v-card>
      </v-form>
    </v-dialog>
    </div>
  <h1>Employees</h1>
  <v-table density="compact">
    <thead>
    <tr>
      <th class="text-left">
        Name
      </th>
      <th class="text-center">
        Username
      </th>
      <th class="text-center">
        Team
      </th>
      <th class="text-center">
        Salary
      </th>
      <th class="text-center">
        Absent Working days
      </th>
      <th class="text-center">
        Parental Leaves
      </th>
    </tr>
    </thead>
    <tbody>
    <tr
        v-for="employee in employees"
    >
      <td>{{ employee.name }}</td>
      <td class="text-center">{{ employee.username }}</td>
      <td class="text-center">{{ employee.team }}</td>
      <td class="text-center">{{ employee.salary }}</td>
      <td class="text-center">{{ employee.absentWorkingDays }}</td>
      <td class="text-center">{{ employee.parentalLeaves }}</td>
    </tr>
    </tbody>
  </v-table>
  <div class="text-center">
    <v-dialog v-model="dialogAssign">
      <template v-slot:activator="{ props }">
        <v-btn
            color="primary"
            v-bind="props"
        >
          Assign Team
        </v-btn>
      </template>
      <v-card>
        <v-form @submit.prevent="onSubmitAssign">
          <v-container>
            <v-row>
              <v-col>
                <v-select
                    v-model="selectedUser"
                    label="User"
                    :items="newUsers.concat(employees)"
                    item-title="username"
                    item-value="username"
                    return-object
                ></v-select>
              </v-col>
              <v-col>
                <v-select
                    v-model="selectedTeam"
                    label="Team"
                    :items="teams"
                    item-title="name"
                    item-value="name"
                    return-object
                ></v-select>
              </v-col>
            </v-row>
            <div class="text-center">
              <v-btn
                  color="primary"
                  type="submit"
              >
                Assign
              </v-btn>
            </div>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>
  </div>
</template>

<style scoped>

</style>