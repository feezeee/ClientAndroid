package com.example.kursachclient.presentation.fragment.register_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.*
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.authorization.PostAuthorizerModel
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.registration.PostRegisterModel
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationViewModel : BaseViewModel<Boolean>() {

    val liveDataNeedToNotifyLoginIsNotFree: MutableLiveData<Unit> =
        MutableLiveData()

    fun register(registerModel : PostRegisterModel) {
        viewModelScope.launch {
            delay(1000)
            try {
                val checkLogin = apiService.loginIsFree(registerModel.login)
                if(checkLogin.code() == 200){
                    if(checkLogin.body()?.isFree == true){
                        val res = apiService.register(registerModel)
                        if (res.code() == 200) {
                            liveDataShowToast.postValue("Регистрация выполнена успешно")
                            liveData.postValue(true)
                        }
                        else {
                            liveDataShowToast.postValue("Ошибка на сервере")
                            liveData.postValue(false)
                        }
                    }
                    else{
                        liveDataNeedToNotifyLoginIsNotFree.postValue(Unit)
                        liveData.postValue(false)
                    }
                }
                else{
                    liveDataNeedToNotifyLoginIsNotFree.postValue(Unit)
                    liveData.postValue(false)
                }
            } catch (e: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveData.postValue(false)
                e.printStackTrace()
            }

        }
    }
}