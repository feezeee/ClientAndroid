package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderDescriptionViewModel : BaseViewModel<MutableList<GetOrderItemResponse>>() {
    val liveDataNeedToNotifyItemChanged: MutableLiveData<Triple<Boolean, Int, GetOrderItemResponse>> =
        MutableLiveData()

    val liveDataNeedToChangeStatus: MutableLiveData<String> =
        MutableLiveData()

    val liveDataNeedToNotifyItemRemove: MutableLiveData<Pair<Boolean, GetOrderItemResponse>> =
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
                        liveData.postValue(emptyList<GetOrderItemResponse>().toMutableList())
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveData.postValue(emptyList<GetOrderItemResponse>().toMutableList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveData.postValue(emptyList<GetOrderItemResponse>().toMutableList())
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveData.postValue(emptyList<GetOrderItemResponse>().toMutableList())
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveData.postValue(emptyList<GetOrderItemResponse>().toMutableList())
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

    fun deleteOrderItem(orderItemId: Int, token: String, item: GetOrderItemResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var response = apiService.deleteOrderItem(orderItemId, "bearer $token")
                when (response.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Книга удалена")
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