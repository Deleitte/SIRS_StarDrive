package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.*
import sirs.stardrive.services.TeamService

@RestController
class TeamController(private val teamService: TeamService) {
    @GetMapping("/teams")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun getTeams(): List<TeamDto> = teamService.getTeams()

    @PostMapping("/teams")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun createTeam(@RequestBody newTeamDto: NewTeamDto): TeamDto = teamService.createTeam(newTeamDto)

    @PostMapping("/employees")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun createEmployee(@RequestBody newEmployeeDto: NewEmployeeDto): EmployeeDto =
        teamService.createEmployee(newEmployeeDto)

    @PatchMapping("/teams/salary/{teamName}")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun changeTeamSalary(@PathVariable teamName: String, @RequestBody newSalary: UpdateTeamSalaryDto): TeamDto =
        teamService.changeTeamSalary(teamName, newSalary.salary)

    @GetMapping("/employees/private")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_EMPLOYEE')")
    fun getPrivateEmployeeInfo(): EmployeePrivateDataDto = teamService.getEmployeePrivateInfo();

    @PatchMapping("/workingshifts/{employeeName}")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun addWorkingShift(@PathVariable employeeName: String, @RequestBody newWorkingShift: WorkingShiftDto): EmployeeWorkingShiftsDto =
        teamService.addWorkingShift(employeeName, newWorkingShift)

    @GetMapping("/workingshifts")
    @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_ENGINEER') || hasAuthority('SCOPE_EMPLOYEE')) || hasAuthority('SCOPE_ADMIN')")
    fun getWorkingShifts(): List<EmployeeWorkingShiftsDto> = teamService.getWorkingShifts()

    @GetMapping("/employees")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ENGINEER')")
    fun getEmployees(): List<EmployeeDto> = teamService.getEmployees()

    @GetMapping("/employees/new")
    @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_ENGINEER') || hasAuthority('SCOPE_ADMIN'))")
    fun getNewUsers(): List<UserDto> = teamService.getNewUsers()

    @PostMapping("/engineers")
    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ADMIN')")
    fun createEngineer(@RequestBody newEngineerDto: NewEngineerDto): UserDto =
        teamService.createEngineer(newEngineerDto)
}