package com.mycompany.testtask.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mycompany.testtask.models.User

@Dao
interface UsersDao {

    @Query("select * from users")
    fun getAllUsers(): List<User>

    @Query("select count(*) from users")
    fun getCountUsers(): Long

    @Query("select * from users where id == :id")
    fun getUser(id: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("delete from users")
    suspend fun deleteAllUsers()

}