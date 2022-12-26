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
    fun getTeams(): List<TeamDto> = teamRepository.findAll().map { TeamDto(it, employeeRepository.findByTeam(it)) }

    fun createTeam(newTeamDto: NewTeamDto): TeamDto = TeamDto(teamRepository.save(Team(newTeamDto)), emptyList())

    fun getEmployees(): List<EmployeeDto> = employeeRepository.findAll().map { EmployeeDto(it) }

    fun createEmployee(newEmployeeDto: NewEmployeeDto): EmployeeDto {
        val team = teamRepository.findByName(newEmployeeDto.team)
            ?: throw StarDriveException(ErrorMessage.TEAM_NOT_FOUND)
        val user = userRepository.findByUsername(newEmployeeDto.username)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)

        return try {
            var employee = employeeRepository.findByUser(user) ?: Employee(
                user,
                team
            )
            employee.id?.let {
                employee.team = team
            }

            employee = employeeRepository.save(employee)
            user.role = Role.EMPLOYEE
            EmployeeDto(employee)
        } catch (e: Exception) {
            throw StarDriveException(ErrorMessage.EMPLOYEE_ALREADY_EXISTS)
        }
    }

    fun changeTeamSalary(teamName: String, newSalary: Int): TeamDto {
        val team = teamRepository.findByName(teamName)
            ?: throw StarDriveException(ErrorMessage.TEAM_NOT_FOUND)
        team.salary = newSalary
        return TeamDto(teamRepository.save(team), employeeRepository.findByTeam(team))
    }
}