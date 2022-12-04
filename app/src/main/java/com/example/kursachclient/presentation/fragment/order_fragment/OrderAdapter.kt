package com.example.kursachclient.presentation.fragment.order_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kursachclient.R
import com.example.kursachclient.domain.model.order.GetOrderResponse
import java.math.RoundingMode

class OrderAdapter(
    private val orderList: MutableList<GetOrderResponse>,
    private val itemLongClickListener: (GetOrderResponse) -> Unit
) : RecyclerView.Adapter<OrderAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_order, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    fun deleteItem(item: GetOrderResponse){
        orderList.remove(item)
        notifyDataSetChanged()
//        notifyItemRemoved(position)
    }

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView: TextView = itemView.findViewById(R.id.tv_order_item_full_name)
        private val phoneNumberTextView: TextView =
            itemView.findViewById(R.id.tv_order_item_phone_number)
        private val fullPriceTextView: TextView =
            itemView.findViewById(R.id.tv_order_item_full_price)

        @SuppressLint("SetTextI18n")
        fun bind(item: GetOrderResponse) {
            fullNameTextView.text = "${item.firstName} ${item.lastName}"
            phoneNumberTextView.text = item.phoneNumber
            fullPriceTextView.text =
                item.fullPrice.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            itemView.setOnClickListener {
                var bundle = Bundle()
                bundle.putSerializable("order", item)
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_orderFragment_to_orderDescriptionFragment, bundle)
            }
            itemView.setOnLongClickListener {
                itemLongClickListener(item)
                true
            }
        }
    }


}