package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.*

@Service
class TeamService(
    private val teamRepository: TeamRepository,
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository
) {
    fun getTeams(): List<TeamDto> = teamRepository.findAll().map { TeamDto(it) }

    fun createTeam(newTeamDto: NewTeamDto): TeamDto = TeamDto(teamRepository.save(Team(newTeamDto)))

    fun getEmployees(): List<EmployeeDto> = employeeRepository.findAll().map { EmployeeDto(it) }

    fun createEmployee(newEmployeeDto: NewEmployeeDto): EmployeeDto {
        val team = teamRepository.findByName(newEmployeeDto.team)
            ?: throw StarDriveException(ErrorMessage.TEAM_NOT_FOUND)
        val user = userRepository.findByUsername(newEmployeeDto.username)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)

        return try {
            val employee = employeeRepository.save(Employee(user))
            team.employees.add(employee)
            teamRepository.save(team)
            EmployeeDto(employee)
        } catch (e: Exception) {
            throw StarDriveException(ErrorMessage.EMPLOYEE_ALREADY_EXISTS)
        }
    }
}