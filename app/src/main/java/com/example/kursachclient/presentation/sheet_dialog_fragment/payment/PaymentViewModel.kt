package com.example.kursachclient.presentation.sheet_dialog_fragment.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.order.PayOrderRequest
import com.example.kursachclient.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val apiService: ApiService)  : BaseViewModel<Unit>() {

    fun payOrder(item: GetOrderResponse, token: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val order = PayOrderRequest(item.id, item.fullPrice)
                val getOrderResponse = apiService.payOrder(order, "bearer $token")
                when (getOrderResponse.code()) {
                    200 -> {
                        liveData.postValue(Unit)
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.toString())
            }
        }
    }
}