package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.models.ActuatorRepository
import sirs.stardrive.models.SensorRepository
import sirs.stardrive.models.StatsDto
import kotlin.random.Random

@Service
class StatsService(val sensorRepository: SensorRepository, val actuatorRepository: ActuatorRepository) {
    fun getStats() = StatsDto(
        sensorRepository.count().toInt(),
        actuatorRepository.count().toInt(),
        Random.nextInt(80, 100),
        Random.nextInt(10, 20)
    )
}