package com.example.kursachclient.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursachclient.domain.ApiService
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.instance.RetrofitInstance
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    val liveData : MutableLiveData<List<Book>> = MutableLiveData()
    val retrofit = RetrofitInstance.getRetrofitInstance()
    val apiService = retrofit.create(ApiService::class.java)

    fun getBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                liveData.postValue(apiService.getBooks())
//            getBooksMock()
//            delay(1000)
//                liveData.postValue(books)
            }
            catch (ex: Exception)
            {
                Log.e("TAG", ex.toString())
            }
        }
    }

    var books: MutableList<Book> = emptyList<Book>().toMutableList()

    fun getBooksMock()
    {
//        books.add(Book(name = "test1", title = "Some desck", count = 1))
//        books.add(Book(name = "test2", title = "Some desck", count = 1))
//        books.add(Book(name = "test3", title = "Some desck", count = 1))
//        books.add(Book(name = "test4", title = "Some desck", count = 1))
//        books.add(Book(name = "test5", title = "Some desck", count = 1))
//        books.add(Book(name = "test6", title = "Some desck", count = 1))
//        books.add(Book(name = "test1", title = "Some desck", count = 1))
//        books.add(Book(name = "test2", title = "Some desck", count = 1))
//        books.add(Book(name = "test3", title = "Some desck", count = 1))
//        books.add(Book(name = "test4", title = "Some desck", count = 1))
//        books.add(Book(name = "test5", title = "Some desck", count = 1))
//        books.add(Book(name = "test6", title = "Some desck", count = 1))

    }

}