package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentOrderDescriptionBinding
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import com.example.kursachclient.domain.model.order_item.UpdateOrderItemRequest
import com.example.kursachclient.presentation.dialog_fragment.order_description.OrderDescriptionDialogFragment
import com.example.kursachclient.presentation.dialog_fragment.order_description.OrderDescriptionStatusDialogFragment
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.login_fragment.LoginViewModel
import com.example.kursachclient.presentation.sheet_dialog_fragment.delete_item.DeleteItemSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class OrderDescriptionFragment : BaseFragment() {
    lateinit var binding: FragmentOrderDescriptionBinding
    private val viewModel by viewModels<OrderDescriptionViewModel>()
    lateinit var adapter: OrderDescriptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        binding = FragmentOrderDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        progressBarIsDisplayed(true)

        var orderResponse = arguments?.getSerializable("order") as GetOrderResponse

        try{
            binding.tvOrderDescriptionFullUserName.text =
                "${orderResponse.firstName} ${orderResponse.lastName}"
            binding.tvOrderDescriptionPhoneNumber.text = orderResponse.phoneNumber
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            binding.tvOrderDescriptionCreatedDate.text =
                Instant.ofEpochSecond(orderResponse.createdDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().format(formatter).toString()
            binding.tvOrderDescriptionOrderStatus.text = orderResponse.status
        }
        catch (e : Exception){
            e.printStackTrace()
        }


        viewModel.liveData.observe(viewLifecycleOwner) {
            try{
                Log.e("TAG", it.toString())
                adapter = OrderDescriptionAdapter(
                    it,
                    {item, position -> clickListener(item, position)},
                    {price -> setFullPrice(price)},
                    {item -> itemLongClickListener(item)}
                )
                binding.rvOrderDescriptionRecyclerView.layoutManager = GridLayoutManager(context, 1)
                binding.rvOrderDescriptionRecyclerView.adapter = adapter
                progressBarIsDisplayed(false)
            }
            catch (e : Exception){
                e.printStackTrace()
            }

        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try{
                showToast(message)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try{
                signOutAndRedirect()
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemChanged.observe(viewLifecycleOwner){
            try{
                if(it.first){
                adapter.notifyItemChanged(it.second, it.third)
            }
                progressBarIsDisplayed(false)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }
        viewModel.liveDataNeedToChangeStatus.observe(viewLifecycleOwner){
            try{
                binding.tvOrderDescriptionOrderStatus.text = it
                progressBarIsDisplayed(false)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemRemove.observe(viewLifecycleOwner) {
            try{
                if (it.first) {
                    adapter.deleteItem(it.second)
                }
                progressBarIsDisplayed(false)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        binding.ivOrderDescriptionBack.setOnClickListener {
            try{
                findNavController().popBackStack()
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        binding.srOrderItems.setOnRefreshListener {
            refresh(orderResponse.id)
        }

        binding.llOrderDescriptionClickableOrderStatus.setOnClickListener{
            try{
                statusClickListener(orderResponse)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        viewModel.getOrderItems(orderResponse.id, pref.getToken())
    }

    private fun refresh(id : Int){
        binding.srOrderItems.isRefreshing = false
        progressBarIsDisplayed(true)
        viewModel.getOrderItems(id, pref.getToken())
    }

    private fun statusClickListener(item: GetOrderResponse) {
        try{
            val orderStatusDialogFragment = OrderDescriptionStatusDialogFragment(item)

            childFragmentManager.setFragmentResultListener(
                "ORDER_DESCRIPTION",
                viewLifecycleOwner
            ) { resultKey, bundle ->

                if (resultKey == "ORDER_DESCRIPTION") {
                    progressBarIsDisplayed(true)
                    val orderResponse =
                        bundle.getSerializable("ORDER_DESCRIPTION_STATUS") as GetOrderResponse
                    viewModel.updateStatus(orderResponse.id, orderResponse.status, pref.getToken())
                }
            }
            orderStatusDialogFragment.show(childFragmentManager, "kek")
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun clickListener(item: GetOrderItemResponse, position: Int) {
        try{
            val basketDialogFragment = OrderDescriptionDialogFragment(1u, 10u, item)

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
                    viewModel.updateOrderItem(updateItem, pref.getToken(), position, orderItemResponse)
                }
            }
            basketDialogFragment.show(childFragmentManager, "kek")
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun setFullPrice(price: BigDecimal) {
        try{
            binding.tvOrderDescriptionFullPrice.text = price.toString()
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }


    private fun itemLongClickListener(item: GetOrderItemResponse) {
        try{
            val mainText = resources.getString(R.string.delete_book)
            val basketSheetItem = DeleteItemSheetDialogFragment(mainText) { deleteItemClickListener(item) }
            basketSheetItem.show(childFragmentManager, "FEEZE")
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun deleteItemClickListener(item: GetOrderItemResponse) {
        try{
            progressBarIsDisplayed(true)
            viewModel.deleteOrderItem(item.id, pref.getToken(), item)
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try{
            when (isDisplayed) {
                true -> {
                    binding.clOrderDescriptionProgressBar.visibility = View.VISIBLE
                    binding.clOrderDescriptionProgressBar2.visibility = View.VISIBLE
                }
                false -> {
                    binding.clOrderDescriptionProgressBar.visibility = View.GONE
                    binding.clOrderDescriptionProgressBar2.visibility = View.GONE
                }
            }
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
}