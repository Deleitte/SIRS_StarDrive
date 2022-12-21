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
    var absentWorkingDays : Int,
    var parentalLeaves : Int,
    @MongoId val id: ObjectId? = null
)

data class NewEmployeeDto(
    val username: String,
    val team: String,
    val absentWorkingDays: Int,
    val parentalLeaves: Int
    )

data class EmployeeDto(val name: String, val username: String, val absentWorkingDays: Int, val parentalLeaves: Int) {
    constructor(employee: Employee) : this(employee.user.name, employee.user.username, employee.absentWorkingDays, employee.parentalLeaves)
}

@Repository
interface EmployeeRepository : MongoRepository<Employee, ObjectId> {
    fun findByUser(user: User): Employee?
    fun findByTeam(team: Team): List<Employee>
}
