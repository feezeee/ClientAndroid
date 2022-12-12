package com.example.kursachclient.presentation.sheet_dialog_fragment.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBottomSheetPaymentBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.order_fragment.OrderAdapter
import com.example.kursachclient.presentation.fragment.order_fragment.OrderViewModel
import com.example.kursachclient.presentation.fragment.register_fragment.RegistrationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
@AndroidEntryPoint
class PaymentSheetDialogFragment(
    private val orderItem : GetOrderResponse,
    private val updateInfo: () -> Unit

) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetPaymentBinding
    override fun getTheme(): Int = R.style.Denis_CustomBottomSheetDialogTheme

    private val viewModel by viewModels<PaymentViewModel>()
    lateinit var pref: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pref = SharedPreference(requireContext())
        binding = FragmentBottomSheetPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        binding.tvPaymentFullPrice.text = String.format(orderItem.fullPrice.toBigDecimal().setScale(2, RoundingMode.UP).toString())
        binding.etPaymentCardNumber.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    binding.etPaymentCardNumber.error = "Заполните поле"
                }
                else {
                    if (s.toString() != current) {
                        val userInput = s.toString().replace(Regex("[^\\d]"),"")
                        if (userInput.length <= 16) {
                            current = userInput.chunked(4).joinToString(" ")
                            s.filters = arrayOfNulls<InputFilter>(0)
                        }
                        s.replace(0, s.length, current, 0, current.length)
                    }
                }
                binding.tvPaymentCardNumber.text = s
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        binding.etPaymentMonth.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                try {
                    if(s.isEmpty()){
                        binding.etPaymentMonth.error = "Заполните поле"
                    }
                    else {
                        val month = s.toString().toInt()
                        if (month <= 0 || month > 12){
                            binding.etPaymentMonth.error = "Проблемы с месяцем"
                        }
                    }
                    binding.tvPaymentMonth.text = s
                }
                catch (e: Exception){
                    binding.etPaymentMonth.error = "Проблемы с месяцем"
                }


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.etPaymentYear.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    binding.etPaymentYear.error = "Заполните поле"
                }
                binding.tvPaymentYear.text = s
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        binding.etPaymentCardHolderName.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    binding.etPaymentCardHolderName.error = "Заполните поле"
                }
                else {
                    if (s.toString() != current) {
                        val userInput = s.toString().replace(Regex("[\\d]"), "").uppercase()
                        if (userInput.length <= 25) {
                            current = userInput
                        }
                        s.replace(0, s.length, current, 0, current.length)
                    }
                }

                binding.tvPaymentCardHolder.text = s
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        binding.etPaymentCvv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    binding.etPaymentCvv.error = "Заполните поле"
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        binding.btnPaymentPay.setOnClickListener {
            var status = true
            if(binding.etPaymentCardNumber.text.isEmpty() || binding.etPaymentCardNumber.text.length != 19){
                binding.etPaymentCardNumber.error = "Заполните поле"
                status = false
            }

            if(binding.etPaymentMonth.text.isEmpty() || binding.etPaymentMonth.text.length != 2){
                binding.etPaymentMonth.error = "Заполните поле"
                status = false
            }
            else {
                val month = binding.etPaymentMonth.text.toString().toInt()
                if (month <= 0 || month > 12){
                    binding.etPaymentMonth.error = "Проблемы с месяцем"
                    status = false
                }
            }
            if(binding.etPaymentYear.text.isEmpty() || binding.etPaymentYear.text.length != 2){
                binding.etPaymentYear.error = "Заполните поле"
                status = false
            }
            if(binding.etPaymentCvv.text.isEmpty() || binding.etPaymentCvv.text.length != 3){
                binding.etPaymentCvv.error = "Заполните поле"
                status = false
            }
            if(binding.etPaymentCardHolderName.text.isEmpty()){
                binding.etPaymentCardHolderName.error = "Заполните поле"
                status = false
            }
            if(status){
                viewModel.payOrder(orderItem, pref.getToken())
            }
        }

        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                Log.e("TAG", it.toString())
                updateInfo()
                dismiss()
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

    }


    protected fun signOutAndRedirect() {
        try{
            pref.clearValue()
            findNavController().navigate(R.id.loginFragment)
        }
        catch (e: Exception){
            Log.d("KEK", "Проблема с авторизацией")
        }
    }

    protected fun showToast(message: String) {
        try{
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        catch (e: Exception){
            Log.d("KEK", "Проблема с toast")
        }
    }
}