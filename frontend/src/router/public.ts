export default [
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
        path: "/external",
        name: "external",
        component: () => import("../views/ExpensesView.vue"),
        meta: {
            requiredAuth: (loggedIn: boolean, role: string) =>
                loggedIn && role === "EXTERNAL",
        }
    }
];
