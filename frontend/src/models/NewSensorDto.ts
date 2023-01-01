export class NewSensorDto {
  name!: string;
  key!: string;

  constructor(name: string, key: string) {
    this.name = name;
    this.key = key;
  }
}
