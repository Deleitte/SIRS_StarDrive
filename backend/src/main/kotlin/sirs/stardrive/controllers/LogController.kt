package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.LogService

@RestController
class LogController(val logService: LogService) {
    @GetMapping("/logs")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun getLogs() = logService.getLogs()
}