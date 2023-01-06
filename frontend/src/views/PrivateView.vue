<script setup lang="ts">
import type { PrivateDataDto } from "@/models/PrivateDataDto";
import {ref} from "vue";
import {getPrivateData} from "@/services/api";

const privateData = ref<PrivateDataDto>();

async function fetchPrivateData() {
  try {
    privateData.value = await getPrivateData();
  } catch (error) {
  }
}

fetchPrivateData();

</script>

<template>
  <div>
    <h1>Personal Data</h1>
    <p>Salary: {{privateData?.salary}}</p>
    <p>Absent working days: {{privateData?.absentWorkingDays}}</p>
    <p>Parental leaves: {{privateData?.parentalLeaves}}</p>
  </div>
</template>

<style scoped>

</style>