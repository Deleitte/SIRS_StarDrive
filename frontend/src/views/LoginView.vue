<script setup lang="ts">
import { ref } from "vue";
import { login } from "@/services/api";

const username = ref("");
const password = ref("");

function onSubmit() {
  login(username.value, password.value);
}
</script>

<template>
  <v-form>
    <v-card max-width="450px" class="mx-auto">
      <v-card-text>
        <v-text-field label="Username"></v-text-field>
        <v-text-field label="Password" type="password"></v-text-field>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" width="100%" @click="onSubmit">Log In</v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>
