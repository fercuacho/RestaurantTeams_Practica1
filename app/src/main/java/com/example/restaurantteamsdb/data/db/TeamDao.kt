package com.example.restaurantteamsdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.restaurantteamsdb.data.db.model.TeamEntity
import com.example.restaurantteamsdb.util.Constants.DATABASE_TEAM_TABLE

@Dao
interface TeamDao {
    //Create
    @Insert
    suspend fun insertTeam(game: TeamEntity)

    //Read
    @Query("SELECT * FROM ${DATABASE_TEAM_TABLE}")
    suspend fun getAllTeams(): List<TeamEntity>

    //Update
    @Update
    suspend fun updateTeam(game: TeamEntity)

    //Delete
    @Delete
    suspend fun deleteTeam(game: TeamEntity)
}