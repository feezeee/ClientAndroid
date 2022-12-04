package com.example.kursachclient.presentation.fragment.order_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentOrderBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.sheet_dialog_fragment.delete_item.DeleteItemSheetDialogFragment
import java.lang.reflect.Executable

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
            try {
                Log.e("TAG", it.toString())
                adapter = OrderAdapter(
                    it as MutableList<GetOrderResponse>
                ) { item -> itemLongClickListener(item) }
                binding.rvOrderItems.layoutManager = GridLayoutManager(context, 1)
                binding.rvOrderItems.adapter = adapter
                progressBarIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try {
                showToast(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try {
                signOutAndRedirect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemRemove.observe(viewLifecycleOwner) {
            try {
                if (it.first) {
                    adapter.deleteItem(it.second)
                }
                progressBarIsDisplayed(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.rgOrderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            try {
                progressBarIsDisplayed(true)
                val checkedButton = view.findViewById<RadioButton>(checkedId)
                if (checkedButton.text.toString().lowercase() == "все") {
                    viewModel.getOrders(null, pref.getToken())
                } else {
                    viewModel.getOrders(checkedButton.text.toString(), pref.getToken())
                }
            } catch (e: Exception) {
                progressBarIsDisplayed(false)
                e.printStackTrace()
            }
        }
        viewModel.getOrders(null, pref.getToken())
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.rgOrderRadioGroup.isEnabled = false
                    binding.rgOrderRadioGroup.children.forEach { it.isEnabled = false }
                    binding.clOrderProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.rgOrderRadioGroup.isEnabled = true
                    binding.rgOrderRadioGroup.children.forEach { it.isEnabled = true }
                    binding.clOrderProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        binding.rgOrderRadioGroup.clearCheck()
        binding.rbOrderAll.isChecked = true
    }

    private fun deleteItemClickListener(item: GetOrderResponse) {
        try {
            progressBarIsDisplayed(true)
            viewModel.deleteOrder(item.id, pref.getToken(), item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun itemLongClickListener(item: GetOrderResponse) {
        try {
            val mainText = resources.getString(R.string.delete_order)
            val orderSheetItem =
                DeleteItemSheetDialogFragment(mainText) { deleteItemClickListener(item) }
            orderSheetItem.show(childFragmentManager, "FEEZE")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}