export class UserDto {
    name!: string;
    username!: string;
    role!: string;

    constructor(jsonObj?: UserDto) {
        if (jsonObj) {
            this.name = jsonObj.name;
            this.username = jsonObj.username;
            this.role = jsonObj.role;
        }
    }
}
