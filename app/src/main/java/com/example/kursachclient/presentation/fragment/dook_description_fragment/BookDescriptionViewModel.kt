package com.example.kursachclient.presentation.fragment.dook_description_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.AddOrRemoveBookFromBasketRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookDescriptionViewModel : ViewModel() {

    val liveData: MutableLiveData<GetBookResponse> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val liveDataExit: MutableLiveData<Boolean> = MutableLiveData()

    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun getBookById(id: Int, token: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                var bookResult = apiService.getBookByKey(id,"bearer $token")
                when(bookResult.code()){
                    200 -> {
                        if(bookResult.body() == null) {
                            liveDataToast.postValue("Ошибка сервера")
                        }
                        if(bookResult.body() != null){
                            liveData.postValue(bookResult.body())
                        }
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
            }
            catch (ex: Exception){
                liveDataToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.message.toString())
            }
        }
    }

    fun addOrRemoveFromBasket(bookItem : AddOrRemoveBookFromBasketRequest, token: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                var bookResult = apiService.addOrDeleteBookFromBasket(bookItem, "bearer $token")
                when(bookResult.code()){
                    200 -> {
                        liveDataToast.postValue("Выполнено")
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
            }
            catch (ex: Exception){
                liveDataToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.message.toString())
            }
        }
    }

    fun addOrRemoveFromBasket() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                var bookResponse = apiService.getBooks("bearer ${sharedPreference.getValue()}")
//
//                if (bookResponse.code() == 200) {
//                    if(!bookResponse.body().isNullOrEmpty())
//                    {
//                        liveData.postValue(bookResponse.body()!!)
//                    }
//                }
//                if (bookResponse.code() == 400) {
//                    liveDataToast.postValue("Некорректный запрос")
//                }
//                if(bookResponse.code() == 401){
//                    liveDataToast.postValue("Ошибка авторизации")
//                    // Делаем разлогин
//                    liveDataExit.postValue(true)
//                }
//                if(bookResponse.code() == 403){
//                    liveDataToast.postValue("У вас нет прав")
//                }
//            } catch (ex: Exception) {
//                Log.e("TAG", ex.toString())
//            }
//        }
    }
}