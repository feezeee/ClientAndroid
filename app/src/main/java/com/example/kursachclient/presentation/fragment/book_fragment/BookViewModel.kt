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

class BookViewModel : ViewModel() {
    val liveData: MutableLiveData<List<GetBookResponse>> = MutableLiveData()
    val liveDataShowToast: MutableLiveData<String> = MutableLiveData()
    val liveDataSignOutAndRedirect: MutableLiveData<Any> = MutableLiveData()

    private val retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun getBooks(filterName: String?, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var bookResponse = apiService.getBooks(filterName, "bearer $token")
                when (bookResponse.code()) {
                    200 -> {
                        liveData.postValue(bookResponse.body())
                    }
                    400 -> {
                        liveDataShowToast.postValue("Некорректный запрос")
                        liveData.postValue(emptyList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(true)
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