<script setup lang="ts">
import { ref } from "vue";
import { login } from "@/services/api";
import {LoginRequestDto} from "@/models/LoginRequestDto";

const username = ref("");
const password = ref("");

function onSubmit() {
  const loginRequest = new LoginRequestDto(username.value, password.value);
  login(loginRequest);
}
</script>

<template>
  <v-form>
    <v-card max-width="450px" class="mx-auto">
      <v-card-text>
        <v-text-field label="Username" v-model="username"></v-text-field>
        <v-text-field label="Password" type="password" v-model="password"></v-text-field>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" width="100%" @click="onSubmit">Log In</v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>
