<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";
import { computed } from "vue";

const authStore = useAuthStore();
const message = computed(() =>
    authStore.token ? `Welcome ${authStore.username} (${authStore.role})` : "You are not logged in");
</script>

<template>
  <h1>{{ message }}</h1>
</template>
