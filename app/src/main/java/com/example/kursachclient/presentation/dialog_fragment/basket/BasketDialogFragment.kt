package com.example.kursachclient.presentation.dialog_fragment.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.kursachclient.databinding.FragmentBasketBinding
import com.example.kursachclient.databinding.FragmentDialogBasketBinding
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import java.util.zip.Inflater

class BasketDialogFragment(private val minValue: UInt, private val maxValue: UInt, private var item: GetBasketResponse) : DialogFragment() {

    private lateinit var binding: FragmentDialogBasketBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.npBasketNumberPicker.minValue = minValue.toInt()
        binding.npBasketNumberPicker.maxValue = maxValue.toInt()
        binding.npBasketNumberPicker.value = item.count.toInt()
        binding.btnBasketSetNumber.setOnClickListener {
            item.count = binding.npBasketNumberPicker.value.toUInt()
            val bundle = Bundle()
            bundle.putSerializable("BASKET_ITEM", item)
            setFragmentResult("REQUEST_FEEZE", bundle)
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