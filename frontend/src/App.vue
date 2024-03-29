<script setup lang="ts">
import {RouterView} from "vue-router";
import {computed, onMounted, ref} from "vue";

import {useAuthStore} from "@/stores/auth";
import AccountMenu from "@/components/AccountMenu.vue";

const drawer = ref(false);

const authStore = useAuthStore();

const menuItems = [
  {
    title: "Home",
    icon: "mdi-home",
    to: "/",
  },
  {
    title: "Personal Data",
    icon: "mdi-account",
    to: "/private",
    showIf: () => authStore.loggedIn && authStore.role === "EMPLOYEE",
  },
  {
    title: "Control Panel",
    icon: "mdi-cog",
    to: "/controlpanel",
    showIf: () => authStore.loggedIn && authStore.role === "ENGINEER",
  },
  {
    title: "Employee Management",
    icon: "mdi-account-group",
    to: "/employees",
    showIf: () => authStore.loggedIn && authStore.role === "ENGINEER",
  },
  {
    title: "Stock Management",
    icon: "mdi-package-variant-closed",
    to: "/stock",
    showIf: () => authStore.loggedIn && authStore.role === "ENGINEER",
  },
  {
    title: "Logs",
    icon: "mdi-file-document",
    to: "/logs",
    showIf: () => authStore.loggedIn && authStore.role === "ENGINEER",
  },
  {
    title: "Dashboard",
    icon: "mdi-view-dashboard",
    to: "/admin",
    showIf: () => authStore.loggedIn && authStore.role === "ADMIN",
  },
  {
    title: "Expense Report",
    icon: "mdi-bank",
    to: "/external",
    showIf: () => authStore.loggedIn && authStore.role === "EXTERNAL",
  },
];

const filteredMenuItems = computed(() =>
    menuItems.filter((item) => !item.showIf || item.showIf())
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
});
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
        <v-btn :icon="themeIcon" @click="toggleTheme"></v-btn>
        <account-menu/>
      </template>
    </v-app-bar>

    <v-main class="fill-height">
      <v-container class="fill-height">
        <router-view/>
      </v-container>
    </v-main>
  </v-app>
</template>
