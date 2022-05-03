package com.mycompany.testtask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mycompany.testtask.data.AppDatabase
import com.mycompany.testtask.data.UsersRepository
import com.mycompany.testtask.models.User
import com.mycompany.testtask.network.ApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val _users by lazy { MutableStateFlow<List<User>>(emptyList()) }
    private val _currentUser by lazy { MutableStateFlow(User()) }
    private val _isLoading by lazy { MutableStateFlow(false) }
    private val _isError by lazy { MutableStateFlow(false) }
    private val _isFirstBoot by lazy { MutableStateFlow(false) }
    private val usersDao by lazy { AppDatabase.getInstance(getApplication()).usersDao() }
    private val repository by lazy { UsersRepository(usersDao) }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun getUsers() = _users

    fun getCurrentUser() = _currentUser

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun getLoading() = _isLoading


    fun getIsError() = _isError

    fun setIsError(isError: Boolean) {
        _isError.value = isError
    }

    fun getFirstIsBoot() = _isFirstBoot

    fun setIsFirstBoot(isFirstBoot: Boolean) {
        _isFirstBoot.value = isFirstBoot
    }

    fun fetchUsers() {
        handleResponse({ ApiService.getInstance().getUsers() }) {
            _isLoading.value = false
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteAllUsers()
                repository.addUsers(it)
            }
            _users.value = it
            _currentUser.value = it[0]
        }
    }

    private fun <T> handleResponse(res: suspend () -> Response<T>, onSuccess: (T) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = res()
            _isLoading.value = true
            if (response.isSuccessful) {
                response.body()?.let {
                    viewModelScope.launch { onSuccess(it) }
                }
            } else {
                _isError.value = true
                if (repository.getCountUsers() > 0) {
                    _users.value = repository.getAllUsers
                    _currentUser.value = _users.value[0]
                } else {
                    _isFirstBoot.value = true
                }
                _isLoading.value = false
            }

        }
    }

}