package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.StatsService

@RestController
class StatsController(val statsService: StatsService) {
    @GetMapping("/stats")
    fun getStats() = statsService.getStats()
}