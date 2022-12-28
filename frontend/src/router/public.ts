export default [
  {
    path: "/",
    name: "home",
    component: () => import("../views/HomeView.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/LoginView.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) => !loggedIn,
    },
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/RegisterView.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) => !loggedIn,
    },
  },
  {
    path: "/changepassword",
    name: "changepassword",
    component: () => import("../views/ChangePassword.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) => loggedIn,
    },
  },
  {
    path: "/controlpanel",
    name: "controlpanel",
    component: () => import("../views/ControlPanelView.vue"),
  }
];
