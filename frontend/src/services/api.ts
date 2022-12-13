import axios from "axios";

import { useAuthStore } from "@/stores/auth";
import { LoginResponseDto } from "@/models/LoginResponseDto";
import type { LoginRequestDto } from "@/models/LoginRequestDto";

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

export async function login(loginRequest: LoginRequestDto): Promise<LoginResponseDto> {
    try {
        const res =  await http.post("/token", loginRequest);
        const data = new LoginResponseDto(res.data);
        authStore.login(data.token);
        console.log("Logged in", res.data);
        return data;
    } catch (error) {
        // TODO: Custom error handling
        throw error;
    }
}
