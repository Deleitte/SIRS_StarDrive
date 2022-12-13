import axios from "axios";

import { useAuthStore } from "../stores/auth";

const authStore = useAuthStore();
const http = axios.create({
    baseURL: "http://localhost:8080",
    headers: { "Content-Type": "application/json" },
});

http.interceptors.request.use(
    (config) => {
        if (config.headers && !config.headers.Authorization) {
            const token = authStore.token;
            if (token)
                config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export async function login(username: string, password: string) {
    const res =  await http.post("/token", { username, password });
    authStore.login(res.data);
    console.log("Logged in", res.data);
}
