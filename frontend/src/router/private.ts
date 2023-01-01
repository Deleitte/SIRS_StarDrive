export default [
  {
    path: "/controlpanel",
    name: "controlpanel",
    component: () => import("../views/ControlPanelView.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) =>
        loggedIn && role === "ENGINEER",
    },
  },
  {
    path: "/private",
    name: "private",
    component: () => import("../views/PrivateView.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) =>
        loggedIn && role === "EMPLOYEE",
    },
  },
  {
    path: "/employees",
    name: "employees",
    component: () => import("../views/EmployeeManagementView.vue"),
    meta: {
      requiredAuth: (loggedIn: boolean, role: string) =>
        loggedIn && role === "ENGINEER",
    },
  },
];
