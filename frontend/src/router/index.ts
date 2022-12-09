import { createRouter, createWebHistory } from "vue-router";

import privateRoutes from "./private";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/HomeView.vue"),
    },
    ...privateRoutes,
  ],
});

export default router;
