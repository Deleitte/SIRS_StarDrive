export class StatsDto {
    sensors!: number;
    actuators!: number;
    energyConsumption!: number;
    carsInProduction!: number;
    maxEnergyConsumption!: number;
    maxCarsInProduction!: number;

    constructor(jsonObj?: StatsDto) {
        if (jsonObj) {
            this.sensors = jsonObj.totalSensors;
            this.actuators = jsonObj.totalActuators;
            this.energyConsumption = jsonObj.energyConsumption;
            this.carsInProduction = jsonObj.carsInProduction;
            this.maxEnergyConsumption = jsonObj.maxEnergyConsumption;
            this.maxCarsInProduction = jsonObj.maxCarsInProduction;
        }
    }
}