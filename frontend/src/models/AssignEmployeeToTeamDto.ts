export class AssignEmployeeToTeamDto {
    username!: string;
    team!: string;

    constructor(username: string, team: string) {
        this.username = username;
        this.team = team;
    }
}