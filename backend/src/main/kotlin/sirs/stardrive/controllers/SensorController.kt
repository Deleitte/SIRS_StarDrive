package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.NewSensorDto
import sirs.stardrive.models.UpdateSensorValueDto
import sirs.stardrive.services.SensorService

@RestController
class SensorController(val sensorService: SensorService) {
    @GetMapping("/sensors")
    fun getSensors() = sensorService.getSensors()

    @PostMapping("/sensors")
    fun createSensor(@RequestBody newSensorDto: NewSensorDto) = sensorService.createSensor(newSensorDto)

    @GetMapping("/sensors/{sensorName}/read")
    fun readSensorValue(@PathVariable sensorName: String) = sensorService.readSensorValue(sensorName)

    @PatchMapping("/sensors/{sensorName}/update")
    fun updateSensorValue(@PathVariable sensorName: String, @RequestBody newValue: UpdateSensorValueDto) =
        sensorService.updateSensorValue(sensorName, newValue.value)

}