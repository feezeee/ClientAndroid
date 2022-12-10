package com.example.kursachclient.presentation.fragment.book_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.BaseViewModel
import kotlinx.coroutines.*

class BookViewModel : BaseViewModel<MutableList<GetBookResponse>>() {
    val liveDataNeedToNotifyItemRemove: MutableLiveData<Pair<Boolean, GetBookResponse>> =
        MutableLiveData()

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
                        liveData.postValue(emptyList<GetBookResponse>().toMutableList())
                    }
                    401 -> {
                        liveDataSignOutAndRedirect.postValue(Unit)
                    }
                    403 -> {
                        liveDataShowToast.postValue("У вас нет прав")
                        liveData.postValue(emptyList<GetBookResponse>().toMutableList())
                    }
                    else -> {
                        liveDataShowToast.postValue("Ошибка на сервере")
                        liveData.postValue(emptyList<GetBookResponse>().toMutableList())
                    }
                }
            } catch (ex: Exception) {
                liveDataShowToast.postValue("Ошибка на сервере")
                liveData.postValue(emptyList<GetBookResponse>().toMutableList())
                Log.e("TAG", ex.toString())
            }
        }
    }

    fun deleteBook(
        bookId: Int,
        token: String,
        item: GetBookResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                var response = apiService.deleteBook(bookId, "bearer $token")
                when (response.code()) {
                    200 -> {
                        liveDataShowToast.postValue("Книга была удалена")
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