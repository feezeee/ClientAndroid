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
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.sheet_dialog_fragment.basket.BasketSheetDialogFragment
import com.example.kursachclient.presentation.sheet_dialog_fragment.order.OrderSheetDialogFragment

class OrderFragment : BaseFragment() {
    lateinit var binding: FragmentOrderBinding
    lateinit var viewModel: OrderViewModel
    lateinit var adapter: OrderAdapter

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
            adapter = OrderAdapter(
                it as MutableList<GetOrderResponse>
            ) { item -> itemLongClickListener(item) }
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

        viewModel.liveDataNeedToNotifyItemRemove.observe(viewLifecycleOwner) {
            if (it.first) {
                adapter.deleteItem(it.second)
            }
            progressBarIsDisplayed(false)
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


    private fun deleteItemClickListener(item: GetOrderResponse) {
        progressBarIsDisplayed(true)
        viewModel.deleteOrder(item.id, pref.getValue(), item)
    }

    private fun itemLongClickListener(item: GetOrderResponse) {
        var orderSheetItem =
            OrderSheetDialogFragment(viewModel, pref) { deleteItemClickListener(item) }
        orderSheetItem.show(childFragmentManager, "FEEZE")
    }
}