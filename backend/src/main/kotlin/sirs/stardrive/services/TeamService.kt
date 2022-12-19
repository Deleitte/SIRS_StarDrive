package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.models.NewTeamDto
import sirs.stardrive.models.Team
import sirs.stardrive.models.TeamDto
import sirs.stardrive.models.TeamRepository

@Service
class TeamService(private val teamRepository: TeamRepository) {
    fun getTeams(): List<TeamDto> = teamRepository.findAll().map { TeamDto(it) }

    fun createTeam(newTeamDto: NewTeamDto): TeamDto = TeamDto(teamRepository.save(Team(newTeamDto)))
}