export class EmployeeDto {
  name!: string;
  username!: string;

  constructor(jsonObj?: EmployeeDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.username = jsonObj.username;
    }
  }
}
