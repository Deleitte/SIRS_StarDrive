import axios, { AxiosError } from "axios";

import { useAuthStore } from "@/stores/auth";
import { LoginResponseDto } from "@/models/LoginResponseDto";
import type { LoginRequestDto } from "@/models/LoginRequestDto";
import { StarDriveError } from "@/models/StarDriveError";
import type { RegisterRequestDto } from "@/models/RegisterRequestDto";
import { TeamDto } from "@/models/TeamDto";
import { SensorDto } from "@/models/SensorDto";
import { ActuatorDto } from "@/models/ActuatorDto";
import { StatsDto } from "@/models/StatsDto";
import type { ChangePasswordDto } from "@/models/ChangePasswordDto";

const http = axios.create({
  baseURL: "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});

http.interceptors.request.use(
  (config) => {
    if (config.headers && !config.headers.Authorization) {
      const { token } = useAuthStore();
      if (token) config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

http.interceptors.response.use(
  (res) => res,
  async (error) => {
    const originalConfig = error.config;

    if (originalConfig.url !== "/auth/signin" && error.response) {
      // Access Token was expired
      if (error.response.status === 401 && !originalConfig._retry) {
        originalConfig._retry = true;

        try {
          const { setToken } = useAuthStore();
          setToken("");
          originalConfig.headers.Authorization = undefined;
          await refreshToken();

          return http(originalConfig);
        } catch (_error) {
          return Promise.reject(_error);
        }
      }
    }

    return Promise.reject(error);
  }
);

export async function login(loginRequest: LoginRequestDto) {
  try {
    const res = await http.post("/token", loginRequest);
    const data = new LoginResponseDto(res.data);
    const { setToken } = useAuthStore();
    setToken(data.token);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function register(registerRequest: RegisterRequestDto) {
  try {
    const res = await http.post("/register", registerRequest);
    const data = new LoginResponseDto(res.data);
    const { setToken } = useAuthStore();
    setToken(data.token);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function refreshToken() {
  const res = await http.post("/token/refresh");
  const data = new LoginResponseDto(res.data);
  const { setToken } = useAuthStore();
  setToken(data.token);
}

export async function changePassword(changePasswordDto: ChangePasswordDto) {
  try {
    await http.post("/changepassword", changePasswordDto);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
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
      error.response.data.code
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
      error.response.data.code
    );
  }
}

export async function getActuators(): Promise<ActuatorDto[]> {
  try {
    const res = await http.get("/actuators");
    return res.data.map((a: any) => new ActuatorDto(a));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getStats(): Promise<StatsDto> {
  try {
    const res = await http.get("/stats");
    return new StatsDto(res.data);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
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
