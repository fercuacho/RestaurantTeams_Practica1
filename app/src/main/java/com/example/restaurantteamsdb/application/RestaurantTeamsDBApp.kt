package com.example.restaurantteamsdb.application

import android.app.Application
import com.example.restaurantteamsdb.data.TeamRepository
import com.example.restaurantteamsdb.data.db.TeamDatabase

class RestaurantTeamsDBApp(): Application() {

    private val database by lazy {
        TeamDatabase.getDatabase(this@RestaurantTeamsDBApp)
    }

    val repository by lazy {
        TeamRepository(database.teamDao())
    }
}