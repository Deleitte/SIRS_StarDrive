import {EmployeeDto} from "@/models/EmployeeDto";
import type {WorkingShiftDto} from "@/models/WorkingShiftDto";

export class EmployeeWorkingShiftsDto {
  name!: String;
  workingShifts!: WorkingShiftDto[];

  constructor(jsonObj?: EmployeeWorkingShiftsDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.workingShifts = jsonObj.workingShifts;
    }
  }
}