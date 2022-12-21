import { EmployeeDto } from "@/models/EmployeeDto";

export class TeamDto {
  name!: string;
  salary!: number;
  employees!: EmployeeDto[];

  constructor(jsonObj?: TeamDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.salary = jsonObj.salary;
      this.employees = jsonObj.employees.map((e) => new EmployeeDto(e));
    }
  }
}