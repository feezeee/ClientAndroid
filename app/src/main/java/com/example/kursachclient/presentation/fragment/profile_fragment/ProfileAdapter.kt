package com.example.kursachclient.presentation.fragment.profile_fragment

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.kursachclient.R
import com.example.kursachclient.domain.model.order.GetOrderResponse
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.Exception

class ProfileAdapter(
    private val orderList: MutableList<GetOrderResponse>,
    private val payClickListener: (GetOrderResponse) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.DescriptionCoinViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionCoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_profile_order, parent, false)
        return DescriptionCoinViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        @RequiresApi(Build.VERSION_CODES.O)
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
                    itemView.setOnClickListener{
                        try {
                            payClickListener(item)
                            true
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}