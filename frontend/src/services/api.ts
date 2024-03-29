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
import { PrivateDataDto } from "@/models/PrivateDataDto";
import { EmployeeDto } from "@/models/EmployeeDto";
import { EmployeeWorkingShiftsDto } from "@/models/EmployeeWorkingShiftsDto";
import type { NewSensorDto } from "@/models/NewSensorDto";
import type { NewActuatorDto } from "@/models/NewActuatorDto";
import { UserDto } from "@/models/UserDto";
import type { AssignEmployeeToTeamDto } from "@/models/AssignEmployeeToTeamDto";
import type { NewTeamDto } from "@/models/NewTeamDto";
import { PartDto } from "@/models/PartDto";
import { LogDto } from "@/models/LogDto";
import type { NewPartDto } from "@/models/NewPartDto";
import { RegisterResponseDto } from "@/models/RegisterResponseDto";
import type { NewEngineerDto } from "@/models/NewEngineerDto";
import type { TotpDto } from "@/models/TotpDto";
import ExpenseDto from "@/models/ExpenseDto";
import type { NewExternalUserDto } from "@/models/NewExternalUserDto";

const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
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
    const res = await http.post("/auth/token", loginRequest);
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

export async function totp(totpDto: TotpDto) {
  try {
    const res = await http.post("/auth/token/2fa", totpDto);
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

export async function register(
  registerRequest: RegisterRequestDto
): Promise<RegisterResponseDto> {
  try {
    const res = await http.post("/auth/register", registerRequest);
    const data = new RegisterResponseDto(res.data);
    const { setToken } = useAuthStore();
    setToken(data.token);
    return data;
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function refreshToken() {
  const res = await http.post("/auth/token/refresh");
  const data = new LoginResponseDto(res.data);
  const { setToken } = useAuthStore();
  setToken(data.token);
}

export async function logout() {
  await http.post("/auth/token/revoke");
  const { setToken } = useAuthStore();
  setToken("");
}

export async function changePassword(changePasswordDto: ChangePasswordDto) {
  try {
    await http.post("/auth/changepassword", changePasswordDto);
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

export async function turnOnActuator(actuator: ActuatorDto) {
  try {
    await http.patch(`actuators/${actuator.name}/on`);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function turnOffActuator(actuator: ActuatorDto) {
  try {
    await http.patch(`actuators/${actuator.name}/off`);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function updateActuatorPingInterval(
  actuator: ActuatorDto,
  pingInterval: number
) {
  try {
    await http.patch(`actuators/${actuator.name}/pingInterval`, {
      pingInterval,
    });
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

export async function getPrivateData(): Promise<PrivateDataDto> {
  try {
    const res = await http.get("/employees/private");
    return new PrivateDataDto(res.data);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getEmployeesWorkingShifts(): Promise<
  EmployeeWorkingShiftsDto[]
> {
  try {
    const res = await http.get("/workingshifts");
    return res.data.map((e: any) => new EmployeeWorkingShiftsDto(e));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getEmployees(): Promise<EmployeeDto[]> {
  try {
    const res = await http.get("/employees");
    return res.data.map((e: any) => new EmployeeDto(e));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createSensor(sensor: NewSensorDto) {
  try {
    await http.post("/sensors", sensor);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createActuator(actuator: NewActuatorDto) {
  try {
    await http.post("/actuators", actuator);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function assignEmployeeToTeam(dto: AssignEmployeeToTeamDto) {
  try {
    await http.post(`/employees`, dto);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getNewUsers(): Promise<UserDto[]> {
  try {
    const res = await http.get("/employees/new");
    return res.data.map((u: any) => new UserDto(u));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createEmployee(assignDto: AssignEmployeeToTeamDto) {
  try {
    await http.post("/employees", assignDto);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getParts(): Promise<PartDto[]> {
  try {
    const res = await http.get("/parts");
    return res.data.map((p: any) => new PartDto(p));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createPart(part: NewPartDto) {
  try {
    await http.post("/parts", part);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function buyPart(
  part: PartDto,
  quantity: number
): Promise<PartDto> {
  try {
    const res = await http.patch(`/parts/${part.ref}`, { quantity });
    return new PartDto(res.data);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createTeam(team: NewTeamDto) {
  try {
    await http.post("/teams", team);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getLogs(): Promise<LogDto[]> {
  try {
    const res = await http.get("/logs");
    return res.data.map((l: any) => new LogDto(l));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createEngineer(
  engineer: NewEngineerDto
): Promise<UserDto> {
  try {
    const res = await http.post("/engineers", engineer);
    return new UserDto(res.data);
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function getExpenses(): Promise<ExpenseDto[]> {
  try {
    const res = await http.get("/external/expenses");
    return res.data.map((dto: any) => new ExpenseDto(dto));
  } catch (error) {
    throw new StarDriveError(
      await errorMessage(error as AxiosError),
      // @ts-ignore
      error.response.data.code
    );
  }
}

export async function createExternalUser(
  externalUser: NewExternalUserDto
): Promise<UserDto> {
  try {
    const res = await http.post("/externalUsers", externalUser);
    return new UserDto(res.data);
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
