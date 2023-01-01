export class PrivateDataDto {
    absentDays!: Number;
    parentalLeaveDays!: Number;
    salary!: Number;

    constructor(jsonObj?: PrivateDataDto) {
        if (jsonObj) {
            this.absentDays = jsonObj.absentWorkingDays;
            this.parentalLeaveDays = jsonObj.parentalLeaves;
            this.salary = jsonObj.salary;
        }
    }
}