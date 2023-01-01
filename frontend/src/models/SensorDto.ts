export class SensorDto {
  name!: string;
  value!: number;

  constructor(jsonObj?: SensorDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      this.value = jsonObj.value;
    }
  }
}
