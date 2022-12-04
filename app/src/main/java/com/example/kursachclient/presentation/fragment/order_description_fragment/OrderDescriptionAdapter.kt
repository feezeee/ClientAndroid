package com.example.kursachclient.presentation.fragment.order_description_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kursachclient.R
import com.example.kursachclient.domain.model.order_item.GetOrderItemResponse
import java.math.BigDecimal
import java.math.RoundingMode

class OrderDescriptionAdapter(
    private val orderItemList: List<GetOrderItemResponse>,
    private val clickListener: (GetOrderItemResponse, Int) -> Unit,
    private val showFullPrice: (BigDecimal) -> Unit
    ) : RecyclerView.Adapter<OrderDescriptionAdapter.DescriptionCoinViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_order_description, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(orderItemList[position], position, orderItemList)
    }

    override fun getItemCount(): Int = orderItemList.size

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_order_description_item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_order_description_item_price)
        private val countTextView: TextView = itemView.findViewById(R.id.tv_order_description_item_count)
        private val calculateFullPriceTextView: TextView = itemView.findViewById(R.id.tv_order_description_item_calculate_price)
        private val countLinearLayout: LinearLayout = itemView.findViewById(R.id.ll_order_description_static_count_count)
        fun bind(item: GetOrderItemResponse, position: Int, list: List<GetOrderItemResponse>) {
            val fullPrice = calculateFullPrice(list)
            showFullPrice(fullPrice)
            nameTextView.text = item.name
            priceTextView.text = item.price.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            countTextView.text = item.count.toString()
            calculateFullPriceTextView.text = (item.count.toDouble() * item.price).toBigDecimal()
                .setScale(2, RoundingMode.UP).toDouble().toString()

            countLinearLayout.setOnClickListener{
                clickListener(item, position)
            }
        }
        private fun calculateFullPrice(list: List<GetOrderItemResponse>) : BigDecimal{
            var result : Double = 0.0
            list.filter { it -> it.count > 0u}.forEach{ it ->  result += ( it.price * it.count.toDouble())}
            return result.toBigDecimal().setScale(2, RoundingMode.UP)
        }
    }
}