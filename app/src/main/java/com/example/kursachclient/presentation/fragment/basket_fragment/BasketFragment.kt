package com.example.kursachclient.presentation.fragment.basket_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBasketBinding
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.presentation.dialog_fragment.basket.BasketDialogFragment
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.math.BigDecimal


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
        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            adapter = BasketAdapter(
                it,
                { basket, position ->
                    clickListener(basket, position)
                },
                { coast -> setFullPrice(coast) },
                { coast -> hideOrReviewBasketComplete(coast) })
            binding.rvBasketItems.layoutManager = GridLayoutManager(context, 1)
            binding.rvBasketItems.adapter = adapter
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

        binding.tvBasketClearBasket.setOnClickListener {
            progressBarIsDisplayed(true)
            viewModel.clearBasket(pref.getValue())
        }

        binding.fabBasketCompleteBasket.setOnClickListener {
            progressBarIsDisplayed(true)
            viewModel.makeOrder(pref.getValue())
        }

        // Добавить проверку на pref
        viewModel.getBasket(pref.getValue())
    }

    private fun clickListener(item: GetBasketResponse, position: Int) {
        var basketDialogFragment = BasketDialogFragment(0u, 10u, item)

        childFragmentManager.setFragmentResultListener(
            "REQUEST_FEEZE",
            viewLifecycleOwner
        ) { resultKey, bundle ->

            if (resultKey == "REQUEST_FEEZE") {
                progressBarIsDisplayed(true)
                val basketItem = bundle.getSerializable("BASKET_ITEM") as GetBasketResponse

                val item = AddBookToBasketRequest(basketItem.book.id, basketItem.count)

                viewModel.addOrRemoveItemFromBasket(item, pref.getValue(), position, basketItem)
            }
        }
        basketDialogFragment.show(childFragmentManager, "kek")
    }

    private fun setFullPrice(price: BigDecimal) {
        binding.tvBasketFullPrice.text = price.toString()
    }

    private fun hideOrReviewBasketComplete(price: BigDecimal) {
        if (price.toInt() == 0) {
            binding.fabBasketCompleteBasket.visibility = View.GONE
        } else if (price.toInt() > 0) {
            binding.fabBasketCompleteBasket.visibility = View.VISIBLE
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
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
}