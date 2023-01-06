<script setup lang="ts">
import { reactive, ref } from "vue";
import { changePassword } from "@/services/api";
import type { StarDriveError } from "@/models/StarDriveError";
import { ChangePasswordDto } from "@/models/ChangePasswordDto";
import router from "@/router";

const changePasswordDto = reactive(new ChangePasswordDto({}));
const form = ref(false);
const showOldPassword = ref(false);
const showNewPassword = ref(false);

async function onSubmit() {
  try {
    await changePassword(changePasswordDto);
    await router.push("/");
  } catch (error) {
    const starDriveError = error as StarDriveError;
  }
}
</script>

<template>
  <div class="d-flex fill-height flex-column">
    <v-spacer></v-spacer>

    <v-card width="450px" class="mx-auto pa-4">
      <v-card-title class="mb-4">
        <h2>Change Password</h2>
      </v-card-title>

      <v-card-text>
        <v-form v-model="form" @submit.prevent="onSubmit">
          <v-text-field
            class="mb-1"
            label="Old Password"
            name="oldPassword"
            :type="showOldPassword ? 'text' : 'password'"
            v-model="changePasswordDto.oldPassword"
            :append-inner-icon="showOldPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append-inner="showOldPassword = !showOldPassword"
          >
          </v-text-field>
          <v-text-field
            class="mb-1"
            label="New Password"
            name="newPassword"
            :type="showNewPassword ? 'text' : 'password'"
            v-model="changePasswordDto.newPassword"
            :append-inner-icon="showNewPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append-inner="showNewPassword = !showNewPassword"
          >
          </v-text-field>
          <v-btn
            variant="tonal"
            color="secondary"
            width="100%"
            height="50px"
            type="submit"
            >Save</v-btn
          >
        </v-form>
      </v-card-text>
    </v-card>

    <!-- I don't like this -->
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>
  </div>
</template>
