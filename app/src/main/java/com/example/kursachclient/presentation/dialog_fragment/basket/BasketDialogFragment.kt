package com.example.kursachclient.presentation.dialog_fragment.basket

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
import dagger.hilt.android.AndroidEntryPoint
import java.util.zip.Inflater
@AndroidEntryPoint
class BasketDialogFragment(private val minValue: UInt, private val maxValue: UInt, private var item: GetBasketResponse) : DialogFragment() {

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
            try {
                item.count = binding.npBasketNumberPicker.value.toUInt()
                val bundle = Bundle()
                bundle.putSerializable("BASKET_ITEM", item)
                setFragmentResult("REQUEST_FEEZE", bundle)
                closeFragment()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.btnBasketCancel.setOnClickListener {
            try {

                closeFragment()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun closeFragment() {
        try {
            this.dismiss()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}