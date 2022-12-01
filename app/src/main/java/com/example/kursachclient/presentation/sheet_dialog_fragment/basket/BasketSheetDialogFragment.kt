package com.example.kursachclient.presentation.sheet_dialog_fragment.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBottomSheetBasketItemBinding
import com.example.kursachclient.presentation.fragment.basket_fragment.BasketViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BasketSheetDialogFragment(
    private val viewModel: BasketViewModel,
    private val pref: SharedPreference,
    private val deleteItemClickListener: () -> Unit
) : BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetBasketItemBinding
    override fun getTheme(): Int = R.style.Denis_CustomBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBasketItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {

        binding.btnBasketRemoveNo.setOnClickListener{
            dialog?.dismiss()
        }
        binding.btnBasketRemoveYes.setOnClickListener {
            dialog?.dismiss()
            deleteItemClickListener()
        }
        super.onStart()
    }
}