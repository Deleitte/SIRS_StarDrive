<script setup lang="ts">
import { RouterView } from "vue-router";
import { ref, computed } from "vue";

const drawer = ref(false);
const theme = ref("dark");

const themeIcon = computed(() => (theme.value === "dark" ? "mdi-moon-waxing-crescent" : "mdi-weather-sunny"));
const toggleTheme = () => (theme.value = theme.value === "dark" ? "light" : "dark");

const menuItems = [
  { title: "Home", icon: "mdi-home", to: "/" },
  { title: "About", icon: "mdi-account", to: "/about" },
];
</script>

<template>
  <v-app :theme="theme">
    <v-navigation-drawer v-model="drawer">
      <v-list>
        <v-list-item
          v-for="item in menuItems"
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
        <v-btn :icon="themeIcon" @click="toggleTheme"></v-btn>
      </template>
    </v-app-bar>

    <v-main>
      <v-container>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>
