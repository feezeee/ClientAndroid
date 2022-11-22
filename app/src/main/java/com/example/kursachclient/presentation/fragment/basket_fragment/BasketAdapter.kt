package com.example.kursachclient.presentation.fragment.basket_fragment

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.presentation.dialog_fragment.basket.BasketDialogFragment
import java.math.RoundingMode

class BasketAdapter(
    private val basketList: List<GetBasketResponse>,
    private val clickListener: (GetBasketResponse) -> GetBasketResponse?
) : RecyclerView.Adapter<BasketAdapter.DescriptionCoinViewHolder>() {

    private val editedBasketList = mutableListOf<GetBasketResponse>()
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

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val mainImage: ImageView = itemView.findViewById(R.id.iv_basket_main_image)
        private val name: TextView = itemView.findViewById(R.id.tv_basket_name)
        private val price: TextView = itemView.findViewById(R.id.tv_basket_price)
        private val count: TextView = itemView.findViewById(R.id.tv_basket_count)
        private val resultPriceItem: TextView = itemView.findViewById(R.id.tv_basket_full_price_item)
        private val qwe: LinearLayout = itemView.findViewById(R.id.ll_basket_static_count_count)
//        private val btnMinus: ImageButton = itemView.findViewById(R.id.imgbtn_basket_minus)
//        private val btnPlus: ImageButton = itemView.findViewById(R.id.imgbtn_basket_plus)

        fun bind(item: GetBasketResponse) {
            name.text = item.book.name
            count.text = item.count.toString()
            price.text = item.book.price.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            resultPriceItem.text = (item.count.toDouble() * item.book.price).toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()

            qwe.setOnClickListener {
                var result = clickListener(item)
                if(result != null)
                {
                    Log.d("KEK", result.toString())
                }
                count.text = item.count.toString()
                price.text = item.book.price.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
                resultPriceItem.text = (item.count.toDouble() * item.book.price).toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            }

//            if(item.count == 0u)
//            {
//                btnMinus.isEnabled = false
//            }
//            if(item.count < item.book.count)
//            {
//                btnPlus.isEnabled = true
//            }
//            if(0u < item.count && item.count < item.book.count){
//                btnMinus.isEnabled = true
//                btnPlus.isEnabled = true
//            }
//            if(item.count == item.book.count){
//                btnPlus.isEnabled = false
//            }
//
//            btnMinus.setOnClickListener {
//                if(btnMinus.isEnabled) {
//                    var existEditedItem = editedBasketList.firstOrNull() { it -> it.book.id == item.book.id }
//                    if(existEditedItem == null){
//                        if(item.count > 0u) {
//                            item.count--
//                            count.text = item.count.toString()
//                            editedBasketList.add(item)
//                        }
//                    }
//                    else {
//                        if(item.count > 0u) {
//                            item.count--
//                            count.text = item.count.toString()
//                        }
//                    }
//                    if(item.count == 0u)
//                    {
//                        btnMinus.isEnabled = false
//                    }
//                    if(item.count < item.book.count)
//                    {
//                        btnPlus.isEnabled = true
//                    }
//                    if(0u < item.count && item.count < item.book.count){
//                        btnMinus.isEnabled = true
//                        btnPlus.isEnabled = true
//                    }
//                    if(item.count == item.book.count){
//                        btnPlus.isEnabled = false
//                    }
//                }
//            }
//
//            btnPlus.setOnClickListener {
//                if(btnPlus.isEnabled) {
//                    var existEditedItem = editedBasketList.firstOrNull() { it -> it.book.id == item.book.id }
//                    if(existEditedItem == null){
//                        if(item.count < item.book.count) {
//                            item.count++
//                            count.text = item.count.toString()
//                            editedBasketList.add(item)
//                        }
//                    }
//                    else {
//                        if(item.count < item.book.count) {
//                            item.count++
//                            count.text = item.count.toString()
//                        }
//                    }
//                    if(item.count == 0u)
//                    {
//                        btnMinus.isEnabled = false
//                    }
//                    if(item.count < item.book.count)
//                    {
//                        btnPlus.isEnabled = true
//                    }
//                    if(0u < item.count && item.count < item.book.count){
//                        btnMinus.isEnabled = true
//                        btnPlus.isEnabled = true
//                    }
//                    if(item.count == item.book.count){
//                        btnPlus.isEnabled = false
//                    }
//                }
//            }

            if(item.book.image == null) {
                Glide.with(itemView).load(R.drawable.no_photos).placeholder(R.drawable.ic_baseline_image_search_24).into(mainImage)
            }
            else{
                Glide.with(itemView).load(RetrofitInstance.URL + item.book.image?.url)
                    .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop().into(mainImage)
            }

        }
    }

    fun getEditedItems() : List<GetBasketResponse>{
        return editedBasketList
    }
}