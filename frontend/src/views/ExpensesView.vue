<script setup lang="ts">
import {ref} from "vue";
import ExpenseDto from "@/models/ExpenseDto";
import {getExpenses} from "@/services/api";

const expenses = ref<ExpenseDto[]>([]);
const bars = ref<Bar[]>([]);

interface Bar {
  name: string;
  percentage: number;
  foreground: string;
  background: string;
}

async function fetchExpenses() {
  try {
    expenses.value = (await getExpenses()).sort((a, b) => b.cost - a.cost);
  } catch (error) {
    /* empty */
  }
}

fetchExpenses();
</script>

<template>
  <h1>
    Expense Report
  </h1>
  <div>
    <v-container>
      <v-row>
        <v-col>
          <v-card>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">
                  Team
                </th>
                <th class="text-left">
                  Cost
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="item in expenses" :key="item.teamName">
                <td>
                  {{ item.teamName }}
                </td>
                <td>
                  €{{ item.cost }}
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card>

        </v-col>

      </v-row>
    </v-container>

  </div>
</template>


