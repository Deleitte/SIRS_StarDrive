<script setup lang="ts">
import {ref} from "vue";
import {UserDto} from "@/models/UserDto";
import {createEmployee, createEngineer, getNewUsers} from "@/services/api";
import {NewEngineerDto} from "@/models/NewEngineerDto";

const newUsers = ref<UserDto[]>([]);
const selectedUser = ref<UserDto>();

async function fetchNewUsers() {
  try {
    newUsers.value = await getNewUsers();
  } catch (error) {
    /* empty */
  }
}

fetchNewUsers();

async function onSubmit() {
  try {
    if (!selectedUser.value) return;

    const engineerDto = new NewEngineerDto(selectedUser.value.username);
    await createEngineer(engineerDto);
    await fetchNewUsers();
  } catch (error) {
    /* empty */
  }
}

</script>

<template>
  <v-card>
    <v-card-title>
      <h2>Create Engineer</h2>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row>
          <v-col cols="10">
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
            <div class="text-center">
              <v-btn color="primary" @click="onSubmit"> Submit </v-btn>
            </div>
          </v-col>
        </v-row>
    </v-container>
    </v-card-text>
  </v-card>
</template>

<style scoped>

</style>