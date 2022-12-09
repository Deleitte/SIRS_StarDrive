const isPrivate = import.meta.env.VITE_PRIVATE !== "false";

export default isPrivate ? [
    {
        path: "/login",
        name: "login",
        component: () => import("../views/LoginView.vue"),
    },
] : [];
