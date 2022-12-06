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
import com.example.kursachclient.domain.model.order.GetOrderResponse
import com.example.kursachclient.presentation.fragment.book_description_fragment.BookDescriptionFragment
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.Exception

class ProfileAdapter(
    private val orderList: MutableList<GetOrderResponse>
) : RecyclerView.Adapter<ProfileAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_profile_order, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionCoinViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    inner class DescriptionCoinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val registrationDateTextView: TextView =
            itemView.findViewById(R.id.tv_profile_order_item_registration_date)
        private val orderStatusTextView: TextView =
            itemView.findViewById(R.id.tv_profile_order_item_status)
        private val fullPriceTextView: TextView =
            itemView.findViewById(R.id.tv_profile_order_item_full_price)

        @SuppressLint("SetTextI18n")
        fun bind(item: GetOrderResponse) {
            try {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                registrationDateTextView.text =
                    Instant.ofEpochSecond(item.createdDate)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(formatter).toString()
                fullPriceTextView.text =
                    item.fullPrice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                orderStatusTextView.text = item.status
                if(orderStatusTextView.text.toString() == "Ожидает оплаты"){
//                    itemView.setOnClickListener {
//                        try {
//                            val bundle = Bundle()
//                            bundle.putSerializable("order", item)
//                            Navigation.findNavController(itemView)
//                                .navigate(
//                                    R.id.action_orderFragment_to_orderDescriptionFragment,
//                                    bundle
//                                )
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}