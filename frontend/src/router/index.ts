import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@/stores/auth";
import publicRoutes from "./public";
import privateRoutes from "./private";
import { useRedirectStore } from "@/stores/redirect";
import {refreshToken} from "@/services/api";

const showPrivateRoutes = import.meta.env.VITE_PRIVATE !== "false";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...publicRoutes, ...(showPrivateRoutes ? privateRoutes : [])],
});

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  const { setUrl } = useRedirectStore();

  if (!authStore.loggedIn) {
    try {
      await refreshToken();
    } catch (err) {
      /* empty */
    }
  }

  const redirectTo = authStore.loggedIn ? "home" : "login";

  const shouldRedirect = // @ts-ignore
    to.meta.requiredAuth && !to.meta.requiredAuth(authStore.loggedIn, authStore.role);

  if (shouldRedirect && !loggedIn) setUrl(to.path);
  else if (!to.redirectedFrom) setUrl(undefined);

  if (shouldRedirect) next({ name: redirectTo });
  else next();
});

export default router;
