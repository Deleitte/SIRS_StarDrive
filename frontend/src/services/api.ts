import axios, { AxiosError } from "axios";

import { useAuthStore } from "@/stores/auth";
import { LoginResponseDto } from "@/models/LoginResponseDto";
import type { LoginRequestDto } from "@/models/LoginRequestDto";
import { StarDriveError } from "@/models/StarDriveError";
import type { RegisterRequestDto } from "@/models/RegisterRequestDto";
import { TeamDto } from "@/models/TeamDto";
import {SensorDto} from "@/models/SensorDto";
import {ActuatorDto} from "@/models/ActuatorDto";
import {StatsDto} from "@/models/StatsDto";

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

export async function login(loginRequest: LoginRequestDto) {
    try {
        const res =  await http.post("/token", loginRequest);
        const data = new LoginResponseDto(res.data);
        authStore.setToken(data.token);
    } catch (error) {
        throw new StarDriveError(
          await errorMessage(error as AxiosError),
          // @ts-ignore
          error.response.data.code,
        );
    }
}

export async function register(registerRequest: RegisterRequestDto) {
    try {
        const res = await http.post("/register", registerRequest);
        const data = new LoginResponseDto(res.data);
        authStore.setToken(data.token);
    } catch (error) {
        throw new StarDriveError(
          await errorMessage(error as AxiosError),
          // @ts-ignore
          error.response.data.code,
        );
    }
}

export async function getTeams(): Promise<TeamDto[]> {
    try {
        const res = await http.get("/teams");
        return res.data.map((t: any) => new TeamDto(t));
    } catch (error) {
        throw new StarDriveError(
          await errorMessage(error as AxiosError),
          // @ts-ignore
          error.response.data.code,
        );
    }
}

export async function getSensors(): Promise<SensorDto[]> {
    try {
        const res = await http.get("/sensors");
        return res.data.map((s: any) => new SensorDto(s));
    } catch (error) {
        throw new StarDriveError(
            await errorMessage(error as AxiosError),
            // @ts-ignore
            error.response.data.code,
        );
    }
}

export async function getActuators() : Promise<ActuatorDto[]> {
    try {
        const res = await http.get("/actuators");
        return res.data.map((a: any) => new ActuatorDto(a));
    } catch (error) {
        throw new StarDriveError(
            await errorMessage(error as AxiosError),
            // @ts-ignore
            error.response.data.code,
        );
    }
}

export async function getStats(): Promise<StatsDto> {
    try {
        const res = await http.get("/stats");
        return new StatsDto(res.data);
    } catch (error) {
        throw new StarDriveError(await errorMessage(error as AxiosError),
            error.response.data.code,
        );
    }
}

async function errorMessage(error: AxiosError) {
    // @ts-ignore
    if (error.response.data.message.includes("Credenciais")) {
        return "Wrong password";
    } else if (error.response) {
        // @ts-ignore
        return error.response.data.message;
    } else {
        return `Unknown error: ${error.message}`;
    }
}
