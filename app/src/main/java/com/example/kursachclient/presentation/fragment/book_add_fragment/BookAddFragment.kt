package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookAddBinding
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.presentation.fragment.BaseFragment
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
        viewModel.liveData.observe(viewLifecycleOwner) {
            try {
                progressBarIsDisplayed(false)
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivBookAddBack.setOnClickListener {
            try {
                findNavController().popBackStack()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.ivBookAddAdd.setOnClickListener {
            try {
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
                viewModel.addBook(book, pref.getToken())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun progressBarIsDisplayed(isDisplayed: Boolean) {
        try {
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
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}