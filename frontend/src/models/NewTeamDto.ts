export class NewTeamDto {
    name!: string;
    salary!: number;

    constructor(name: string, salary: number) {
        this.name = name;
        this.salary = salary;
    }
}