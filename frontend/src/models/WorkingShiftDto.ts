export class WorkingShiftDto {
    weekDay!: string;
    startTime!: string;
    endTime!: string;

    constructor(jsonObj?: WorkingShiftDto) {
        if (jsonObj) {
            this.weekDay = jsonObj.weekDay;
            this.startTime = jsonObj.startTime;
            this.endTime = jsonObj.endTime;
        }
    }
}