package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.NewSensorDto
import sirs.stardrive.models.UpdateSensorValueDto
import sirs.stardrive.services.SensorService

@RestController
class SensorController(val sensorService: SensorService) {
    @GetMapping("/sensors")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun getSensors() = sensorService.getSensors()

    @PostMapping("/sensors")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun createSensor(@RequestBody newSensorDto: NewSensorDto) = sensorService.createSensor(newSensorDto)

    @GetMapping("/sensors/{sensorName}/read")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun readSensorValue(@PathVariable sensorName: String) = sensorService.readSensorValue(sensorName)

    @PatchMapping("/sensors/{sensorName}/update")
    fun updateSensorValue(@PathVariable sensorName: String, @RequestBody newValue: UpdateSensorValueDto) =
        sensorService.updateSensorValue(sensorName, newValue)

}