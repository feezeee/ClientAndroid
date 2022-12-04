package com.example.kursachclient.presentation.fragment.profile_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.user_profile.GetUserProfileResponse
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel<GetUserProfileResponse>() {
    val liveDataNeedToNotifyProgressBarHide: MutableLiveData<Unit> =
        MutableLiveData()
    fun getUserProfile(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val userProfile = apiService.getUserProfile("bearer $token")
                when (userProfile.code()) {
                    200 -> {
                        liveData.postValue(userProfile.body())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                        liveDataNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyProgressBarHide.postValue(Unit)
                Log.e("TAG", ex.toString())
            }
        }
    }
}