package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.models.EmployeeDto
import sirs.stardrive.models.NewEmployeeDto
import sirs.stardrive.models.NewTeamDto
import sirs.stardrive.models.TeamDto
import sirs.stardrive.services.TeamService

@RestController
class TeamController(private val teamService: TeamService) {
    @GetMapping("/teams")
    fun getTeams(): List<TeamDto> = teamService.getTeams()

    @PostMapping("/teams")
    fun createTeam(@RequestBody newTeamDto: NewTeamDto): TeamDto = teamService.createTeam(newTeamDto)

    @PostMapping("/employees")
    fun createEmployee(@RequestBody newEmployeeDto: NewEmployeeDto): EmployeeDto =
        teamService.createEmployee(newEmployeeDto)
}