package com.example.kursachclient.presentation.fragment.book_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar

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
        viewModel = BookViewModel()
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBarIsDisplayed(true)

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Snackbar.make(
                    binding.root,
                    "Do you want to exit?", Snackbar.LENGTH_SHORT
                )
                    .setAction("Exit") {
                        activity?.finish()
                    }.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        viewModel.liveData.observe(viewLifecycleOwner) { getBookResponseList ->
            Log.e("TAG", getBookResponseList.toString())
            Log.e("KEK", Looper.myLooper().toString())
            var adapter = BookAdapter(getBookResponseList) { clickLongListener(it) }
            binding.rvBookBooks.layoutManager = GridLayoutManager(context, 2)
            binding.rvBookBooks.adapter = adapter
            progressBarIsDisplayed(false)
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            signOutAndRedirect()
        }

        binding.fabAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_bookFragment_to_bookAddFragment)
        }

        binding.svBookSearchBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                progressBarIsDisplayed(true)
                Log.d("TAG", "onQueryTextSubmit: ")
                viewModel.getBooks(if(query.isNullOrEmpty()) null else query, pref.getValue())
                binding.svBookSearchBook.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    progressBarIsDisplayed(true)
                    viewModel.getBooks(null, pref.getValue())
                }
                return true
            }

        })

        viewModel.getBooks(null, pref.getValue())
    }

    private fun progressBarIsDisplayed(isDisplayed : Boolean){
        when(isDisplayed){
            true -> {
                binding.fabAddBook.isEnabled = false
                binding.clBookProgressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.fabAddBook.isEnabled = true
                binding.clBookProgressBar.visibility = View.GONE
            }
        }
    }

    private fun signOutAndRedirect(){
        this.showToast("Ошибка авторизации")
        pref.clearValue()
        findNavController().navigate(R.id.loginFragment)
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clickLongListener(item: GetBookResponse) {
        Log.e("TAG", item.name)
    }
}