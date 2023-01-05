<script setup lang="ts">
import {ref} from "vue";
import {UserDto} from "@/models/UserDto";
import {createEmployee, createEngineer, createExternalUser, getNewUsers} from "@/services/api";
import {NewEngineerDto} from "@/models/NewEngineerDto";
import {NewExternalUserDto} from "@/models/NewExternalUserDto";

const newUsers = ref<UserDto[]>([]);
const selectedUser = ref<UserDto>();
const selectedExternalUser = ref<UserDto>();

async function fetchNewUsers() {
  try {
    newUsers.value = await getNewUsers();
  } catch (error) {
    /* empty */
  }
}

fetchNewUsers();

async function onSubmitEngineers() {
  try {
    if (!selectedUser.value) return;

    const engineerDto = new NewEngineerDto(selectedUser.value.username);
    await createEngineer(engineerDto);
    await fetchNewUsers();
  } catch (error) {
    /* empty */
  }
}

async function onSubmitExternals() {
  try {
    if (!selectedExternalUser.value) return;

    await createExternalUser(new NewExternalUserDto(selectedExternalUser.value.username));
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
              <v-btn color="primary" @click="onSubmitEngineers"> Submit </v-btn>
            </div>
          </v-col>
        </v-row>
    </v-container>
    </v-card-text>
  </v-card>
  <v-card>
    <v-card-title>
      <h2>Create External User</h2>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row>
          <v-col cols="10">
            <v-select
                v-model="selectedExternalUser"
                label="User"
                :items="newUsers"
                item-title="username"
                item-value="username"
                return-object
            ></v-select>
          </v-col>
          <v-col>
            <div class="text-center">
              <v-btn color="primary" @click="onSubmitExternals"> Submit </v-btn>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<style scoped>

</style>