import {WorkingShiftDto} from "@/models/WorkingShiftDto";

export class EmployeeDto {
  name!: string;
  username!: string;
  team!: string;
  salary!: number;
  absentWorkingDays!: number;
  parentalLeaves!: number;


  constructor(jsonObj?: EmployeeDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.username = jsonObj.username;
      this.team = jsonObj.team;
      this.salary = jsonObj.salary;
      this.absentWorkingDays = jsonObj.absentWorkingDays;
      this.parentalLeaves = jsonObj.parentalLeaves;
    }
  }
}
