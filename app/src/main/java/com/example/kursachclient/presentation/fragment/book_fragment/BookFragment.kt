package com.example.kursachclient.presentation.fragment.book_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding

class BookFragment: Fragment() {
    lateinit var binding: FragmentBookBinding
    lateinit var pref: SharedPreference
    lateinit var viewModel : BookViewModel

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
        viewModel.liveData.observe(viewLifecycleOwner){
            Log.e("TAG", it.toString())
            var adapter = BookAdapter(it)
            binding.rvBooks.layoutManager = GridLayoutManager(context, 2)
            binding.rvBooks.adapter = adapter
        }
        viewModel.liveDataToast.observe(viewLifecycleOwner){
            Log.e("TAG", it.toString())
            showToast(it)
        }
        viewModel.liveDataExit.observe(viewLifecycleOwner){
            if(it == true){
                // Делаем разлогин
            }
        }
        viewModel.getBooks()
    }

    fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}