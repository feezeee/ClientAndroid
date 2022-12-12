package com.example.kursachclient.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.instance.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

open class BaseViewModel<T> () : ViewModel() {
    val liveData: MutableLiveData<T> = MutableLiveData()
    val liveDataShowToast: MutableLiveData<String> = MutableLiveData()
    val liveDataSignOutAndRedirect: MutableLiveData<Unit> = MutableLiveData()
}