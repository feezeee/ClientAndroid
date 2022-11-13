package com.example.kursachclient.presentation.fragment.dook_description_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import java.lang.Exception

class BookDescriptionFragment : Fragment() {
    lateinit var binding: FragmentBookDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var book = arguments?.getSerializable("book") as GetBookResponse
        binding.tvName.text = book.name
        binding.tvTitle.text = book.title
        if(book.image != null)
        {
            try {
                Glide.with(view).load(RetrofitInstance.URL + book.image?.url)
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
    }
}