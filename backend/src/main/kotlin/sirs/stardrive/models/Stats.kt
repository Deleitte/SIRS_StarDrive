package sirs.stardrive.models

data class StatsDto(
    val totalSensors: Int,
    val totalActuators: Int,
    val energyConsumption: Int,
    val carsInProduction: Int,
    val maxEnergyConsumption: Int = 100,
    val maxCarsInProduction: Int = 20,
)