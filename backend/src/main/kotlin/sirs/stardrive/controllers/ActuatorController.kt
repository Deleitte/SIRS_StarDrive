package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.NewActuatorDto
import sirs.stardrive.models.UpdateActuatorStatusDto
import sirs.stardrive.models.UpdatePingIntervalDto
import sirs.stardrive.services.ActuatorService

@RestController
class ActuatorController(val actuatorService: ActuatorService) {
    @GetMapping("/actuators")
    fun getActuators() = actuatorService.getActuators()

    @PostMapping("/actuators")
    fun createActuator(@RequestBody newActuatorDto: NewActuatorDto) = actuatorService.createActuator(newActuatorDto)

    @PatchMapping("/actuators/{actuatorName}/pingInterval")
    fun updatePingInterval(
        @PathVariable actuatorName: String,
        @RequestBody newInterval: UpdatePingIntervalDto
    ) = actuatorService.updatePingInterval(actuatorName, newInterval.pingInterval)

    @PatchMapping("/actuators/{actuatorName}")
    fun updateActuatorStatus(@PathVariable actuatorName: String, @RequestBody newStatus: UpdateActuatorStatusDto) =
        actuatorService.updateActuatorStatus(actuatorName, newStatus)

    @PatchMapping("/actuators/{actuatorName}/on")
    fun turnOnActuator(@PathVariable actuatorName: String) = actuatorService.turnOnActuator(actuatorName)

    @PatchMapping("/actuators/{actuatorName}/off")
    fun turnOffActuator(@PathVariable actuatorName: String) = actuatorService.turnOffActuator(actuatorName)

    @GetMapping("/actuators/{actuatorName}/challenge")
    fun getChallenge(@PathVariable actuatorName: String) = actuatorService.getChallenge(actuatorName)

}