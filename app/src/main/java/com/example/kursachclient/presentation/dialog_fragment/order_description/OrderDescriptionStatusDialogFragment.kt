package com.example.kursachclient.presentation.dialog_fragment.order_description

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.kursachclient.databinding.FragmentOrderDescriptionDialogBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse

class OrderDescriptionStatusDialogFragment(private var item: GetOrderResponse) : DialogFragment() {

    private lateinit var binding: FragmentOrderDescriptionDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDescriptionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when(item.status.lowercase()){
            "В обработке".lowercase() -> {
                binding.rbOrderDescriptionDialogInProgress.isChecked = true
            }
            "Ожидает клиента".lowercase() -> {
                binding.rbOrderDescriptionDialogWaitClient.isChecked = true
            }
            "Выполнен".lowercase() -> {
                binding.rbOrderDescriptionDialogComplete.isChecked = true
            }
            "Отменен".lowercase() -> {
                binding.rbOrderDescriptionDialogCanceled.isChecked = true
            }
            else -> {
                closeFragment()
            }
        }


        binding.btnOrderDescriptionDialogSetOrderStatus.setOnClickListener {

            if(binding.rbOrderDescriptionDialogInProgress.isChecked){
               item.status = "В обработке"
            }
            else if(binding.rbOrderDescriptionDialogWaitClient.isChecked){
                item.status = "Ожидает клиента"
            }
            else if(binding.rbOrderDescriptionDialogComplete.isChecked){
                item.status = "Выполнен"
            }
            else if(binding.rbOrderDescriptionDialogCanceled.isChecked){
                item.status = "Отменен"
            }
            else {
                closeFragment()
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putSerializable("ORDER_DESCRIPTION_STATUS", item)
            setFragmentResult("ORDER_DESCRIPTION", bundle)
            closeFragment()
        }
        binding.btnOrderDescriptionDialogCancel.setOnClickListener {
            closeFragment()
        }
    }

    private fun closeFragment() {
        this.dismiss()
    }
}