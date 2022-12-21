<script setup lang="ts">
import { RouterView } from "vue-router";
import { ref, computed, onMounted } from "vue";
import Cookies from "js-cookie";

import { useAuthStore } from "@/stores/auth";

const drawer = ref(false);

const authStore = useAuthStore();

const menuItems = [
  { title: "Login", icon: "mdi-login", to: "/login", showIf: () => !authStore.loggedIn },
  { title: "Register", icon: "mdi-account-plus", to: "/register", showIf: () => !authStore.loggedIn },
  { title: "Home", icon: "mdi-home", to: "/", showIf: () => authStore.loggedIn },
];

const filteredMenuItems = computed(() => menuItems.filter((item) => item.showIf()));

const theme = ref("dark");
const themeIcon = computed(() => (theme.value === "dark" ? "mdi-moon-waxing-crescent" : "mdi-weather-sunny"));
const toggleTheme = () => {
  theme.value = theme.value === "dark" ? "light" : "dark";
  localStorage.setItem("theme", theme.value);
};

onMounted(() => {
  theme.value = localStorage.getItem("theme") ?? "dark";
  const authToken = Cookies.get("authToken");
  if (authToken)
    authStore.setToken(authToken);
});

authStore.$subscribe((_, state) => {
  if (state.token)
    Cookies.set("authToken", state.token, { expires: authStore.expiresAt });
  else
    Cookies.remove("authToken");
});

const logout = () => {
  authStore.setToken("");
};
</script>

<template>
  <v-app :theme="theme">
    <v-navigation-drawer v-model="drawer">
      <v-list>
        <v-list-item
          v-for="item in filteredMenuItems"
          :key="item.title"
          :to="item.to"
          :prepend-icon="item.icon"
          :title="item.title"
          link
          @click="drawer = false"
        ></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar>
      <template #prepend>
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>StarDrive</v-app-bar-title>

      <template #append>
        <v-btn :prepend-icon="themeIcon" @click="toggleTheme">Toggle theme</v-btn>
        <v-btn v-if="authStore.loggedIn" prepend-icon="mdi-logout" @click="logout">Logout</v-btn>
      </template>
    </v-app-bar>

    <v-main class="fill-height">
      <v-container class="fill-height">
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>
