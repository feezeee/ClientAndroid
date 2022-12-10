package com.example.kursachclient.presentation.fragment.profile_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.user_profile.GetUserProfileResponse
import com.example.kursachclient.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel<GetUserProfileResponse>() {
    val liveDataUserInfNeedToNotifyProgressBarHide: MutableLiveData<Unit> =
        MutableLiveData()
    val liveDataPersonalOrdersNeedToNotifyProgressBarHide: MutableLiveData<Unit> =
        MutableLiveData()

    val liveDataOrderList : MutableLiveData<MutableList<GetOrderResponse>> = MutableLiveData()

    fun getUserProfile(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                val userProfile = apiService.getUserProfile("bearer $token")
                when (userProfile.code()) {
                    200 -> {
                        liveData.postValue(userProfile.body())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                        liveDataUserInfNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataUserInfNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataUserInfNeedToNotifyProgressBarHide.postValue(Unit)
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun getPersonalOrderList(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val personalOrderListResponse = apiService.getPersonalOrders("bearer $token")
                when (personalOrderListResponse.code()) {
                    200 -> {
                        liveDataOrderList.postValue(personalOrderListResponse.body())
                    }
                    204 -> {
                        liveDataOrderList.postValue(emptyList<GetOrderResponse>().toMutableList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                        liveDataPersonalOrdersNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveDataPersonalOrdersNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataPersonalOrdersNeedToNotifyProgressBarHide.postValue(Unit)
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataPersonalOrdersNeedToNotifyProgressBarHide.postValue(Unit)
                Log.e("TAG", ex.toString())
            }
        }
    }
}