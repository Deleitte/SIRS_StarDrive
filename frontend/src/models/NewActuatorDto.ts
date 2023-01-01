export class NewActuatorDto {
    name!: string;
    pingInterval!: number;
    key!: string;

    constructor(name: string, pingInterval: number, key: string) {
        this.name = name;
        this.pingInterval = pingInterval;
        this.key = key;
    }
}