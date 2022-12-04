package com.example.kursachclient.presentation.sheet_dialog_fragment.delete_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kursachclient.R
import com.example.kursachclient.databinding.FragmentBottomSheetDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteItemSheetDialogFragment(
    private val mainText: String,
    private val deleteItemClickListener: () -> Unit
) : BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetDeleteBinding
    override fun getTheme(): Int = R.style.Denis_CustomBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvBottomSheetDeleteStaticTextBeforeDeleting.text = mainText
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {

        binding.btnBottomSheetDeleteRemoveNo.setOnClickListener{
            try {
                dialog?.dismiss()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.btnBottomSheetDeleteRemoveYes.setOnClickListener {
            try {
                dialog?.dismiss()
                deleteItemClickListener()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onStart()
    }
}