package com.example.kursachclient.presentation.fragment.basket_fragment

import android.os.Bundle
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
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.fragment.dook_description_fragment.BookDescriptionFragment
import org.w3c.dom.Text
import java.lang.Exception

class BasketAdapter(
    private val basketList: List<GetBasketResponse>
) : RecyclerView.Adapter<BasketAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_basket, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(basketList[position])
    }

    override fun getItemCount(): Int = basketList.size

    class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val mainImage: ImageView = itemView.findViewById(R.id.iv_main_image)
        private val name: TextView = itemView.findViewById(R.id.tv_name)
        private val price: TextView = itemView.findViewById(R.id.tv_price)
        private val count: TextView = itemView.findViewById(R.id.tv_count)

        fun bind(item: GetBasketResponse) {
            name.text = item.book.name
            count.text = item.count.toString()
            price.text = item.book.price.toString()

            if(item.book.image == null) {
                Glide.with(itemView).load(R.drawable.no_photos).placeholder(R.drawable.ic_baseline_image_search_24).into(mainImage)
            }
            else{
                Glide.with(itemView).load(RetrofitInstance.URL + item.book.image?.url)
                    .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
            }

//            if(item.image != null)
//            {
//                try {
//                    Glide.with(itemView).load(RetrofitInstance.URL + item.image.url)
//                        .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
//                } catch (e: Exception) {
//
//                }
//            }
//            else {
//                try {
//                    Glide.with(itemView).load(R.drawable.no_photos).into(mainImage)
//                } catch (e: Exception) {
//
//                }
//            }

//            itemView.setOnClickListener {
//                val favoriteFragment = BookDescriptionFragment()
//                var book = GetBookResponse(
//                    id = item.id,
//                    name = item.name,
//                    title = item.title,
//                    count = item.count,
//                    price = item.price,
//                    imageId = item.imageId,
//                    image = item.image
//                )
//                var bundle = Bundle()
//                bundle.putSerializable("book", book)
//                Navigation.findNavController(itemView)
//                    .navigate(R.id.action_bookFragment_to_bookDescriptionFragment, bundle)
//            }
//
//            itemView.setOnLongClickListener {
//                longClickListener(item)
//                true
//            }
        }
    }


}