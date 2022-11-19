package com.example.kursachclient.presentation.fragment.setting_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.AddBookRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {
    val liveDataComplete: MutableLiveData<Boolean> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val liveDataExit: MutableLiveData<Boolean> = MutableLiveData()

    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun logout() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                var response = apiService.addBook("bearer $token", book)
//
//                if (response.code() == 201) {
//                    liveDataToast.postValue("Книга была добавлена")
//                    liveDataComplete.postValue(true)
//                }
//                if (response.code() == 400) {
//                    liveDataToast.postValue("Некорректный запрос")
//                }
//                if (response.code() == 401) {
//                    liveDataToast.postValue("Ошибка авторизации")
//                    // Делаем разлогин
//                    liveDataExit.postValue(true)
//                }
//                if (response.code() == 403) {
//                    liveDataToast.postValue("У вас нет прав")
//                }
//            } catch (ex: Exception) {
//                Log.e("TAG", ex.toString())
//                liveDataToast.postValue("Какие-то проблемы с сервером")
//
//            }
//        }
    }
}