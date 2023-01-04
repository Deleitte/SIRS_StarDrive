package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document
data class Team(
    @Indexed(unique = true) val name: String,
    var salary: Int,
    @MongoId val id: ObjectId? = null
) {
    constructor(newTeamDto: NewTeamDto) : this(newTeamDto.name, newTeamDto.salary)
}

data class NewTeamDto(val name: String, val salary: Int)

data class TeamDto(val name: String, val salary: Int, val employees: List<EmployeeDto>) {
    constructor(team: Team, employees: List<Employee>) : this(team.name, team.salary, employees.map { EmployeeDto(it) })
}
data class UpdateTeamSalaryDto(val salary: Int)

@Repository
interface TeamRepository : MongoRepository<Team, ObjectId> {
    fun findByName(name: String): Team?
}

@Document
data class Employee(
    @DocumentReference val user: User,
    @DocumentReference var team: Team,
    var absentWorkingDays : Int = 0,
    var parentalLeaves : Int = 0,
    var workingShifts : MutableList<WorkingShift> = mutableListOf(),
    @MongoId val id: ObjectId? = null
)

data class NewEmployeeDto(
    val username: String,
    val team: String
)

data class EmployeeDto(val name: String, val username: String, val team: String, val salary: Int, val absentWorkingDays: Int, val parentalLeaves: Int) {
    constructor(employee: Employee) : this(employee.user.name, employee.user.username, employee.team.name, employee.team.salary, employee.absentWorkingDays, employee.parentalLeaves)
}

data class EmployeePrivateDataDto(val salary: Int, val absentWorkingDays: Int, val parentalLeaves: Int) {
    constructor(employee: Employee) : this(employee.team.salary, employee.absentWorkingDays, employee.parentalLeaves)
}

@Repository
interface EmployeeRepository : MongoRepository<Employee, ObjectId> {
    fun findByUser(user: User): Employee?
    fun findByTeam(team: Team): List<Employee>
}

data class WorkingShift(
    val weekDay: String,
    val startTime: String,
    val endTime: String
) {
    constructor(workingShiftDto: WorkingShiftDto) : this(workingShiftDto.weekDay, workingShiftDto.startTime, workingShiftDto.endTime)
}

data class WorkingShiftDto(
    val weekDay: String,
    val startTime: String,
    val endTime: String
)

data class EmployeeWorkingShiftsDto(val name: String, val workingShifts: List<WorkingShift>) {
    constructor(employee: Employee) : this(employee.user.name, employee.workingShifts)
}

data class NewEngineerDto(val username: String)