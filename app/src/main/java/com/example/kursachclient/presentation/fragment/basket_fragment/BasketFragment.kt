package com.example.kursachclient.presentation.fragment.basket_fragment

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBasketBinding
import com.example.kursachclient.domain.model.basket.AddOrRemoveBookFromBasketRequest
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.presentation.dialog_fragment.basket.BasketDialogFragment
import java.math.BigDecimal

class BasketFragment : Fragment() {
    lateinit var binding: FragmentBasketBinding
    lateinit var pref: SharedPreference
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
        checkPrefToken()

        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
//            adapter = BasketAdapter(it) { basket, position ->
//                clickListener(basket, position)
//            }
            adapter = BasketAdapter(
                it,
                { basket, position ->
                clickListener(basket, position) },
                { coast -> setFullPrice(coast) },
                { coast -> hideOrReviewBasketComplete(coast) })
            binding.rvBasketItems.layoutManager = GridLayoutManager(context, 1)
            binding.rvBasketItems.adapter = adapter
        }
        binding.tvClearBasket.setOnClickListener {
            // Добавить проверку на pref
            viewModel.clearBasket(pref.getValue().toString())
        }
        // Добавить проверку на pref
        viewModel.getBasket(pref.getValue().toString())
//        viewModel.liveDataToast.observe(viewLifecycleOwner) {
//            Log.e("TAG", it.toString())
//            showToast(it)
//        }
        viewModel.liveDataExit.observe(viewLifecycleOwner) {
            if (it == true) {
                // Делаем разлогин
//                pref.clearValue()
            }
        }
//        viewModel.liveDataComplete.observe(viewLifecycleOwner) {
//            if (it == true) {
//                // Делаем разлогин
//                findNavController().navigateUp()
//            }
//        }
//        binding.btnAddBook.setOnClickListener {
//            // Делаем валидацию
//
//            var book =
//                AddBookRequest(
//                    binding.etName.text.toString(),
//                    binding.etTitle.text.toString(),
//                    binding.etPrice.text.toString().toDouble(),
//                    binding.etCount.text.toString().toUInt(),
//                    null
//                )
//            viewModel.addBook(book, pref.getValue().toString())
//            try {
//
//            }
//            catch (ex: Exception){
//
//            }
//        }
    }

    private fun clickListener(item: GetBasketResponse, position: Int) {

        var basketDialogFragment = BasketDialogFragment(0u, item.book.count, item)

        childFragmentManager.setFragmentResultListener(
            "REQUEST_FEEZE",
            viewLifecycleOwner
        ) { resultKey, bundle ->

            if (resultKey == "REQUEST_FEEZE") {
                val basketItem = bundle.getSerializable("BASKET_ITEM") as GetBasketResponse
                checkPrefToken()

                var item = AddOrRemoveBookFromBasketRequest(basketItem.book.id, basketItem.count)

                viewModel.addOrRemoveItemFromBasket(item, pref.getValue().toString())

                adapter.notifyItemChanged(position, basketItem)
            }
        }
        basketDialogFragment.show(childFragmentManager, "kek")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setFullPrice(price: BigDecimal){
        binding.tvBasketFullPrice.text = price.toString()
    }

    private fun hideOrReviewBasketComplete(price: BigDecimal){
        if(price.toInt() == 0){
            binding.fabBasketCompleteBasket.visibility = View.GONE
        }
        else if(price.toInt() > 0){
            binding.fabBasketCompleteBasket.visibility = View.VISIBLE
        }
    }

    private fun checkPrefToken(){
        if(pref.getValue() == null){
            // Перекидываем на экран входа
            pref.clearValue()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}