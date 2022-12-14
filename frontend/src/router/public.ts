export default [
  {
    path: "/",
    name: "home",
    component: () => import("../views/HomeView.vue"),
    /*meta: {
      requiresAuth: true,
    }*/
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/LoginView.vue"),
  },
];
