<script setup lang="ts">
fetch("http://localhost:8080")
  .then((response) => response.json())
  .then((data) => console.log(data));
</script>

<template>
  <h1>Home</h1>
</template>
