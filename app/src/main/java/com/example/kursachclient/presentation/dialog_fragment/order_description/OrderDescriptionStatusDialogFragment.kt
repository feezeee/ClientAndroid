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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            "Ожидает оплаты".lowercase() -> {
                binding.rbOrderDescriptionDialogPaymentWait.isChecked = true
            }
            "Оплачено".lowercase() -> {
                binding.rbOrderDescriptionDialogPaymentComplete.isChecked = true
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
            try {
                if(binding.rbOrderDescriptionDialogInProgress.isChecked){
                    item.status = "В обработке"
                }
                else if(binding.rbOrderDescriptionDialogWaitClient.isChecked){
                    item.status = "Ожидает клиента"
                }
                else if(binding.rbOrderDescriptionDialogPaymentWait.isChecked){
                    item.status = "Ожидает оплаты"
                }
                else if(binding.rbOrderDescriptionDialogPaymentComplete.isChecked){
                    item.status = "Оплачено"
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
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.btnOrderDescriptionDialogCancel.setOnClickListener {
            try{
                closeFragment()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun closeFragment() {
        try{
            this.dismiss()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}