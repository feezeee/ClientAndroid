package com.example.kursachclient.presentation.fragment.order_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.domain.model.book.GetBookResponse

class OrderFragment : Fragment() {
    lateinit var binding: FragmentBookBinding
    lateinit var pref: SharedPreference
    lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = OrderViewModel()
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPrefToken()
//        viewModel.liveData.observe(viewLifecycleOwner) {
//            Log.e("TAG", it.toString())
//            var adapter = BookAdapter(it) { clickLongListener(it) }
//            binding.rvBooks.layoutManager = GridLayoutManager(context, 2)
//            binding.rvBooks.adapter = adapter
//        }
//        viewModel.liveDataToast.observe(viewLifecycleOwner) {
//            Log.e("TAG", it.toString())
//            showToast(it)
//        }
//        viewModel.liveDataExit.observe(viewLifecycleOwner) {
//            if (it == true) {
//                // Делаем разлогин
//            }
//        }
//        binding.fabAddBook.setOnClickListener {
//            findNavController().navigate(R.id.action_bookFragment_to_bookAddFragment)
//        }
////        object : SearchView.OnQueryTextListener
//        binding.etSearchBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                Log.d("TAG", "onQueryTextSubmit: ")
//                checkPrefToken()
//                viewModel.getBooks(if(query.isNullOrEmpty()) null else query, pref.getValue().toString())
//                binding.etSearchBook.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrEmpty()){
//                    viewModel.getBooks(null, pref.getValue().toString())
//                    binding.etSearchBook.clearFocus()
//                }
//                return true
//            }
//
////            override fun onQueryTextChange(newText: String?): Boolean {
////                // if query text is change in that case we
////                // are filtering our adapter with
////                // new text on below line.
////                listAdapter.filter.filter(newText)
////                return false
////            }
//        })
//
//        viewModel.getBooks(null, pref.getValue().toString())
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun clickLongListener(item: GetBookResponse) {
        Log.e("TAG", "${item.name}")
    }

    private fun checkPrefToken(){
        if(pref.getValue() == null){
            // Перекидываем на экран входа
            pref.clearValue()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}