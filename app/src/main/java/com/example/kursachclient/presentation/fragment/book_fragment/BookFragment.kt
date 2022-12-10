package com.example.kursachclient.presentation.fragment.book_fragment

import android.opengl.Visibility
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kursachclient.R
import com.example.kursachclient.SharedPreference
import com.example.kursachclient.databinding.FragmentBookBinding
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.BaseFragment
import com.example.kursachclient.presentation.sheet_dialog_fragment.delete_item.DeleteItemSheetDialogFragment

class BookFragment : BaseFragment() {
    lateinit var binding: FragmentBookBinding
    lateinit var viewModel: BookViewModel
    lateinit var adapter: BookAdapter

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

        viewModel.liveData.observe(viewLifecycleOwner) { getBookResponseList ->
            try{
                adapter = BookAdapter(pref.getRole(), getBookResponseList) { itemLongClickListener(it) }
                binding.rvBookBooks.layoutManager = GridLayoutManager(context, 1)
                binding.rvBookBooks.adapter = adapter
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataShowToast.observe(viewLifecycleOwner) { message ->
            try{
                showToast(message)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataSignOutAndRedirect.observe(viewLifecycleOwner) {
            try{
                signOutAndRedirect()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.srBookBooks.setOnRefreshListener {
            refresh()
        }

        binding.fabAddBook.setOnClickListener {
            try{
                findNavController().navigate(R.id.action_bookFragment_to_bookAddFragment)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        viewModel.liveDataNeedToNotifyItemRemove.observe(viewLifecycleOwner) {
            try{
                if (it.first) {
                    adapter.deleteItem(it.second)
                }
                progressBarIsDisplayed(false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.svBookSearchBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                try{
                    progressBarIsDisplayed(true)
                    Log.d("TAG", "onQueryTextSubmit: ")
                    viewModel.getBooks(if(query.isNullOrEmpty()) null else query, pref.getToken())
                    binding.svBookSearchBook.clearFocus()
                    return true
                }
                catch (e: Exception){
                    e.printStackTrace()
                    return true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                try{
                    if (newText.isNullOrEmpty()){
                        progressBarIsDisplayed(true)
                        viewModel.getBooks(null, pref.getToken())
                    }
                    return true
                }
                catch (e: Exception){
                    e.printStackTrace()
                    return true
                }
            }

        })

        viewModel.getBooks(null, pref.getToken())
    }

    override fun onStart() {
        super.onStart()

        try{
            when(pref.getRole().lowercase()){
                "user" -> {
                    binding.fabAddBook.visibility = View.GONE
                }
                "admin" -> {
                    binding.fabAddBook.visibility = View.VISIBLE
                }
                else -> {
                    binding.fabAddBook.visibility = View.VISIBLE
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun progressBarIsDisplayed(isDisplayed : Boolean){
        try{
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
        catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun refresh(){
        binding.srBookBooks.isRefreshing = false
        progressBarIsDisplayed(true)
        viewModel.getBooks(if(binding.svBookSearchBook.query.toString().isNullOrEmpty()) null else binding.svBookSearchBook.query.toString(), pref.getToken())
    }

    private fun itemLongClickListener(item: GetBookResponse) {
        try{
            val mainText = resources.getString(R.string.delete_book)
            val basketSheetItem = DeleteItemSheetDialogFragment(mainText) { deleteItemClickListener(item) }
            basketSheetItem.show(childFragmentManager, "FEEZE")
        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun deleteItemClickListener(item: GetBookResponse) {
        try{
            progressBarIsDisplayed(true)
            viewModel.deleteBook(item.id, pref.getToken(), item)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}