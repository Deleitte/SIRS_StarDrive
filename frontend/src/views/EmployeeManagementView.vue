<script setup lang="ts">
import {ref} from "vue";
import {createEmployee, createTeam, getEmployees, getNewUsers, getTeams,} from "@/services/api";
import type {EmployeeDto} from "@/models/EmployeeDto";
import type {UserDto} from "@/models/UserDto";
import type {TeamDto} from "@/models/TeamDto";
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

const usernames = ref<UserName[]>();
interface UserName {
    username: string;
}

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
    if (!selectedTeam.value || !selectedUser.value) return;

    const assignDto = new AssignEmployeeToTeamDto(
        selectedUser.value.username,
        selectedTeam.value.name
    );
    await createEmployee(assignDto);
    await fetchEmployees();
    await fetchNewUsers();
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
  <v-card>
    <v-card-title>
      <v-container>
        <v-row>
          <v-col>
            <h2>Teams</h2>
          </v-col>
          <v-col class="d-flex justify-end">
            <div class="text-center">
              <v-dialog v-model="dialogTeams">
                <template v-slot:activator="{ props }">
                  <v-btn color="primary" v-bind="props"> New Team</v-btn>
                </template>
                <v-form @submit.prevent="onSubmitTeam">
                  <v-card>
                    <v-card-text>
                      <v-text-field v-model="newTeam" label="Name" required/>
                      <v-text-field v-model="salary" label="Salary" required/>
                    </v-card-text>
                    <div class="text-center">
                      <v-btn color="primary" type="submit"> Create</v-btn>
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
          <th class="text-left">Name</th>
          <th class="text-center">Salary</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in teams" :key="item.name">
          <td class="text-left">
            {{ item.name }}
          </td>
          <td class="text-center">
            {{ item.salary }}
          </td>
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
            <h2>Employees</h2>
          </v-col>
          <v-col class="d-flex justify-end">
            <div class="text-center">
              <v-dialog v-model="dialogAssign">
                <template v-slot:activator="{ props }">
                  <v-btn color="primary" v-bind="props"> Assign Team</v-btn>
                </template>
                <v-card>
                  <v-form @submit.prevent="onSubmitAssign">
                    <v-container>
                      <v-row>
                        <v-col>
                          <v-select
                              v-model="selectedUser"
                              label="User"
                              :items="newUsers"
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
                        <v-btn color="primary" type="submit"> Assign</v-btn>
                      </div>
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
          <th class="text-center">Username</th>
          <th class="text-center">Team</th>
          <th class="text-center">Salary</th>
          <th class="text-center">Absent Working days</th>
          <th class="text-center">Parental Leaves</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="employee in employees">
          <td>{{ employee.name }}</td>
          <td class="text-center">{{ employee.username }}</td>
          <td class="text-center">{{ employee.team }}</td>
          <td class="text-center">{{ employee.salary }}</td>
          <td class="text-center">{{ employee.absentWorkingDays }}</td>
          <td class="text-center">{{ employee.parentalLeaves }}</td>
        </tr>
        </tbody>
      </v-table>
    </v-card-text>
  </v-card>
</template>
