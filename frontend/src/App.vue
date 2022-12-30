<script setup lang="ts">
import { RouterView } from "vue-router";
import { ref, computed, onMounted } from "vue";

import { useAuthStore } from "@/stores/auth";
import router from "@/router";
import { logout, refreshToken } from "@/services/api";

const drawer = ref(false);

const authStore = useAuthStore();

const menuItems = [
  {
    title: "Home",
    icon: "mdi-home",
    to: "/",
    showIf: () => true || authStore.loggedIn,
  },
  {
    title: "Personal Data",
    icon: "mdi-account",
    to: "/private",
    showIf: () => authStore.loggedIn,
  },
  {
    title: "Control Panel",
    icon: "mdi-cog",
    to: "/controlpanel",
    showIf: () => authStore.loggedIn,
  },
];

const filteredMenuItems = computed(() =>
  menuItems.filter((item) => item.showIf())
);

const theme = ref("dark");
const themeIcon = computed(() =>
  theme.value === "dark" ? "mdi-moon-waxing-crescent" : "mdi-weather-sunny"
);
const toggleTheme = () => {
  theme.value = theme.value === "dark" ? "light" : "dark";
  localStorage.setItem("theme", theme.value);
};

onMounted(async () => {
  theme.value = localStorage.getItem("theme") ?? "dark";
  // TODO: need a better on first load than this
  if (!authStore.token) {
    try {
      await refreshToken();
    } catch (err) {
      /* empty */
    }
  }
});

const onLogout = async () => {
  await logout();
  await router.push({ name: "login" });
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

    <v-app-bar density="compact">
      <template #prepend>
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>StarDrive</v-app-bar-title>

      <template #append>
        <v-btn :prepend-icon="themeIcon" @click="toggleTheme"
          >Toggle theme</v-btn
        >

        <v-menu>
          <template #activator="{ props }">
            <v-btn prepend-icon v-bind="props">
              <v-avatar icon="mdi-account" size="32" />
            </v-btn>
          </template>

          <v-list>
            <v-list-item
              v-if="authStore.token"
              prepend-icon="mdi-logout"
              title="Logout"
              @click="onLogout"
            ></v-list-item>

            <v-list-item
              v-if="!authStore.token"
              prepend-icon="mdi-login"
              :to="{ name: 'login' }"
              title="Login"
              @click="drawer = false"
            ></v-list-item>

            <v-list-item
              v-if="!authStore.token"
              prepend-icon="mdi-account-plus"
              :to="{ name: 'register' }"
              title="Register"
              @click="drawer = false"
            ></v-list-item>
          </v-list>
        </v-menu>
      </template>
    </v-app-bar>

    <v-main class="fill-height">
      <v-container class="fill-height">
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>
