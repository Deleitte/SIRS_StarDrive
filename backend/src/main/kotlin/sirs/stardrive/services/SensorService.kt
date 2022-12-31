package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.*
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

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

    fun updateSensorValue(sensorName: String, valueDto: UpdateSensorValueDto) : SensorDto {
        val sensor = sensorRepository.findByName(sensorName) ?: throw StarDriveException(ErrorMessage.SENSOR_NOT_FOUND)
        if (verifySignature(sensor, valueDto) && valueDto.timestamp > sensor.timestamp) {
            sensor.timestamp = valueDto.timestamp
            sensor.value = valueDto.value
            return SensorDto(sensorRepository.save(sensor))
        } else {
            throw StarDriveException(ErrorMessage.INVALID_SIGNATURE)
        }
    }

    fun verifySignature(sensor: Sensor, valueDto: UpdateSensorValueDto) : Boolean {
        val requestJson = "{\"value\": " + valueDto.value + ", \"timestamp\": " + valueDto.timestamp + "}"
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(sensor.publicKey)))
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(key)
        signature.update(requestJson.toByteArray())
        return signature.verify(Base64.getDecoder().decode(valueDto.signature))
    }
}