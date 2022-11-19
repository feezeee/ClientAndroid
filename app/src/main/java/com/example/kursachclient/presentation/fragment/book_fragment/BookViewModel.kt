package com.example.kursachclient.presentation.fragment.book_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import kotlinx.coroutines.*

class BookViewModel() : ViewModel() {
    val liveData: MutableLiveData<List<GetBookResponse>> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val liveDataExit: MutableLiveData<Boolean> = MutableLiveData()


    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun getBooks(filterName: String?, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var bookResponse = apiService.getBooks(filterName, "bearer $token")
                when (bookResponse.code()) {
                    200 -> {
                        liveData.postValue(bookResponse.body())
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