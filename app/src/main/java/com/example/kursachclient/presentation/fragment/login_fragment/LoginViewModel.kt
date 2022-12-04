package com.example.kursachclient.presentation.fragment.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.model.authorization.PostAuthorizerModel
import com.example.kursachclient.domain.model.authorization.GetTokenRoleModel
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<GetTokenRoleModel>() {
    val liveDataNeedToNotifySomeProblemWithServer: MutableLiveData<Unit> =
        MutableLiveData()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            delay(1000)
            try {
                val model = PostAuthorizerModel(login, password)
                val res = apiService.getToken(model)
                if (res.code() == 200) {
                    liveDataShowToast.postValue("Вход выполнен успешно")
                    liveData.postValue(res.body())
                } else {
                    liveDataShowToast.postValue("Проблемы со входом")
                    liveDataNeedToNotifySomeProblemWithServer.postValue(Unit)
                }

            } catch (e: Exception) {
                liveDataShowToast.postValue("Проблемы со входом")
                liveDataNeedToNotifySomeProblemWithServer.postValue(Unit)
                e.printStackTrace()
            }

        }
    }
}