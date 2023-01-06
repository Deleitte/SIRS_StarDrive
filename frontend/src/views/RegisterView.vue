<script setup lang="ts">
import { ref } from "vue";
import { register } from "@/services/api";
import type { StarDriveError } from "@/models/StarDriveError";
import { RegisterRequestDto } from "@/models/RegisterRequestDto";
import QrcodeVue from "qrcode.vue";

const name = ref("");
const username = ref("");
const password = ref("");
const showPassword = ref(false);
const form = ref(false);
const userErrorMessage = ref<string | undefined>(undefined);
const passwordErrorMessage = ref<string | undefined>(undefined);
const submitted = ref<boolean>(false);
let totpKey = "";

async function onSubmit() {
  userErrorMessage.value = undefined;
  passwordErrorMessage.value = undefined;
  try {
    const registerRequest = new RegisterRequestDto(
      name.value,
      username.value,
      password.value
    );
    let dto = await register(registerRequest);
    submitted.value = true;
    totpKey = dto.totpSecret;
  } catch (error) {
    const starDriveError = error as StarDriveError;
    if (starDriveError.message.includes("User"))
      userErrorMessage.value = (error as StarDriveError).message;
    else passwordErrorMessage.value = (error as StarDriveError).message;
  }
}

function generateQrCode(totpSecret: string) {
  const url = `otpauth://totp/Stardrive:${username.value}?secret=${totpSecret}&issuer=Stardrive&algorithm=SHA1`;
  const encodedUrl = encodeURI(url);
  return encodedUrl;
}
</script>

<template>
  <div class="d-flex fill-height flex-column">
    <v-spacer></v-spacer>

    <v-card width="450px" class="mx-auto pa-4">
      <v-card-title class="mb-4">
        <h2>Register</h2>
      </v-card-title>

      <v-card-text v-if="!submitted">
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
            >Log In
          </v-btn>
        </v-form>
      </v-card-text>
      <v-card-text v-else class="d-flex flex-column align-center">
        <qrcode-vue :value="generateQrCode(totpKey)" level="M" :size="300" />
        <v-btn
          variant="tonal"
          color="secondary"
          width="100%"
          height="50px"
          class="mt-4"
          @click="$router.push('/')"
        >
          Continue
        </v-btn>
      </v-card-text>
    </v-card>

    <!-- I don't like this -->
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>
  </div>
</template>
