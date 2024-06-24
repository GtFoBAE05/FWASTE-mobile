package com.example.ta_mobile.ui.buyer.order

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponseData
import com.example.ta_mobile.databinding.ItemOrderStatusCardLayoutBinding
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DateTimeHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerOrderStatusAdapter(private val listener : (OrderStatusResponseData) -> Unit) : RecyclerView.Adapter<BuyerOrderStatusAdapter.BuyerOrderStatusViewHolder>(){
    private var items = emptyList<OrderStatusResponseData>()
    class BuyerOrderStatusViewHolder(val binding : ItemOrderStatusCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : OrderStatusResponseData){
            Glide.with(binding.root).load(item.storeImageUrl).into(binding.orderStatusCardImageView)
            binding.orderStatusCardTitleTv.text = item.storeName
            binding.orderStatusCardTitleTvDateTime.text = DateTimeHelper.convertDateWithTime(item.createdAt)
            binding.orderStatusCardTitleTvMenu.text = item.productNames
            binding.orderStatusCardTotalItemTv.text = CurrencyHelper.convertToRupiah(item.totalAmount)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerOrderStatusViewHolder {
        return BuyerOrderStatusViewHolder(ItemOrderStatusCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerOrderStatusViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }

    }
    fun setData(data : List<OrderStatusResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}