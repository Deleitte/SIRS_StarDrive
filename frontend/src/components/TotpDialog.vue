<script setup lang="ts">
import { ref, watch } from "vue";
import type { ActuatorDto } from "@/models/ActuatorDto";
import {totp, updateActuatorPingInterval} from "@/services/api";
import {TotpDto} from "@/models/TotpDto";

interface Props {
  dialog: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits(["close-dialog"]);

const dialog = ref(props.dialog);
const code = ref(0);
const form = ref(false);

watch(dialog, (value) => {
  if (!value) {
    emit("close-dialog");
  }
});

watch(props, (value) => {
  dialog.value = value.dialog;
});

async function onSubmit() {
  await totp(new TotpDto(code.value));
  dialog.value = false;
}

</script>

<template>
  <div class="text-center">
    <v-dialog v-model="dialog" max-width="700px">
      <v-card class="pa-2">
        <v-card-title class="text-center">
          <h2>Insert 2FA code</h2>
        </v-card-title>

        <v-card-text class="mt-2">
          <v-form v-model="form" @submit.prevent="onSubmit">
            <v-text-field
              v-model="code"
              label="Code"
              type="number"
              class="mb-2"
            />

            <v-btn
              color="secondary"
              variant="tonal"
              block
              type="submit"
            >
              Submit</v-btn
            >
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>
