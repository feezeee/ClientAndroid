package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class BookAddViewModel : BaseViewModel<Unit>() {

    val liveDataNeedToNotifyGoneProgressBar: MutableLiveData<Unit> =
        MutableLiveData()

    fun addBook(book: AddBookRequest, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val response = apiService.addBook(book, "bearer $token")
                when(response.code()) {
                    201 -> {
                        liveDataShowToast.postValue("Книга была добавлена")
                        liveData.postValue(Unit)
                    }
                    400 -> {
                        liveDataShowToast.postValue("Проблема с картинкой")
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
            } catch (ex: Exception) {
                Log.e("TAG", ex.toString())
                liveDataShowToast.postValue("Ошибка на сервере")
                liveDataNeedToNotifyGoneProgressBar.postValue(Unit)
            }
        }
    }

}