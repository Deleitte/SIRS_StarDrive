export class ActuatorDto {
    name!: String;
    interval!: Number;
    on!: Boolean;
    damaged!: Boolean;

    constructor(jsonObj?: ActuatorDto) {
        if (jsonObj) {
            this.name = jsonObj.name;
            this.interval = jsonObj.pingInterval;
            this.on = jsonObj.on;
            this.damaged = jsonObj.damaged;
        }
    }
}