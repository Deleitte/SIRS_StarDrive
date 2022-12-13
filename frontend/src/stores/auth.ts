import { ref } from "vue";
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", () => {
  const token = ref("");

  function login(authToken: string) {
    token.value = authToken;
  }

  return { token, login };
});
