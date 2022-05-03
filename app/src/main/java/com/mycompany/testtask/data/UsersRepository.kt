package com.mycompany.testtask.data

import com.mycompany.testtask.models.User

class UsersRepository(private val usersDao: UsersDao) {

    val getAllUsers: List<User> = usersDao.getAllUsers()

    fun getCountUsers(): Long = usersDao.getCountUsers()

    suspend fun addUsers(users: List<User>) {
        usersDao.insertUsers(users)
    }

    suspend fun deleteAllUsers() = usersDao.deleteAllUsers()

}