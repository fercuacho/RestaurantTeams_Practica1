package com.example.restaurantteamsdb.data

import com.example.restaurantteamsdb.data.db.TeamDao
import com.example.restaurantteamsdb.data.db.model.TeamEntity

class TeamRepository(private val teamDao: TeamDao) {

    suspend fun insertTeam(team: TeamEntity){
        teamDao.insertTeam(team)
    }

    suspend fun getAllTeams(): List<TeamEntity> = teamDao.getAllTeams()

    suspend fun updateTeam(team: TeamEntity){
        teamDao.updateTeam(team)
    }

    suspend fun deleteTeam(team: TeamEntity){
        teamDao.deleteTeam(team)
    }

}