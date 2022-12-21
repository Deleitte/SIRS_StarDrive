package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.*
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

    @PatchMapping("/teams/salary/{teamName}")
    fun changeTeamSalary(@PathVariable teamName: String, @RequestBody newSalary: UpdateTeamSalaryDto): TeamDto =
        teamService.changeTeamSalary(teamName, newSalary.salary)
}