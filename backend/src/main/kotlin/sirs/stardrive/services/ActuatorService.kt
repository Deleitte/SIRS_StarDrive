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
class ActuatorService(private val actuatorRepository: ActuatorRepository) {
    fun getActuators() = actuatorRepository.findAll().map { ActuatorDto(it) }

    fun createActuator(newActuatorDto: NewActuatorDto) : ActuatorDto {
        return try {
            ActuatorDto(actuatorRepository.save(Actuator(newActuatorDto)))
        } catch (e: Exception) {
            throw StarDriveException(ErrorMessage.ACTUATOR_ALREADY_EXISTS)
        }
    }

    fun updatePingInterval(actuatorName: String, pingInterval: Int) : ActuatorDto {
        val actuator = actuatorRepository.findByName(actuatorName) ?: throw StarDriveException(ErrorMessage.ACTUATOR_NOT_FOUND)
        actuator.pingInterval = pingInterval
        return ActuatorDto(actuatorRepository.save(actuator))
    }

    fun turnOnActuator(actuatorName: String) : ActuatorDto {
        val actuator = actuatorRepository.findByName(actuatorName) ?: throw StarDriveException(ErrorMessage.ACTUATOR_NOT_FOUND)
        if (!actuator.damaged) {
            actuator.on = true
        }
        return ActuatorDto(actuatorRepository.save(actuator))
    }

    fun turnOffActuator(actuatorName: String) : ActuatorDto {
        val actuator = actuatorRepository.findByName(actuatorName) ?: throw StarDriveException(ErrorMessage.ACTUATOR_NOT_FOUND)
        actuator.on = false
        return ActuatorDto(actuatorRepository.save(actuator))
    }

    fun updateActuatorStatus(actuatorName: String, status: UpdateActuatorStatusDto) : ActuatorDto {
        val actuator = actuatorRepository.findByName(actuatorName) ?: throw StarDriveException(ErrorMessage.ACTUATOR_NOT_FOUND)
        if (verifySignature(actuator, status) && status.timestamp > actuator.timestamp) {
            actuator.timestamp = status.timestamp
            actuator.damaged = status.damaged
            if (status.damaged) {
                actuator.on = false
            }
            return ActuatorDto(actuatorRepository.save(actuator))
        } else {
            throw StarDriveException(ErrorMessage.INVALID_SIGNATURE)
        }

    }

    fun verifySignature(actuator: Actuator, status: UpdateActuatorStatusDto) : Boolean {
        val requestJson = "{\"damaged\": ${status.damaged}, \"timestamp\": ${status.timestamp}}"
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(actuator.publicKey)))
        val signatureRSA: Signature = Signature.getInstance("SHA256withRSA")
        signatureRSA.initVerify(key)
        signatureRSA.update(requestJson.toByteArray())
        return signatureRSA.verify(Base64.getDecoder().decode(status.signature))
    }
}