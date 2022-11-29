package com.example.kursachclient.presentation.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse

open class BaseViewModel <T> : ViewModel(){

    val liveData: MutableLiveData<T> = MutableLiveData()
    val liveDataShowToast: MutableLiveData<String> = MutableLiveData()
    val liveDataSignOutAndRedirect: MutableLiveData<Unit> = MutableLiveData()

    private val retrofit = RetrofitInstance.getRetrofitInstance()
    protected val apiService: ApiService = retrofit.create(ApiService::class.java)
}