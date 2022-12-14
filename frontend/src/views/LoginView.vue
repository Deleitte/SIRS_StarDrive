<script setup lang="ts">
import { ref } from "vue";
import { login } from "@/services/api";
import {LoginRequestDto} from "@/models/LoginRequestDto";

const username = ref("");
const password = ref("");
const showPassword = ref(false);

function onSubmit() {
  const loginRequest = new LoginRequestDto(username.value, password.value);
  login(loginRequest);
}
</script>

<template>
  <div class="d-flex fill-height flex-column">
    <v-spacer></v-spacer>
    <v-card min-width="450px" class="mx-auto pa-4">
      <v-card-title class="mb-4">
        <h2>Login</h2>
      </v-card-title>

      <v-card-text>
        <v-text-field label="Username" v-model="username"></v-text-field>
        <v-text-field
            label="Password"
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append-inner="showPassword = !showPassword"
        >
        </v-text-field>

        <v-btn variant="tonal" color="secondary" width="100%" min-height="50px" @click="onSubmit">Log In</v-btn>
      </v-card-text>
    </v-card>

    <!-- I don't like this -->
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>
  </div>
</template>
