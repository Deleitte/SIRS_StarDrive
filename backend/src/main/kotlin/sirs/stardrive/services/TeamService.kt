package sirs.stardrive.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
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
            userRepository.save(user)
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

    fun getEmployeePrivateInfo(): EmployeePrivateDataDto {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt;
        val user = userRepository.findByUsername(jwt.subject)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, jwt.subject)
        val employee = employeeRepository.findByUser(user)
            ?: throw StarDriveException(ErrorMessage.EMPLOYEE_NOT_FOUND, jwt.subject)

        return EmployeePrivateDataDto(employee)
    }

    fun addWorkingShift(employeeName: String, workingShiftDto: WorkingShiftDto): EmployeeWorkingShiftsDto {
        val user = userRepository.findByUsername(employeeName)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, employeeName)
        val employee = employeeRepository.findByUser(user)
            ?: throw StarDriveException(ErrorMessage.EMPLOYEE_NOT_FOUND, employeeName)
        val workingShift = WorkingShift(workingShiftDto)
        employee.workingShifts.add(workingShift)
        return EmployeeWorkingShiftsDto(employeeRepository.save(employee))
    }

    fun getWorkingShifts(): List<EmployeeWorkingShiftsDto> {
        return employeeRepository.findAll().map { EmployeeWorkingShiftsDto(it) }
    }

    fun getNewUsers(): List<UserDto> {
        return userRepository.findAll().filter { it.role == Role.NEW }.map { UserDto(it) }
    }

    fun createEngineer(new: NewEngineerDto ): UserDto {
        val user = userRepository.findByUsername(new.username)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, new.username)
        user.role = Role.ENGINEER
        return UserDto(userRepository.save(user))
    }

    fun createExternalUser(new: NewExternalUserDto): UserDto {
        val user = userRepository.findByUsername(new.username)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, new.username)
        user.role = Role.EXTERNAL
        return UserDto(userRepository.save(user))
    }
}