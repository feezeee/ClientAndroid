package com.example.kursachclient.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursachclient.databinding.FragmentBookDescriptionBinding
import com.example.kursachclient.databinding.FragmentHomeBinding
import com.example.kursachclient.domain.Book
import com.example.kursachclient.presentation.home.HomeAdapter
import com.example.kursachclient.presentation.home.HomeViewModel

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
        var book = arguments?.getSerializable("book") as Book
        binding.tvName.text = book.name
        binding.tvTitle.text = book.title
        binding.tvCount.text = book.count.toString()
    }
}