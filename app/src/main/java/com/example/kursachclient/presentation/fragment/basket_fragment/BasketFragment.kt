package com.example.kursachclient.presentation.fragment.basket_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBasketBinding
import com.example.kursachclient.databinding.FragmentBookAddBinding
import com.example.kursachclient.domain.model.book.AddBookRequest
import com.example.kursachclient.presentation.fragment.book_add_fragment.BookAddViewModel
import com.example.kursachclient.presentation.fragment.book_fragment.BookAdapter
import java.lang.Exception

class BasketFragment : Fragment() {
    lateinit var binding: FragmentBasketBinding
    lateinit var pref: SharedPreference
    lateinit var viewModel: BasketViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreference(requireContext())
        viewModel = BasketViewModel()
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
            var adapter = BasketAdapter(it)
            binding.rvBasketItems.layoutManager = GridLayoutManager(context, 1)
            binding.rvBasketItems.adapter = adapter
        }
        binding.tvClearBasket.setOnClickListener{
            // Добавить проверку на pref
            viewModel.clearBasket(pref.getValue().toString())
        }
        // Добавить проверку на pref
        viewModel.getBasket(pref.getValue().toString())
//        viewModel.liveDataToast.observe(viewLifecycleOwner) {
//            Log.e("TAG", it.toString())
//            showToast(it)
//        }
//        viewModel.liveDataExit.observe(viewLifecycleOwner) {
//            if (it == true) {
//                // Делаем разлогин
////                pref.clearValue()
//            }
//        }
//        viewModel.liveDataComplete.observe(viewLifecycleOwner) {
//            if (it == true) {
//                // Делаем разлогин
//                findNavController().navigateUp()
//            }
//        }
//        binding.btnAddBook.setOnClickListener {
//            // Делаем валидацию
//
//            var book =
//                AddBookRequest(
//                    binding.etName.text.toString(),
//                    binding.etTitle.text.toString(),
//                    binding.etPrice.text.toString().toDouble(),
//                    binding.etCount.text.toString().toUInt(),
//                    null
//                )
//            viewModel.addBook(book, pref.getValue().toString())
//            try {
//
//            }
//            catch (ex: Exception){
//
//            }
//        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}