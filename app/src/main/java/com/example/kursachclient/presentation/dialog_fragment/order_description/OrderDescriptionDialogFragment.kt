package com.example.kursachclient.presentation.dialog_fragment.order_description

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.kursachclient.databinding.FragmentBasketDialogBinding
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse

class OrderDescriptionDialogFragment(private val minValue: UInt, private val maxValue: UInt, private var item: GetOrderItemResponse) : DialogFragment() {

    private lateinit var binding: FragmentBasketDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.npBasketNumberPicker.minValue = minValue.toInt()
        binding.npBasketNumberPicker.maxValue = maxValue.toInt()
        binding.npBasketNumberPicker.value = item.count.toInt()
        binding.btnBasketSetNumber.setOnClickListener {
            item.count = binding.npBasketNumberPicker.value.toUInt()
            val bundle = Bundle()
            bundle.putSerializable("ORDER_DESCRIPTION_ITEM", item)
            setFragmentResult("ORDER_DESCRIPTION", bundle)
            closeFragment()
        }
        binding.btnBasketCancel.setOnClickListener {
            closeFragment()
        }
    }

    private fun closeFragment() {
        this.dismiss()
    }
}