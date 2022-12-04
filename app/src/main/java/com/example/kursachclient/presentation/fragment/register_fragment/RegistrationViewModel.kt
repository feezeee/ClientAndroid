package com.example.kursachclient.presentation.fragment.register_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.*
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.registration.PostRegisterModel
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    val registrationLiveDate = MutableLiveData<Result<Boolean>>()
    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun register(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                var model = PostRegisterModel(firstName, lastName, phoneNumber, login, password)
                var isSuccessfully = apiService.register(model)
//                registrationLiveDate.postValue(Result.success(isSuccessfully))
            }
            catch (e: Exception){
                registrationLiveDate.postValue(Result.failure(e))
                e.printStackTrace()
            }

        }
    }
}