package com.example.kursachclient.presentation.fragment.book_add_fragment

import android.os.Bundle
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
import com.example.kursachclient.presentation.fragment.book_fragment.BookAdapter
import com.example.kursachclient.presentation.fragment.book_fragment.BookViewModel
import java.lang.Exception

class BookAddFragment : Fragment() {
    lateinit var binding: FragmentBookAddBinding
    lateinit var pref: SharedPreference
    lateinit var viewModel: BookAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = BookAddViewModel()
        binding = FragmentBookAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveDataToast.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            showToast(it)
        }
        viewModel.liveDataExit.observe(viewLifecycleOwner) {
            if (it == true) {
                // Делаем разлогин
//                pref.clearValue()
            }
        }
        viewModel.liveDataComplete.observe(viewLifecycleOwner) {
            if (it == true) {
                // Делаем разлогин
                findNavController().navigateUp()
            }
        }
        binding.btnAddBook.setOnClickListener {
            // Делаем валидацию

            var book =
                AddBookRequest(
                    binding.etName.text.toString(),
                    binding.etTitle.text.toString(),
                    binding.etPrice.text.toString().toDouble(),
                    null
                )
            viewModel.addBook(book, pref.getValue().toString())
            try {

            }
            catch (ex: Exception){

            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}