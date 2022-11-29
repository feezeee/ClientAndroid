package com.example.kursachclient.presentation.fragment.dook_description_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.AddBookToBasketRequest
import com.example.kursachclient.domain.model.book.GetBookResponse
import java.lang.Exception

class BookDescriptionFragment : Fragment() {
    lateinit var binding: FragmentBookDescriptionBinding
    lateinit var viewModel: BookDescriptionViewModel
    lateinit var pref: SharedPreference
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPrefToken()
        var book = arguments?.getSerializable("book") as GetBookResponse
        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            binding.tvDescriptionBookName.text = it.name
            binding.tvDescriptionBookTitle.text = it.title
            if(it.image != null)
            {
                try {
                    Glide.with(view).load(RetrofitInstance.URL + it.image.url)
                        .placeholder(R.drawable.ic_baseline_image_search_24).into(binding.ivImage)
                } catch (e: Exception) {

                }
            }
            else{
                try {
                    Glide.with(view).load(R.drawable.no_photos).into(binding.ivImage)
                } catch (e: Exception) {

                }
            }
            binding.btnAddOrRemoveFromBasket.setOnClickListener{ btn ->
                var book = AddBookToBasketRequest(it.id, 1u)
                viewModel.addOrRemoveFromBasket(book, pref.getValue().toString())
            }
        }

        checkPrefToken()
        viewModel.getBookById(book.id, pref.getValue().toString())

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun checkPrefToken(){
        if(pref.getValue() == null){
            // Перекидываем на экран входа
            pref.clearValue()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}