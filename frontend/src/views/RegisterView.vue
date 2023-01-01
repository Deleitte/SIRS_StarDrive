<script setup lang="ts">
import { ref } from "vue";
import { register } from "@/services/api";
import router from "@/router";
import { StarDriveError } from "@/models/StarDriveError";
import { RegisterRequestDto } from "@/models/RegisterRequestDto";

const name = ref("");
const username = ref("");
const password = ref("");
const showPassword = ref(false);
const form = ref(false);
const userErrorMessage = ref<string | undefined>(undefined);
const passwordErrorMessage = ref<string | undefined>(undefined);

async function onSubmit() {
  userErrorMessage.value = undefined;
  passwordErrorMessage.value = undefined;
  try {
    const registerRequest = new RegisterRequestDto(
      name.value,
      username.value,
      password.value
    );
    await register(registerRequest);
    await router.push("/");
  } catch (error) {
    const starDriveError = error as StarDriveError;
    if (starDriveError.message.includes("User"))
      userErrorMessage.value = (error as StarDriveError).message;
    else passwordErrorMessage.value = (error as StarDriveError).message;
  }
}
</script>

<template>
  <div class="d-flex fill-height flex-column">
    <v-spacer></v-spacer>

    <v-card width="450px" class="mx-auto pa-4">
      <v-card-title class="mb-4">
        <h2>Register</h2>
      </v-card-title>

      <v-card-text>
        <v-form v-model="form" @submit.prevent="onSubmit">
          <v-text-field
            class="mb-1"
            label="Name"
            v-model="name"
            name="name"
          ></v-text-field>

          <v-text-field
            class="mb-1"
            label="Username"
            v-model="username"
            name="username"
            :error-messages="userErrorMessage"
          >
          </v-text-field>
          <v-text-field
            class="mb-1"
            label="Password"
            name="password"
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append-inner="showPassword = !showPassword"
            :error-messages="passwordErrorMessage"
          >
          </v-text-field>
          <v-btn
            variant="tonal"
            color="secondary"
            width="100%"
            height="50px"
            type="submit"
            >Log In</v-btn
          >
        </v-form>
      </v-card-text>
    </v-card>

    <!-- I don't like this -->
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>
  </div>
</template>
