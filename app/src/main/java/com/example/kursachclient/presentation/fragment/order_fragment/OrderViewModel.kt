package com.example.kursachclient.presentation.fragment.order_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderViewModel : BaseViewModel<List<GetOrderResponse>>() {

    fun getOrders(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var ordersResponse = apiService.getOrders("bearer $token")
                when (ordersResponse.code()) {
                    200 -> {
                        liveData.postValue(ordersResponse.body())
                    }
                    204 -> {
                        liveData.postValue(emptyList())
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveData.postValue(emptyList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveData.postValue(emptyList())
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveData.postValue(emptyList())
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveData.postValue(emptyList())
                Log.e("TAG", ex.toString())
            }
        }
    }
}