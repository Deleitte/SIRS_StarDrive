package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.ExternalService

@RestController
class ExternalController(val externalService: ExternalService) {

    @GetMapping("/external/expenses")
    fun getExpenses() = externalService.getExpenses()
}