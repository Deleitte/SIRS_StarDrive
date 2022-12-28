import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { JWTPayloadData } from "@/models/JWTPayload";

export const useAuthStore = defineStore("auth", () => {
  const token = ref("");
  const payload = ref<JWTPayloadData | null>(null);
  const username = computed(() => payload.value?.username ?? "");
  const role = computed(() => payload.value?.scope ?? "");
  const expiresAt = computed(() => payload.value?.expiresAt ?? new Date());
  const loggedIn = computed(() => token.value !== "");

  function setToken(authToken: string) {
    token.value = authToken;
    if (authToken)
      payload.value = new JWTPayloadData(
        JSON.parse(atob(authToken.split(".")[1]))
      );
    else payload.value = null;
  }

  return { token, username, role, expiresAt, setToken, loggedIn };
});
