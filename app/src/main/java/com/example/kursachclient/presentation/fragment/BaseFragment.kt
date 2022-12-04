package com.example.kursachclient.presentation.fragment

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference

open class BaseFragment : Fragment() {
    lateinit var pref: SharedPreference

    protected fun signOutAndRedirect() {
        try{
            pref.clearValue()
            findNavController().navigate(R.id.loginFragment)
        }
        catch (e: Exception){
            Log.d("KEK", "Проблема с авторизацией")
        }
    }

    protected fun showToast(message: String) {
        try{
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        catch (e: Exception){
            Log.d("KEK", "Проблема с toast")
        }
    }
}