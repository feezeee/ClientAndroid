package com.example.kursachclient.presentation.fragment.dook_description_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookDescriptionViewModel : ViewModel() {

    val liveData: MutableLiveData<List<GetBookResponse>> = MutableLiveData()
    val liveDataToast: MutableLiveData<String> = MutableLiveData()
    val liveDataExit: MutableLiveData<Boolean> = MutableLiveData()

    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

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