package com.example.kursachclient.presentation.sheet_dialog_fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBottomSheetBasketItemBinding
import com.example.kursachclient.databinding.FragmentBottomSheetOrderItemBinding
import com.example.kursachclient.presentation.fragment.basket_fragment.BasketViewModel
import com.example.kursachclient.presentation.fragment.order_fragment.OrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderSheetDialogFragment(
    private val viewModel: OrderViewModel,
    private val pref: SharedPreference,
    private val deleteItemClickListener: () -> Unit
) : BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetOrderItemBinding
    override fun getTheme(): Int = R.style.Denis_CustomBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetOrderItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {

        binding.btnOrderSheetDialogRemoveNo.setOnClickListener{
            dialog?.dismiss()
        }
        binding.btnOrderSheetDialogRemoveYes.setOnClickListener {
            dialog?.dismiss()
            deleteItemClickListener()
        }
        super.onStart()
    }
}