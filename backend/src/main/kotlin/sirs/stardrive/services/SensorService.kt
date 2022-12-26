package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.*

@Service
class SensorService(val sensorRepository: SensorRepository) {
    fun getSensors() = sensorRepository.findAll().map { SensorDto(it) }

    fun createSensor(newSensorDto: NewSensorDto) : SensorDto {
        return try {
            SensorDto(sensorRepository.save(Sensor(newSensorDto)))
        } catch (e: Exception) {
            throw StarDriveException(ErrorMessage.SENSOR_ALREADY_EXISTS)
        }
    }

    fun readSensorValue(sensorName: String) : ReadSensorValueDto {
        val sensor = sensorRepository.findByName(sensorName) ?: throw StarDriveException(ErrorMessage.SENSOR_NOT_FOUND)
        return ReadSensorValueDto(sensor.value)
    }

    fun updateSensorValue(sensorName: String, value: Int) : SensorDto {
        val sensor = sensorRepository.findByName(sensorName) ?: throw StarDriveException(ErrorMessage.SENSOR_NOT_FOUND)
        sensor.value = value
        return SensorDto(sensorRepository.save(sensor))
    }
}