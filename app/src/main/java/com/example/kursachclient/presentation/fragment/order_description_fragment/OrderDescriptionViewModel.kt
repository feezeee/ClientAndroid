package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderDescriptionViewModel : BaseViewModel<List<GetOrderItemResponse>>() {
    val liveDataNeedToNotifyItemChanged: MutableLiveData<Triple<Boolean, Int, GetOrderItemResponse>> =
        MutableLiveData()

    val liveDataNeedToChangeStatus: MutableLiveData<String> =
        MutableLiveData()

    fun getOrderItems(orderId: Int, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var orderItemsResponse = apiService.getOrderItems(orderId, "bearer $token")
                when (orderItemsResponse.code()) {
                    200 -> {
                        liveData.postValue(orderItemsResponse.body())
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

    fun updateOrderItem(orderItem: UpdateOrderItemRequest, token: String, position: Int, orderDescription: GetOrderItemResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var basketResponse = apiService.updateOrderItem(orderItem, "bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Заказ был обновлен")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                true,
                                position,
                                orderDescription
                            )
                        )
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                false,
                                position,
                                orderDescription
                            )
                        )
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                false,
                                position,
                                orderDescription
                            )
                        )
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                false,
                                position,
                                orderDescription
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyItemChanged.postValue(
                    Triple(
                        false,
                        position,
                        orderDescription
                    )
                )
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun updateStatus(orderId: Int, status: String, token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var basketResponse = apiService.updateStatus(orderId, status, "bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Статус был обновлен")
                        liveDataNeedToChangeStatus.postValue(status)
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
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