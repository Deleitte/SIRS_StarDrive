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
  const { loggedIn, role } = useAuthStore();
  const redirectTo = loggedIn ? "home" : "login";
  // @ts-ignore
  const shouldRedirect = to.meta.requiredAuth && !to.meta.requiredAuth(loggedIn, role);
  if (shouldRedirect)
    next({ name: redirectTo });
  else
    next();
});

export default router;
