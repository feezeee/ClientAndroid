package com.example.kursachclient.presentation.fragment.order_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.databinding.FragmentOrderBinding
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.BaseFragment

class OrderFragment : BaseFragment() {
    lateinit var binding: FragmentOrderBinding
    lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = OrderViewModel()
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBarIsDisplayed(true)

        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            var adapter = OrderAdapter(it)
            binding.rvOrderItems.layoutManager = GridLayoutManager(context, 1)
            binding.rvOrderItems.adapter = adapter
            progressBarIsDisplayed(false)
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            signOutAndRedirect()
        }


        viewModel.getOrders(pref.getValue())
    }

    private fun progressBarIsDisplayed(isDisplayed : Boolean){
        when(isDisplayed){
            true -> {
                binding.clOrderProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.clOrderProgressBar.visibility = View.GONE
            }
        }
    }

    private fun clickLongListener(item: GetBookResponse) {
        Log.e("TAG", "${item.name}")
    }
}