//package com.example.kursachclient.presentation.fragment.order_fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView.OnItemLongClickListener
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.navigation.Navigation
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.kursachclient.R
//import com.example.kursachclient.domain.Book
//import com.example.kursachclient.domain.instance.RetrofitInstance
//import com.example.kursachclient.domain.model.book.GetBookResponse
//import com.example.kursachclient.presentation.fragment.dook_description_fragment.BookDescriptionFragment
//import java.lang.Exception
//
//class OrderAdapter(
//    private val bookList: List<GetBookResponse>,
//    private val longClickListener: (GetBookResponse) -> Unit
//) : RecyclerView.Adapter<OrderAdapter.DescriptionCoinViewHolder>() {
//
////    override fun onCreateViewHolder(
////        parent: ViewGroup,
////        viewType: Int
////    ): DescriptionCoinViewHolder {
////        val layoutInflater = LayoutInflater.from(parent.context)
////        val view = layoutInflater.inflate(R.layout.item_book, parent, false)
////        return DescriptionCoinViewHolder(view, longClickListener)
////    }
////
////    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
////        holder.bind(bookList[position])
////    }
////
////    override fun getItemCount(): Int = bookList.size
////
////    class DescriptionCoinViewHolder(
////        itemView: View,
////        private val longClickListener: (GetBookResponse) -> Unit
////    ) : RecyclerView.ViewHolder(itemView) {
////        private val name: TextView = itemView.findViewById(R.id.tv_Name)
////        private val count: TextView = itemView.findViewById(R.id.tv_Count)
////        private val price: TextView = itemView.findViewById(R.id.tv_Price)
////        private val mainImage: ImageView = itemView.findViewById(R.id.iv_MainImage)
////        fun bind(item: GetBookResponse) {
////            name.text = item.name
////            count.text = item.count.toString()
////            price.text = item.price.toString()
////
////            if(item.image == null) {
////                Glide.with(itemView).load(R.drawable.no_photos).placeholder(R.drawable.ic_baseline_image_search_24).into(mainImage)
////            }
////            else{
////                Glide.with(itemView).load(RetrofitInstance.URL + item.image?.url)
////                    .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
////            }
////
//////            if(item.image != null)
//////            {
//////                try {
//////                    Glide.with(itemView).load(RetrofitInstance.URL + item.image.url)
//////                        .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
//////                } catch (e: Exception) {
//////
//////                }
//////            }
//////            else {
//////                try {
//////                    Glide.with(itemView).load(R.drawable.no_photos).into(mainImage)
//////                } catch (e: Exception) {
//////
//////                }
//////            }
////
////            itemView.setOnClickListener {
////                val favoriteFragment = BookDescriptionFragment()
////                var book = GetBookResponse(
////                    id = item.id,
////                    name = item.name,
////                    title = item.title,
////                    count = item.count,
////                    price = item.price,
////                    imageId = item.imageId,
////                    image = item.image
////                )
////                var bundle = Bundle()
////                bundle.putSerializable("book", book)
////                Navigation.findNavController(itemView)
////                    .navigate(R.id.action_bookFragment_to_bookDescriptionFragment, bundle)
////            }
////
////            itemView.setOnLongClickListener {
////                longClickListener(item)
////                true
////            }
////        }
////    }
//
//
//}