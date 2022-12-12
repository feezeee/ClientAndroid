package com.example.kursachclient.presentation.fragment.book_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kursachclient.R
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.book_description_fragment.BookDescriptionFragment
import java.math.RoundingMode
import kotlin.Exception

class BookAdapter(
    private val role: String,
    private val bookList: MutableList<GetBookResponse>,
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

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(item: GetBookResponse){
        bookList.remove(item)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder,
                                  position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    inner class DescriptionCoinViewHolder(
        itemView: View,
        private val longClickListener: (GetBookResponse) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.tv_book_item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_book_item_price)
        private val mainImageView: ImageView = itemView.findViewById(R.id.iv_book_item_main_image)

        fun bind(item: GetBookResponse) {
            try{
                nameTextView.text = item.name
                val priceStr = item.price.toBigDecimal()
                    .setScale(2, RoundingMode.UP).toString()
                priceTextView.text = priceStr


                if(item.image == null){
                    mainImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    Glide.with(itemView)
                        .load(R.drawable.no_photos)
                        .placeholder(R.drawable.ic_baseline_image_search_24)
                        .error(R.drawable.no_photos)
                        .into(mainImageView)
                }
                else{
                    mainImageView.scaleType = ImageView.ScaleType.FIT_XY
                    Glide.with(itemView)
                        .load(RetrofitInstance.URL + item.image?.url)
                        .placeholder(R.drawable.ic_baseline_image_search_24)
                        .error(R.drawable.no_photos)
                        .into(mainImageView)
                }


                itemView.setOnClickListener {
                    try{
                        val book = GetBookResponse(
                            id = item.id,
                            name = item.name,
                            title = item.title,
                            price = item.price,
                            image = item.image
                        )
                        val bundle = Bundle()
                        bundle.putSerializable("book", book)
                        Navigation.findNavController(itemView)
                            .navigate(R.id.action_bookFragment_to_bookDescriptionFragment, bundle)
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                when(role.lowercase()){
                    "user" -> {

                    }
                    "admin" -> {
                        itemView.setOnLongClickListener {
                            try {
                                longClickListener(item)
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                            true
                        }
                    }
                    else -> {
                        itemView.setOnLongClickListener {
                            try {
                                longClickListener(item)
                            }
                            catch (e: Exception)
                            {
                                e.printStackTrace()
                            }
                            true
                        }
                    }
                }


            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}