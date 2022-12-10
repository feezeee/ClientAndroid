package com.example.kursachclient.presentation.fragment.basket_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBasketBinding
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.presentation.dialog_fragment.basket.BasketDialogFragment
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.sheet_dialog_fragment.delete_item.DeleteItemSheetDialogFragment
import java.math.BigDecimal
import java.math.RoundingMode


class BasketFragment : BaseFragment() {
    lateinit var binding: FragmentBasketBinding
    lateinit var viewModel: BasketViewModel
    lateinit var adapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = BasketViewModel()
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBarIsDisplayed(true)
        binding.fabBasketCompleteBasket.setColorFilter(resources.getColor(R.color.white))
        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                Log.e("TAG", it.toString())
                if(it.size == 0){
                    setFullPrice(0.00.toBigDecimal())
                    hideOrReviewBasketComplete(0.00.toBigDecimal())
                }
                adapter = BasketAdapter(
                    it as MutableList<GetBasketResponse>,
                    { basket, position ->
                        countChangeClickListener(basket, position)
                    },
                    { coast -> setFullPrice(coast) },
                    { coast -> hideOrReviewBasketComplete(coast) },
                    { item -> itemLongClickListener(item) })
                binding.rvBasketItems.layoutManager = GridLayoutManager(context, 1)
                binding.rvBasketItems.adapter = adapter
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.srBasketItems.setOnRefreshListener {
            refresh()
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try {
                showToast(message)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try {
                signOutAndRedirect()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemChanged.observe(viewLifecycleOwner) {
            try {
                if (it.first) {
                    adapter.notifyItemChanged(it.second, it.third)
                }
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyBasketEmpty.observe(viewLifecycleOwner){
            try {
                setFullPrice(0.00.toBigDecimal())
                hideOrReviewBasketComplete(0.00.toBigDecimal())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemRemove.observe(viewLifecycleOwner) {
            try {
                if (it.first) {
                    adapter.deleteItem(it.second)
                }
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.tvBasketClearBasket.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                viewModel.clearBasket(pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.fabBasketCompleteBasket.setOnClickListener {
            try {
                progressBarIsDisplayed(true)
                viewModel.makeOrder(pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.getBasket(pref.getToken())
    }

    private fun refresh(){
        binding.srBasketItems.isRefreshing = false
        progressBarIsDisplayed(true)
        viewModel.getBasket(pref.getToken())
    }

    private fun countChangeClickListener(item: GetBasketResponse, position: Int) {
        try {
            val basketDialogFragment = BasketDialogFragment(0u, 10u, item)

            childFragmentManager.setFragmentResultListener(
                "REQUEST_FEEZE",
                viewLifecycleOwner
            ) { resultKey, bundle ->

                if (resultKey == "REQUEST_FEEZE") {
                    progressBarIsDisplayed(true)
                    val basketItem = bundle.getSerializable("BASKET_ITEM") as GetBasketResponse

                    val item = AddBookToBasketRequest(basketItem.book.id, basketItem.count)

                    viewModel.addOrRemoveItemFromBasket(item, pref.getToken(), position, basketItem)
                }
            }
            basketDialogFragment.show(childFragmentManager, "kek")
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun itemLongClickListener(item: GetBasketResponse) {
        try {
            val mainText = resources.getString(R.string.delete_item_from_basket)
            val basketSheetItem = DeleteItemSheetDialogFragment(mainText) { deleteItemClickListener(item) }
            basketSheetItem.show(childFragmentManager, "FEEZE")
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun deleteItemClickListener(item: GetBasketResponse) {
        try {
            progressBarIsDisplayed(true)
            viewModel.deleteItemFromBasket(item.book.id, pref.getToken(), item)
        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setFullPrice(price: BigDecimal) {
        try {
            binding.tvBasketFullPrice.text = price.setScale(2, RoundingMode.UP).toString()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun hideOrReviewBasketComplete(price: BigDecimal) {
        try {
            if (price.setScale(2, RoundingMode.UP).toString() == "0.00") {
                binding.fabBasketCompleteBasket.visibility = View.GONE
            } else if (price > 0.toBigDecimal()) {
                binding.fabBasketCompleteBasket.visibility = View.VISIBLE
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
            when (isDisplayed) {
                true -> {
                    binding.rvBasketItems.isEnabled = false
                    binding.tvBasketClearBasket.isEnabled = false
                    binding.fabBasketCompleteBasket.isEnabled = false
                    binding.clBasketProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.rvBasketItems.isEnabled = true
                    binding.tvBasketClearBasket.isEnabled = true
                    binding.fabBasketCompleteBasket.isEnabled = true
                    binding.clBasketProgressBar.visibility = View.GONE
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}