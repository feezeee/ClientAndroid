package com.example.kursachclient.presentation.fragment.setting_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.R
import com.example.kursachclient.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val backCallBack = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.bookFragment)
//            }
//        }
//        activity?.onBackPressedDispatcher?.addCallback(backCallBack)
    }
}