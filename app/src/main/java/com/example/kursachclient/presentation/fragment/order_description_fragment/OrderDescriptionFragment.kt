package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentOrderDescriptionBinding
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import com.example.kursachclient.presentation.dialog_fragment.order_description.OrderDescriptionDialogFragment
import com.example.kursachclient.presentation.dialog_fragment.order_description.OrderDescriptionStatusDialogFragment
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OrderDescriptionFragment : BaseFragment() {
    lateinit var binding: FragmentOrderDescriptionBinding
    lateinit var viewModel: OrderDescriptionViewModel
    lateinit var adapter: OrderDescriptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = OrderDescriptionViewModel()
        binding = FragmentOrderDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        progressBarIsDisplayed(true)

        var orderResponse = arguments?.getSerializable("order") as GetOrderResponse

        binding.tvOrderDescriptionFullUserName.text =
            "${orderResponse.firstName} ${orderResponse.lastName}"
        binding.tvOrderDescriptionPhoneNumber.text = orderResponse.phoneNumber
        var formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy hh:mm")
        binding.tvOrderDescriptionCreatedDate.text =
            Instant.ofEpochSecond(orderResponse.createdDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(formatter).toString()
        binding.tvOrderDescriptionOrderStatus.text = orderResponse.status

        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            adapter = OrderDescriptionAdapter(
                it,
                {item, position -> clickListener(item, position)},
                {price -> setFullPrice(price)}
            )
            binding.rvOrderDescriptionRecyclerView.layoutManager = GridLayoutManager(context, 1)
            binding.rvOrderDescriptionRecyclerView.adapter = adapter
            progressBarIsDisplayed(false)
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            signOutAndRedirect()
        }

        viewModel.liveDataNeedToNotifyItemChanged.observe(viewLifecycleOwner){
            if(it.first){
                adapter.notifyItemChanged(it.second, it.third)
            }
            progressBarIsDisplayed(false)
        }
        viewModel.liveDataNeedToChangeStatus.observe(viewLifecycleOwner){
            binding.tvOrderDescriptionOrderStatus.text = it
            progressBarIsDisplayed(false)
        }

        binding.ivOrderDescriptionBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.llOrderDescriptionClickableOrderStatus.setOnClickListener{
            statusClickListener(orderResponse)
        }

        viewModel.getOrderItems(orderResponse.id, pref.getValue())
    }


    private fun statusClickListener(item: GetOrderResponse) {
        var orderStatusDialogFragment = OrderDescriptionStatusDialogFragment(item)

        childFragmentManager.setFragmentResultListener(
            "ORDER_DESCRIPTION",
            viewLifecycleOwner
        ) { resultKey, bundle ->

            if (resultKey == "ORDER_DESCRIPTION") {
                progressBarIsDisplayed(true)
                val orderResponse =
                    bundle.getSerializable("ORDER_DESCRIPTION_STATUS") as GetOrderResponse
                viewModel.updateStatus(orderResponse.id, orderResponse.status, pref.getValue())
            }
        }
        orderStatusDialogFragment.show(childFragmentManager, "kek")
    }

    private fun clickListener(item: GetOrderItemResponse, position: Int) {
        var basketDialogFragment = OrderDescriptionDialogFragment(1u, 10u, item)

        childFragmentManager.setFragmentResultListener(
            "ORDER_DESCRIPTION",
            viewLifecycleOwner
        ) { resultKey, bundle ->

            if (resultKey == "ORDER_DESCRIPTION") {
                progressBarIsDisplayed(true)
                val orderItemResponse =
                    bundle.getSerializable("ORDER_DESCRIPTION_ITEM") as GetOrderItemResponse
                val updateItem =
                    UpdateOrderItemRequest(orderItemResponse.id, orderItemResponse.count)
                viewModel.updateOrderItem(updateItem, pref.getValue(), position, orderItemResponse)
            }
        }
        basketDialogFragment.show(childFragmentManager, "kek")
    }

    private fun setFullPrice(price: BigDecimal) {
        binding.tvOrderDescriptionFullPrice.text = price.toString()
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        when (isDisplayed) {
            true -> {
                binding.clOrderDescriptionProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.clOrderDescriptionProgressBar.visibility = View.GONE
            }
        }
    }
}