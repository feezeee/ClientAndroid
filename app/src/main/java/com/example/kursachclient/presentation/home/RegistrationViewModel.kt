package com.example.kursachclient.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.RegisterModel
import com.example.kursachclient.domain.TokenModel
import com.example.kursachclient.domain.instance.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun register(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        login: String,
        password: String
    ): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                var model = RegisterModel(firstName, lastName, phoneNumber, login, password)
                apiService.register(model)
                return@launch true
            } catch (ex: Exception) {
                Log.e("TAG", ex.toString())
            }
        }
    }
}