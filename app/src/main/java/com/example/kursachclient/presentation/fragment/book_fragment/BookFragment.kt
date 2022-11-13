package com.example.kursachclient.presentation.fragment.book_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.domain.model.book.GetBookResponse

class BookFragment : Fragment() {
    lateinit var binding: FragmentBookBinding
    lateinit var pref: SharedPreference
    lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = BookViewModel(pref)
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            var adapter = BookAdapter(it) { clickLongListener(it) }
            binding.rvBooks.layoutManager = GridLayoutManager(context, 2)
            binding.rvBooks.adapter = adapter
        }
        viewModel.liveDataToast.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            showToast(it)
        }
        viewModel.liveDataExit.observe(viewLifecycleOwner) {
            if (it == true) {
                // Делаем разлогин
            }
        }
        binding.fabAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_bookFragment_to_bookAddFragment)
        }
        viewModel.getBooks()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun clickLongListener(item: GetBookResponse) {
        Log.e("TAG", "${item.name}")
    }
}