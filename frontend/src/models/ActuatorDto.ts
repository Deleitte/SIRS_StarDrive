export class ActuatorDto {
  name!: string;
  interval!: number;
  on!: boolean;
  damaged!: boolean;

  constructor(jsonObj?: ActuatorDto) {
    if (jsonObj) {
      this.name = jsonObj.name;
      // @ts-ignore
      this.interval = jsonObj.pingInterval;
      this.on = jsonObj.on;
      this.damaged = jsonObj.damaged;
    }
  }
}
