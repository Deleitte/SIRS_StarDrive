import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@/stores/auth";
import publicRoutes from "./public";
import privateRoutes from "./private";
import { useRedirectStore } from "@/stores/redirect";

const showPrivateRoutes = import.meta.env.VITE_PRIVATE !== "false";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...publicRoutes, ...(showPrivateRoutes ? privateRoutes : [])],
});

router.beforeEach((to, from, next) => {
  const { loggedIn, role } = useAuthStore();
  const { setUrl } = useRedirectStore();
  const redirectTo = loggedIn ? "home" : "login";

  const shouldRedirect = // @ts-ignore
    to.meta.requiredAuth && !to.meta.requiredAuth(loggedIn, role);

  if (shouldRedirect && !loggedIn) setUrl(to.path);
  else if (!to.redirectedFrom) setUrl(undefined);

  if (shouldRedirect) next({ name: redirectTo });
  else next();
});

export default router;
