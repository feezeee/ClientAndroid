package com.example.kursachclient.presentation.fragment.book_description_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.book.UpdateBookRequest
import com.example.kursachclient.presentation.fragment.BaseFragment
import java.math.RoundingMode

class BookDescriptionFragment : BaseFragment() {
    lateinit var binding: FragmentBookDescriptionBinding
    lateinit var viewModel: BookDescriptionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        binding = FragmentBookDescriptionBinding.inflate(inflater, container, false)
        viewModel = BookDescriptionViewModel()
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBarIsDisplayed(true)
        val book = arguments?.getSerializable("book") as GetBookResponse
        viewModel.liveData.observe(viewLifecycleOwner) {
            progressBarIsDisplayed(false)
            Log.e("TAG", it.toString())
            binding.etBookDescriptionBookName.setText(it.name)
            binding.etBookDescriptionBookTitle.setText(it.title)
            if(it.image == null) {
                Glide.with(view)
                    .load(R.drawable.no_photos)
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_image_search_24)
                    .into(binding.imBookDescriptionMainImage)
            }
            else{
                Glide.with(view)
                    .load(RetrofitInstance.URL + it.image.url)
                    .placeholder(R.drawable.ic_baseline_image_search_24)
                    .centerCrop()
                    .into(binding.imBookDescriptionMainImage)
            }
            binding.etBookDescriptionBookPrice.setText(it.price.toBigDecimal()
            .setScale(2, RoundingMode.UP).toString())
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            signOutAndRedirect()
        }
        viewModel.liveDataNeedToNotifyGoneProgressBar.observe(viewLifecycleOwner) {
            progressBarIsDisplayed(false)
        }

        binding.btnBookDescriptionAddToBasket.setOnClickListener {
            val addBookToBasketRequest = AddBookToBasketRequest(book.id, 1u)
            viewModel.addOrRemoveFromBasket(addBookToBasketRequest, pref.getValue())
        }

        binding.ivBookDescriptionUpdate.setOnClickListener {
            progressBarIsDisplayed(true)
            if (binding.etBookDescriptionBookName.text.isEmpty()) {
                binding.etBookDescriptionBookName.error = "Не может быть пустым"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }
            if (binding.etBookDescriptionBookTitle.text.isEmpty()) {
                binding.etBookDescriptionBookTitle.error = "Не может быть пустым"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }

            val priceRegex = "^\\d+.\\d{1,2}".toRegex()
            val resultPrice = priceRegex.matchEntire(binding.etBookDescriptionBookPrice.text.toString())
            if (resultPrice == null) {
                binding.etBookDescriptionBookPrice.error = "Формат *.00"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }
            val resultPriceDecimal = resultPrice.value.toBigDecimal().setScale(2, RoundingMode.UP)
            if (resultPriceDecimal.toString() == "0.00") {
                binding.etBookDescriptionBookPrice.error = "Минимальная цена 0.01"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }

            var updatedBook = UpdateBookRequest(
                book.id,
                binding.etBookDescriptionBookName.text.toString(),
                binding.etBookDescriptionBookTitle.text.toString(),
                resultPriceDecimal.toDouble(),
                null)

            viewModel.updateBook(updatedBook, pref.getValue())
        }


        binding.ivBookDescriptionBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getBookById(book.id, pref.getValue())

    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        when (isDisplayed) {
            true -> {
                binding.imBookDescriptionMainImage.isEnabled = false
                binding.ivBookDescriptionUpdate.isEnabled = false
                binding.clBookDescriptionProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.imBookDescriptionMainImage.isEnabled = true
                binding.ivBookDescriptionUpdate.isEnabled = true
                binding.clBookDescriptionProgressBar.visibility = View.GONE
            }
        }
    }
}