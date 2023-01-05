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
    {
        path: "/stock",
        name: "stock",
        component: () => import("../views/StockView.vue"),
        meta: {
            requiredAuth: (loggedIn: boolean, role: string) =>
                loggedIn && role === "ENGINEER",
        },
    },
    {
        path: "/logs",
        name: "logs",
        component: () => import("../views/LogView.vue"),
        meta: {
            requiredAuth: (loggedIn: boolean, role: string) =>
                loggedIn && role === "ENGINEER",
        }
    },
    {
        path: "/admin",
        name: "admin",
        component: () => import("../views/AdminView.vue"),
        meta: {
            requiredAuth: (loggedIn: boolean, role: string) =>
                loggedIn && role === "ADMIN",
        }
    },

];
