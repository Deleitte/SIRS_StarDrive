export default class ExpenseDto {
    teamName!: string;
    cost!: number;

    constructor(json?: ExpenseDto) {
        if (json) {
            this.teamName = json.teamName;
            this.cost = json.cost;
        }
    }
}
