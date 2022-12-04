package com.example.kursachclient.presentation.fragment.basket_fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kursachclient.R
import com.example.kursachclient.domain.instance.RetrofitInstance
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import java.math.BigDecimal
import java.math.RoundingMode

//private val longClickListener: (GetBasketResponse, Int) -> Unit
class BasketAdapter(
    private val basketList: MutableList<GetBasketResponse>,
    private val countItemClickListener: (GetBasketResponse, Int) -> Unit,
    private val showFullPrice: (BigDecimal) -> Unit,
    private val hideOrReviewBasketComplete: (BigDecimal) -> Unit,
    private val itemLongClickListener: (GetBasketResponse) -> Unit
) : RecyclerView.Adapter<BasketAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_basket, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(item: GetBasketResponse){
        basketList.remove(item)
        notifyDataSetChanged()

        val fullPrice = calculateFullPrice(basketList)
        hideOrReviewBasketComplete(fullPrice)
        showFullPrice(fullPrice)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(basketList[position], position, basketList)
    }

    override fun getItemCount(): Int = basketList.size

    private fun calculateFullPrice(list: List<GetBasketResponse>) : BigDecimal{
        var result : Double = 0.0
        list.filter { it -> it.count > 0u}.forEach{ it ->  result += ( it.book.price * it.count.toDouble())}
        return result.toBigDecimal().setScale(2, RoundingMode.UP)
    }

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val mainCardView: CardView = itemView.findViewById(R.id.cv_basket_main_card_view)
        private val mainImage: ImageView = itemView.findViewById(R.id.iv_basket_main_image)
        private val name: TextView = itemView.findViewById(R.id.tv_basket_name)
        private val price: TextView = itemView.findViewById(R.id.tv_basket_price)
        private val count: TextView = itemView.findViewById(R.id.tv_basket_count)


        private val resultPriceItem: TextView =
            itemView.findViewById(R.id.tv_basket_full_price_item)
        private val linearLayoutClickable: LinearLayout =
            itemView.findViewById(R.id.ll_basket_static_count_count)

        private val noItemsTextView: TextView =
            itemView.findViewById(R.id.tv_basket_static_no_items)

        fun bind(item: GetBasketResponse, position: Int, list: List<GetBasketResponse>) {
            val fullPrice = calculateFullPrice(list)
            hideOrReviewBasketComplete(fullPrice)
            showFullPrice(fullPrice)
            name.text = item.book.name
            count.text = item.count.toString()
            price.text = item.book.price.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            resultPriceItem.text = (item.count.toDouble() * item.book.price).toBigDecimal()
                .setScale(2, RoundingMode.UP).toDouble().toString()

            mainCardView.setOnLongClickListener{
                itemLongClickListener(item)
                true
            }

            if (item.count > 0u) {
                mainCardView.foreground = ColorDrawable(Color.TRANSPARENT)
                noItemsTextView.visibility = View.GONE
                linearLayoutClickable.setOnClickListener {
                    countItemClickListener(item, position)
                }
            }
            if (item.count == 0u) {
                mainCardView.foreground = ColorDrawable(Color.parseColor("#BBffffff"))
                linearLayoutClickable.setOnClickListener {
                    countItemClickListener(item, position)
                }
            }

            if (item.book.image == null) {
                Glide.with(itemView).load(R.drawable.no_photos)
                    .placeholder(R.drawable.ic_baseline_image_search_24).into(mainImage)
            } else {
                Glide.with(itemView).load(RetrofitInstance.URL + item.book.image.url)
                    .placeholder(R.drawable.ic_baseline_image_search_24).centerCrop()
                    .into(mainImage)
            }

        }
    }
}