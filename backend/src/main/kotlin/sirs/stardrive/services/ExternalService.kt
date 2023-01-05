package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.models.ExpenseDto

@Service
class ExternalService(private val teamService: TeamService) {

    fun getExpenses() = teamService.getTeams().map {
        ExpenseDto(it.name, it.employees.count() * it.salary)
    }
}