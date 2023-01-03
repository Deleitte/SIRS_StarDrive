package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.LogService

@RestController
class LogController(val logService: LogService) {
    @GetMapping("/logs")
    fun getLogs() = logService.getLogs()
}