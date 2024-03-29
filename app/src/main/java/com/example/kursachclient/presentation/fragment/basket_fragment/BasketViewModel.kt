package com.example.kursachclient.presentation.fragment.basket_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val apiService: ApiService) :
    BaseViewModel<MutableList<GetBasketResponse>>() {
    val liveDataNeedToNotifyItemChanged: MutableLiveData<Triple<Boolean, Int, GetBasketResponse>> =
        MutableLiveData()

    val liveDataNeedToNotifyItemRemove: MutableLiveData<Pair<Boolean, GetBasketResponse>> =
        MutableLiveData()

    val liveDataNeedToNotifyBasketEmpty: MutableLiveData<Unit> = MutableLiveData()

    fun getBasket(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var basketResponse = apiService.getBasket("bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveData.postValue(basketResponse.body())
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveData.postValue(emptyList<GetBasketResponse>().toMutableList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveData.postValue(emptyList<GetBasketResponse>().toMutableList())
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveData.postValue(emptyList<GetBasketResponse>().toMutableList())
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveData.postValue(emptyList<GetBasketResponse>().toMutableList())
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun clearBasket(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var basketResponse = apiService.clearBasket("bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Корзина очищена")
                        liveData.postValue(emptyList<GetBasketResponse>().toMutableList())
                        liveDataNeedToNotifyBasketEmpty.postValue(Unit)
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

    fun addOrRemoveItemFromBasket(
        item: AddBookToBasketRequest, token: String, position: Int, basketItem: GetBasketResponse
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var basketResponse = apiService.addBookToBasket(item, "bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Корзина была обновлена")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                true, position, basketItem
                            )
                        )
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                false, position, basketItem
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
                                false, position, basketItem
                            )
                        )
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyItemChanged.postValue(
                            Triple(
                                false, position, basketItem
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyItemChanged.postValue(
                    Triple(
                        false, position, basketItem
                    )
                )
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun makeOrder(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var ordersResponse = apiService.makeOrder("bearer $token")
                when (ordersResponse.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Заказ оформлен")
                        getBasket(token)
                    }
                    400 -> {
                        getBasket(token)
                        liveDataShowToast.postValue("Некорректный запрос")
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        getBasket(token)
                        liveDataShowToast.postValue("У вас нет прав")
                    }
                    else -> {
                        getBasket(token)
                        liveDataShowToast.postValue("Ошибка на сервере")
                    }
                }
            } catch (ex: Exception) {
                getBasket(token)
                liveDataShowToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun deleteItemFromBasket(
        itemId: Int, token: String, item: GetBasketResponse
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var response = apiService.clearBasket(itemId, "bearer $token")
                when (response.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Корзина была обновлена")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                true, item
                            )
                        )
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                false, item
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
                                false, item
                            )
                        )
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyItemRemove.postValue(
                            Pair(
                                false, item
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyItemRemove.postValue(
                    Pair(
                        false, item
                    )
                )
                Log.e("TAG", ex.toString())
            }
        }
    }
}