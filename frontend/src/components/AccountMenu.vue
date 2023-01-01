<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";
import { logout } from "@/services/api";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";

const authStore = useAuthStore();
const { token, username, role } = storeToRefs(authStore);
const router = useRouter();

async function onLogout() {
  await logout();
  await router.push({ name: "login" });
}
</script>

<template>
  <v-menu>
    <template #activator="{ props }">
      <v-btn prepend-icon v-bind="props">
        <v-avatar icon="mdi-account" size="32" />
        {{ token ? `Welcome ${username} (${role})` : "You're not logged in" }}
      </v-btn>
    </template>

    <v-list>
      <v-list-item
        v-if="token"
        prepend-icon="mdi-logout"
        title="Logout"
        @click="onLogout"
      ></v-list-item>

      <v-list-item
        v-if="!token"
        prepend-icon="mdi-login"
        :to="{ name: 'login' }"
        title="Login"
      ></v-list-item>

      <v-list-item
        v-if="!token"
        prepend-icon="mdi-account-plus"
        :to="{ name: 'register' }"
        title="Register"
      ></v-list-item>
    </v-list>
  </v-menu>
</template>
