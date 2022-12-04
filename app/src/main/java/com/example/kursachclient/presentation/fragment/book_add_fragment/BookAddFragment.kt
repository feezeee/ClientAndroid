package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookAddBinding
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.fragment.book_fragment.BookAdapter
import com.example.kursachclient.presentation.fragment.book_fragment.BookViewModel
import java.lang.Exception
import java.math.RoundingMode

class BookAddFragment : BaseFragment() {
    lateinit var binding: FragmentBookAddBinding
    lateinit var viewModel: BookAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = BookAddViewModel()
        pref = SharedPreference(requireContext())
        binding = FragmentBookAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            signOutAndRedirect()
        }
        viewModel.liveData.observe(viewLifecycleOwner) {
            progressBarIsDisplayed(false)
            findNavController().popBackStack()
        }

        binding.ivBookAddBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivBookAddAdd.setOnClickListener {
            progressBarIsDisplayed(true)
            if (binding.etBookAddBookName.text.isEmpty()) {
                binding.etBookAddBookName.error = "Не может быть пустым"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }
            if (binding.etBookAddBookTitle.text.isEmpty()) {
                binding.etBookAddBookTitle.error = "Не может быть пустым"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }

            val priceRegex = "^\\d+.\\d{1,2}".toRegex()
            val resultPrice = priceRegex.matchEntire(binding.etBookAddBookPrice.text.toString())
            if (resultPrice == null) {
                binding.etBookAddBookPrice.error = "Формат *.00"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }
            val resultPriceDecimal = resultPrice.value.toBigDecimal().setScale(2, RoundingMode.UP)
            if (resultPriceDecimal.toString() == "0.00") {
                binding.etBookAddBookPrice.error = "Минимальная цена 0.01"
                progressBarIsDisplayed(false)
                return@setOnClickListener
            }

            val book =
                AddBookRequest(
                    binding.etBookAddBookName.text.toString(),
                    binding.etBookAddBookTitle.text.toString(),
                    resultPriceDecimal.toDouble(),
                    null
                )
            viewModel.addBook(book, pref.getValue())
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        when (isDisplayed) {
            true -> {
                binding.ivBookAddBack.isEnabled = false
                binding.ivBookAddAdd.isEnabled = false
                binding.ivBookAddMainImage.isEnabled = false
                binding.clBookAddProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.ivBookAddBack.isEnabled = true
                binding.ivBookAddAdd.isEnabled = true
                binding.ivBookAddMainImage.isEnabled = true
                binding.clBookAddProgressBar.visibility = View.GONE
            }
        }
    }
}