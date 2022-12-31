import {WorkingShiftDto} from "@/models/WorkingShiftDto";

export class EmployeeDto {
  name!: string;
  username!: string;
  workingShifts!: WorkingShiftDto[];

  constructor(jsonObj?: EmployeeDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.username = jsonObj.username;
        this.workingShifts = jsonObj.workingShifts;
    }
  }
}
