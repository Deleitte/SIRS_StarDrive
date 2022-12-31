package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.*
import java.security.KeyFactory
import java.security.SecureRandom
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*
import javax.crypto.Cipher

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
        if (status.challenge == actuator.challenge + 1) {
            actuator.damaged = status.damaged
            if (status.damaged) {
                actuator.on = false
            }
            actuator.challenge = SecureRandom().nextInt()
            return ActuatorDto(actuatorRepository.save(actuator))
        } else {
            throw StarDriveException(ErrorMessage.INVALID_CHALLENGE)
        }
    }

    fun getChallenge(actuatorName: String) : ChallengeDto {
        val actuator = actuatorRepository.findByName(actuatorName) ?: throw StarDriveException(ErrorMessage.ACTUATOR_NOT_FOUND)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = RSAPublicKeySpec(actuator.modulus, actuator.exponent)
        val key = keyFactory.generatePublic(keySpec) as RSAPublicKey
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherData: ByteArray = cipher.doFinal(actuator.challenge.toString().toByteArray())

        return ChallengeDto(Base64.getEncoder().encodeToString(cipherData))
    }
}