package com.example.restaurantteamsdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurantteamsdb.data.db.model.TeamEntity
import com.example.restaurantteamsdb.util.Constants

@Database(
    entities = [TeamEntity::class],
    version = 1, //veraion de la DB. Importante para las migraciones
    exportSchema = true // por defecto es true
)
abstract class TeamDatabase: RoomDatabase() {
    //Aquí va el DAO
    abstract fun teamDao(): TeamDao

    //Sin inyección de dependencias, metemos la creación de la bd con un singleton aquí
    companion object{
        @Volatile //lo que se escriba en este campo, será inmediatamente visible a otros hilos
        private var INSTANCE: TeamDatabase? = null

        fun getDatabase(context: Context): TeamDatabase{
            return INSTANCE?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                // si es nula, entonces se crea la base de datos (patrón singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TeamDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}