package com.mycompany.testtask.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mycompany.testtask.models.User

@Database(entities = [User::class], version = 2)
abstract class AppDatabase(): RoomDatabase() {

    abstract fun usersDao(): UsersDao

    companion object {

        private var database: AppDatabase? = null
        private const val DB_NAME = "users.db"

        fun getInstance(context: Context): AppDatabase{
            synchronized(this) {
                var instance = database
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                    database = instance
                }
                return instance
            }
        }
    }
}