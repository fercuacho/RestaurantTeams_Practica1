package com.example.restaurantteamsdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantteamsdb.R
import com.example.restaurantteamsdb.application.RestaurantTeamsDBApp
import com.example.restaurantteamsdb.data.TeamRepository
import com.example.restaurantteamsdb.data.db.model.TeamEntity
import com.example.restaurantteamsdb.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var teams: List<TeamEntity> = emptyList()
    private lateinit var repository: TeamRepository

    private lateinit var teamAdapter: TeamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as RestaurantTeamsDBApp).repository

        teamAdapter = TeamAdapter(){ team ->
            teamClicked(team)

        }

//        binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
//        binding.rvGames.adapter = gameAdapter

        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = teamAdapter
        }

        updateUI()

    }


    private fun updateUI(){
        lifecycleScope.launch {
            teams =repository.getAllTeams()

            if (teams.isNotEmpty()){
                //Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            teamAdapter.updateList(teams)
        }
    }

    fun click(view: View) {
        val dialog = TeamDialog( updateUI = {
            updateUI()
        }, message = {
            message(it)
        })
        dialog.show(supportFragmentManager, getString(R.string.dialog_tag))
    }

    private fun teamClicked(team: TeamEntity){
//        Toast.makeText(this, "click en el juego: ${game.title}", Toast.LENGTH_SHORT).show()
        val dialog = TeamDialog(newTeam = false, team = team, updateUI = {
            updateUI()
        }, message = {
            message(it)
        })
        dialog.show(supportFragmentManager, getString(R.string.dialog_tag))
    }

    private fun message (text: String) {
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor(getString(R.string.color_message)))
            .setBackgroundTint(Color.parseColor(getString(R.string.background_message_color)))
            .show()
    }

}