package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kursachclient.R
import com.example.kursachclient.domain.model.basket.GetBasketResponse
import com.example.kursachclient.domain.model.book.GetBookResponse
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import java.math.BigDecimal
import java.math.RoundingMode

class OrderDescriptionAdapter(
    private val orderItemList: MutableList<GetOrderItemResponse>,
    private val clickListener: (GetOrderItemResponse, Int) -> Unit,
    private val showFullPrice: (BigDecimal) -> Unit,
    private val longClickListener: (GetOrderItemResponse) -> Unit
) : RecyclerView.Adapter<OrderDescriptionAdapter.DescriptionCoinViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_order_description, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(item: GetOrderItemResponse) {
        try {
            orderItemList.remove(item)
            notifyDataSetChanged()
            val fullPrice = calculateFullPrice(orderItemList)
            showFullPrice(fullPrice)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateFullPrice(list: List<GetOrderItemResponse>): BigDecimal {
        try{
            var result: Double = 0.0
            list.filter { it -> it.count > 0u }
                .forEach { it -> result += (it.price * it.count.toDouble()) }
            return result.toBigDecimal().setScale(2, RoundingMode.UP)
        }
        catch (e: Exception){
            e.printStackTrace()
            return 0.00.toBigDecimal()
        }
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(orderItemList[position], position, orderItemList)
    }

    override fun getItemCount(): Int = orderItemList.size

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView =
            itemView.findViewById(R.id.tv_order_description_item_name)
        private val priceTextView: TextView =
            itemView.findViewById(R.id.tv_order_description_item_price)
        private val countTextView: TextView =
            itemView.findViewById(R.id.tv_order_description_item_count)
        private val calculateFullPriceTextView: TextView =
            itemView.findViewById(R.id.tv_order_description_item_calculate_price)
        private val countLinearLayout: LinearLayout =
            itemView.findViewById(R.id.ll_order_description_static_count_count)

        @SuppressLint("SetTextI18n")
        fun bind(item: GetOrderItemResponse, position: Int, list: List<GetOrderItemResponse>) {
            try{
                val fullPrice = calculateFullPrice(list)
                showFullPrice(fullPrice)
                nameTextView.text = item.name
                priceTextView.text =
                    item.price.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                countTextView.text = item.count.toString()
                calculateFullPriceTextView.text = (item.count.toDouble() * item.price).toBigDecimal()
                    .setScale(2, RoundingMode.UP).toString()

                countLinearLayout.setOnClickListener {
                    try {
                        clickListener(item, position)
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                itemView.setOnLongClickListener {
                    try {
                        longClickListener(item)
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                    true
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}