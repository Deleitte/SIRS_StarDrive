package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.NewActuatorDto
import sirs.stardrive.models.UpdateActuatorStatusDto
import sirs.stardrive.models.UpdatePingIntervalDto
import sirs.stardrive.services.ActuatorService

@RestController
class ActuatorController(val actuatorService: ActuatorService) {
    @GetMapping("/actuators")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun getActuators() = actuatorService.getActuators()

    @PostMapping("/actuators")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun createActuator(@RequestBody newActuatorDto: NewActuatorDto) = actuatorService.createActuator(newActuatorDto)

    @PatchMapping("/actuators/{actuatorName}/pingInterval")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun updatePingInterval(
        @PathVariable actuatorName: String,
        @RequestBody newInterval: UpdatePingIntervalDto
    ) = actuatorService.updatePingInterval(actuatorName, newInterval.pingInterval)

    @PatchMapping("/actuators/{actuatorName}")
    fun updateActuatorStatus(@PathVariable actuatorName: String, @RequestBody newStatus: UpdateActuatorStatusDto) =
        actuatorService.updateActuatorStatus(actuatorName, newStatus)

    @PatchMapping("/actuators/{actuatorName}/on")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun turnOnActuator(@PathVariable actuatorName: String) = actuatorService.turnOnActuator(actuatorName)

    @PatchMapping("/actuators/{actuatorName}/off")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun turnOffActuator(@PathVariable actuatorName: String) = actuatorService.turnOffActuator(actuatorName)

}