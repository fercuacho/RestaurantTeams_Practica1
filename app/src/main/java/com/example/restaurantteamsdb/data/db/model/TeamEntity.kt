package com.example.restaurantteamsdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restaurantteamsdb.util.Constants

@Entity(tableName = Constants.DATABASE_TEAM_TABLE)
data class TeamEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "team_id")
    val id: Long = 0,
    @ColumnInfo(name = "team_name")
    var name: String,

    @ColumnInfo(name = "team_manager")
    var manager: String,

    @ColumnInfo(name = "team_country")
    var country: String
)