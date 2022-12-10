package com.example.kursachclient.presentation.fragment.order_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderViewModel : BaseViewModel<List<GetOrderResponse>>() {



    val liveDataNeedToNotifyItemRemove: MutableLiveData<Pair<Boolean, GetOrderResponse>> =
        MutableLiveData()

    fun getOrders(filterName: String?, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var ordersResponse = apiService.getOrders(filterName,"bearer $token")
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

    fun deleteOrder(
        orderId: Int,
        token: String,
        item: GetOrderResponse
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var response = apiService.deleteOrder(orderId, "bearer $token")
                when (response.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Заказ был удален")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                true,
                                item
                            )
                        )
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                false,
                                item
                            )
                        )
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                false,
                                item
                            )
                        )
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                false,
                                item
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyItemRemove.postValue(
                    Pair(
                        false,
                        item
                    )
                )
                Log.e("TAG", ex.toString())
            }
        }
    }
}