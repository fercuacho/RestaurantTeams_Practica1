package com.example.restaurantteamsdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantteamsdb.R
import com.example.restaurantteamsdb.data.db.model.TeamEntity
import com.example.restaurantteamsdb.databinding.TeamElementBinding

class TeamAdapter(private val onTeamClick: (TeamEntity) -> Unit): RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    private var teams:List<TeamEntity> = emptyList()

    class ViewHolder(private val binding: TeamElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        fun bind(team: TeamEntity){
//            binding.tvTitle.text = team.title
//            binding.tvGenre.text = team.genre
//            binding.tvDeveloper.text = team.developer

            binding.apply {
                tvTitle.text = team.name
                tvGenre.text = team.manager
                tvDeveloper.text = team.country

                val imagenId = when (team.country) {
                    "Brazil" -> R.drawable.brazil
                    "Canada" -> R.drawable.canada
                    "Suecia" -> R.drawable.sweden
                    "Italia" -> R.drawable.italy
                    "Korea del Sur" -> R.drawable.south_orea
                    "India" -> R.drawable.india
                    else -> R.drawable.rainbow // Puedes agregar una imagen predeterminada en caso de un valor inesperado
                }
                ivIcon.setImageResource(imagenId)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding = TeamElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = teams.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(teams[position])

        holder.itemView.setOnClickListener{
            //Aquí va el click del elemento
            onTeamClick(teams[position])
        }

        holder.ivIcon.setOnClickListener{
            //Click para la vista del imageView con el icono
        }

    }

    fun updateList(list: List<TeamEntity>){
        teams = list
        notifyDataSetChanged()
    }

}