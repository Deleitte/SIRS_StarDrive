<script setup lang="ts">
import { ref, watch } from "vue";
import type { ActuatorDto } from "@/models/ActuatorDto";
import { updateActuatorPingInterval } from "@/services/api";

interface Props {
  dialog: boolean;
  actuator: ActuatorDto | undefined;
}

const props = defineProps<Props>();
const emit = defineEmits(["close-dialog"]);

const dialog = ref(props.dialog);
const pingInterval = ref(props.actuator?.interval ?? 0);
const form = ref(false);

watch(dialog, (value) => {
  if (!value) {
    emit("close-dialog");
  }
});

watch(props, (value) => {
  console.log(value);
  dialog.value = value.dialog;
  pingInterval.value = value.actuator?.interval ?? 0;
});

async function onSubmit() {
  if (!props.actuator) return;
  await updateActuatorPingInterval(props.actuator, pingInterval.value);
  dialog.value = false;
}
</script>

<template>
  <div class="text-center">
    <v-dialog v-model="dialog" max-width="700px">
      <v-card class="pa-2">
        <v-card-title class="text-center">
          <h2>Update Actuator's Ping Interval</h2>
        </v-card-title>

        <v-card-text class="mt-2">
          <v-form v-model="form" @submit.prevent="onSubmit">
            <v-text-field
              v-model="pingInterval"
              label="Ping Interval"
              type="number"
              class="mb-2"
            />

            <v-btn
              color="secondary"
              variant="tonal"
              block
              type="submit"
              @click="onSubmit"
            >
              Update</v-btn
            >
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>
