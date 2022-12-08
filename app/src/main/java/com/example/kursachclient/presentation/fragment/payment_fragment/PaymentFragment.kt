package com.example.kursachclient.presentation.fragment.payment_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentOrderBinding
import com.example.kursachclient.databinding.FragmentPaymentBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.order_fragment.OrderAdapter
import com.example.kursachclient.presentation.fragment.order_fragment.OrderViewModel
import com.example.kursachclient.utils.GooglePaymentUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*

class PaymentFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
//        viewModel = OrderViewModel()
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val REQUEST_CODE = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnPaymentTest.setOnClickListener {
            val request = GooglePaymentUtils.createPaymentDataRequest("12")
            AutoResolveHelper.resolveTask<PaymentData>(googlePaymentsClient.loadPaymentData(request),
                requireActivity(), REQUEST_CODE)
        }
    }

    private lateinit var googlePaymentsClient: PaymentsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googlePaymentsClient = GooglePaymentUtils.createGoogleApiClientForPay(requireContext())
    }

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data == null)
                            return

                        val paymentData = PaymentData.getFromIntent(data)
                    }
                    Activity.RESULT_CANCELED -> {
                        // Пользователь нажал назад,
                        // когда был показан диалог google pay
                        // если показывали загрузку или что-то еще,
                        // можете отменить здесь
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        if (data == null)
                            return

                        // Гугл сам покажет диалог ошибки.
                        // Можете вывести логи и спрятать загрузку,
                        // если показывали

                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.e("GOOGLE PAY", "Load payment data has failed with status: $status")
                    }
                    else -> { }
                }
            }
            else -> { }
        }
    }

}