package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.StatsService

@RestController
class StatsController(val statsService: StatsService) {
    @GetMapping("/stats")
    @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_ENGINEER') || hasAuthority('SCOPE_EMPLOYEE') || hasAuthority('SCOPE_ADMIN'))")
    fun getStats() = statsService.getStats()
}