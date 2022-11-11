package com.example.kursachclient.presentation.home

import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.kursachclient.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding

    val homeViewModel = HomeViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel.liveData.observe(viewLifecycleOwner){
            Log.e("TAG", it.toString())
            var adapter = HomeAdapter(it)
            binding.rvBooks.layoutManager = GridLayoutManager(context, 2)
            binding.rvBooks.adapter = adapter
        }
//        homeViewModel.getBooks()
    }
}