package com.example.kursachclient.presentation.fragment.book_description_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import com.example.kursachclient.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookDescriptionViewModel : BaseViewModel<GetBookResponse>() {
    val liveDataNeedToNotifyGoneProgressBar: MutableLiveData<Unit> =
        MutableLiveData()
    fun getBookById(id: Int, token: String){
        viewModelScope.launch(Dispatchers.IO){
            delay(1000)
            try {
                var bookResult = apiService.getBookByKey(id,"bearer $token")
                when(bookResult.code()){
                    200 -> {
                        if(bookResult.body() == null) {
                            liveDataShowToast.postValue("Ошибка сервера")
                        }
                        if(bookResult.body() != null){
                            liveData.postValue(bookResult.body())
                        }
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                    }
                    401 -> {
                        liveDataShowToast.postValue("Ошибка авторизации")
                        // Делаем разлогин
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                    }
                }
            }
            catch (ex: Exception){
                liveDataShowToast.postValue("Ошибка на сервере")
                Log.e("TAG", ex.message.toString())
            }
        }
    }

    fun addOrRemoveFromBasket(bookItem : AddBookToBasketRequest, token: String){
        viewModelScope.launch(Dispatchers.IO){
            delay(1000)
            try {
                val bookResult = apiService.addBookToBasket(bookItem, "bearer $token")
                when(bookResult.code()){
                    200 -> {
                        liveDataShowToast.postValue("Выполнено")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    401 -> {
                        liveDataShowToast.postValue("Ошибка авторизации")
                        // Делаем разлогин
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                }
            }
            catch (ex: Exception){
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                Log.e("TAG", ex.message.toString())
            }
        }
    }

    fun updateBook(bookItem : UpdateBookRequest, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                var bookResult = apiService.updateBook(bookItem,"bearer $token")
                when(bookResult.code()){
                    200 -> {
                        liveDataShowToast.postValue("Изменения сохранены")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    401 -> {
                        liveDataShowToast.postValue("Ошибка авторизации")
                        // Делаем разлогин
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                    }
                }
            }
            catch (ex: Exception){
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
                Log.e("TAG", ex.message.toString())
            }
        }
    }
}