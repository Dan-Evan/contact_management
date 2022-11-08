package com.example.contactmanagment.repository

import androidx.lifecycle.LiveData
import com.example.contactmanagment.data.UserDao
import com.example.contactmanagment.model.User

class UserRepository(private val userDao: UserDao) {

    val readAllUser: LiveData<List<User>> = userDao.readAllUser()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUser(){
        userDao.deleteAllUser()
    }
}