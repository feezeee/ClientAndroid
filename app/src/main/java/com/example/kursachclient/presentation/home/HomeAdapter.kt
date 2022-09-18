package com.example.kursachclient.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kursachclient.R
import com.example.kursachclient.domain.Book
import com.example.kursachclient.presentation.home.book_description.BookDescriptionFragment
import kotlinx.coroutines.channels.ticker
import java.io.Serializable

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

        private val name: TextView = itemView.findViewById(R.id.tv_name)
        private val count: TextView = itemView.findViewById(R.id.tv_count)

        fun bind(item: Book) {
            name.text = item.name
            count.text = item.count.toString()
            itemView.setOnClickListener {
                val favoriteFragment = BookDescriptionFragment()
                var book = Book(id = item.id, name = item.name, title = item.title, count = item.count)
                var bundle = Bundle()
                bundle.putSerializable("book", book)
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_homeFragment_to_bookDescriptionFragment, bundle)
            }
        }
    }


}