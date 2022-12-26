package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.*

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
        actuator.damaged = status.damaged
        if (status.damaged) {
            actuator.on = false
        }
        return ActuatorDto(actuatorRepository.save(actuator))
    }
}