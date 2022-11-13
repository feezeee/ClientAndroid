package com.example.kursachclient.presentation.fragment.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.AuthorizeModel
import com.example.kursachclient.domain.TokenModel
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val liveData: MutableLiveData<TokenModel> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                val model = AuthorizeModel(login, password)
                val res = apiService.getToken(model)
                if (res.code() == 200) {
                    if(res.body() != null)
                    {
                        liveDataToast.postValue("Вход выполнен успешно")
                        liveData.postValue(res.body())
                    }
                    else {
                        liveDataToast.postValue("Какие-то проблемы с токеном")
                    }
                }
                else {
                    liveDataToast.postValue("Проблемы со входом")
                }

            } catch (e: Exception) {
                liveDataToast.postValue("Проблемы со входом")
                e.printStackTrace()
            }

        }
    }
}