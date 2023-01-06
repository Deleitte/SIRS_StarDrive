export class StatsDto {
    totalSensors!: number;
    totalActuators!: number;
    energyConsumption!: number;
    carsInProduction!: number;
    maxEnergyConsumption!: number;
    maxCarsInProduction!: number;

    constructor(jsonObj?: StatsDto) {
        if (jsonObj) {
            this.totalSensors = jsonObj.totalSensors;
            this.totalActuators = jsonObj.totalActuators;
            this.energyConsumption = jsonObj.energyConsumption;
            this.carsInProduction = jsonObj.carsInProduction;
            this.maxEnergyConsumption = jsonObj.maxEnergyConsumption;
            this.maxCarsInProduction = jsonObj.maxCarsInProduction;
        }
    }
}