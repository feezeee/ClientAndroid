package com.example.kursachclient.presentation.fragment.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.AuthorizeModel
import com.example.kursachclient.domain.TokenModel
import com.example.kursachclient.domain.instance.RetrofitInstance
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    val tokenLiveData = MutableLiveData<Result<TokenModel>>()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                val model = AuthorizeModel(login, password)
                val res = apiService.getToken(model)
//                tokenLiveData.postValue(Result.success(res))
            }
            catch (e: Exception){
                tokenLiveData.postValue(Result.failure(e))
                e.printStackTrace()
            }

        }
    }
}