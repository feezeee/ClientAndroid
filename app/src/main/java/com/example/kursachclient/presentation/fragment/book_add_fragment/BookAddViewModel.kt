package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookAddViewModel : BaseViewModel<Unit>() {

    fun addBook(book: AddBookRequest, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val response = apiService.addBook(book, "bearer $token")

                if (response.code() == 201) {
                    liveDataShowToast.postValue("Книга была добавлена")
                    liveData.postValue(Unit)
                }
                if (response.code() == 400) {
                    liveDataShowToast.postValue("Некорректный запрос")
                }
                if (response.code() == 401) {
                    liveDataShowToast.postValue("Ошибка авторизации")
                    // Делаем разлогин
                    liveDataSignOutAndRedirect.postValue(Unit)
                }
                if (response.code() == 403) {
                    liveDataShowToast.postValue("У вас нет прав")
                }
            } catch (ex: Exception) {
                Log.e("TAG", ex.toString())
                liveDataShowToast.postValue("Ошибка на сервере")
            }
        }
    }

}