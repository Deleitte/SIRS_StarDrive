export class PrivateDataDto {
    absentWorkingDays!: Number;
    parentalLeaves!: Number;
    salary!: Number;

    constructor(jsonObj?: PrivateDataDto) {
        if (jsonObj) {
            this.absentWorkingDays = jsonObj.absentWorkingDays;
            this.parentalLeaves = jsonObj.parentalLeaves;
            this.salary = jsonObj.salary;
        }
    }
}