package com.example.kursachclient.presentation.fragment.book_fragment

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.dook_description_fragment.BookDescriptionFragment
import kotlin.Exception

class BookAdapter(
    private val bookList: List<GetBookResponse>,
    private val longClickListener: (GetBookResponse) -> Unit
) : RecyclerView.Adapter<BookAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_book, parent, false)
        return DescriptionCoinViewHolder(view, longClickListener)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    class DescriptionCoinViewHolder(
        itemView: View,
        private val longClickListener: (GetBookResponse) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_Name)
        private val price: TextView = itemView.findViewById(R.id.tv_Price)
        private val mainImage: ImageView = itemView.findViewById(R.id.iv_MainImage)
        fun bind(item: GetBookResponse) {
            Log.e("KEK", Looper.myLooper().toString())
            name.text = item.name
            price.text = item.price.toString()

            if(item.image == null) {
                Glide.with(itemView).load(R.drawable.no_photos).placeholder(R.drawable.ic_baseline_image_search_24).into(mainImage)
            }
            else{
                Glide.with(itemView).load(RetrofitInstance.URL + item.image.url)
                    .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
            }

            itemView.setOnClickListener {
                try{
                    val favoriteFragment = BookDescriptionFragment()
                    var book = GetBookResponse(
                        id = item.id,
                        name = item.name,
                        title = item.title,
                        price = item.price,
                        image = item.image
                    )
                    var bundle = Bundle()
                    bundle.putSerializable("book", book)
                    Navigation.findNavController(itemView)
                        .navigate(R.id.action_bookFragment_to_bookDescriptionFragment, bundle)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            itemView.setOnLongClickListener {
                longClickListener(item)
                true
            }
        }
    }
}