package com.example.kursachclient.presentation.fragment.basket_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.GetBookResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BasketViewModel : ViewModel() {
    val liveData: MutableLiveData<List<GetBasketResponse>> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val liveDataExit: MutableLiveData<Boolean> = MutableLiveData()


    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun getBasket(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var basketResponse = apiService.getBasket("bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveData.postValue(basketResponse.body())
                    }
                    400 -> {
                        liveDataToast.postValue("Некорректный запрос")
                    }
                    401 -> {
                        liveDataToast.postValue("Ошибка авторизации")
                        // Делаем разлогин
                        liveDataExit.postValue(true)
                    }
                    403 -> {
                        liveDataToast.postValue("У вас нет прав")
                    }
                    else -> {
                        liveDataToast.postValue("Ошибка на сервере")
                    }
                }
            } catch (ex: Exception) {
                liveDataToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun clearBasket(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var basketResponse = apiService.clearBasket("bearer $token")
                when (basketResponse.code()) {
                    200 -> {
                        liveDataToast.postValue("Корзина очищена")
                        liveData.postValue(ArrayList<GetBasketResponse>())
                    }
                    400 -> {
                        liveDataToast.postValue("Некорректный запрос")
                    }
                    401 -> {
                        liveDataToast.postValue("Ошибка авторизации")
                        // Делаем разлогин
                        liveDataExit.postValue(true)
                    }
                    403 -> {
                        liveDataToast.postValue("У вас нет прав")
                    }
                    else -> {
                        liveDataToast.postValue("Ошибка на сервере")
                    }
                }
            } catch (ex: Exception) {
                liveDataToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.toString())
            }
        }
    }
}