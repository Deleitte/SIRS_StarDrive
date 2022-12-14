import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@/stores/auth";
import publicRoutes from "./public";
import privateRoutes from "./private";

const showPrivateRoutes = import.meta.env.VITE_PRIVATE !== "false";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...publicRoutes,
    ...(showPrivateRoutes ? privateRoutes : []),
  ],
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  if (to.meta.requiresAuth && !authStore.token) {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router;
