package com.example.kursachclient.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.domain.Book
import com.example.kursachclient.domain.instance.RetrofitInstance
import java.lang.Exception

class HomeAdapter (
    private val bookList: List<Book>
): RecyclerView.Adapter<HomeAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_book, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.tv_Name)
        private val count: TextView = itemView.findViewById(R.id.tv_Count)
        private val price: TextView = itemView.findViewById(R.id.tv_Price)
        private val mainImage: ImageView = itemView.findViewById(R.id.iv_MainImage)

        fun bind(item: Book) {
            name.text = item.name
            count.text = item.count.toString()
            price.text = item.price.toString()
            try {
                Glide.with(itemView).load(RetrofitInstance.URL + item.images.first { it.isMain }.url).placeholder(R.drawable.ic_baseline_image_24).into(mainImage)
            }
            catch (e:Exception){

            }
            itemView.setOnClickListener {
                val favoriteFragment = BookDescriptionFragment()
                var book = Book(id = item.id, name = item.name, title = item.title, count = item.count, price = item.price, discountPercent = item.discountPercent, images = item.images)
                var bundle = Bundle()
                bundle.putSerializable("book", book)
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_homeFragment_to_bookDescriptionFragment, bundle)
            }
        }
    }


}