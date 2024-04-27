package com.example.ta_mobile.ui.seller.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponseData
import com.example.ta_mobile.databinding.ItemOrderStatusCardLayoutBinding
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class SellerOrderStatusAdapter(private val listener : (OrderStatusResponseData) -> Unit) : RecyclerView.Adapter<SellerOrderStatusAdapter.SellerOrderStatusViewHolder>(){
    private var items = emptyList<OrderStatusResponseData>()
    class SellerOrderStatusViewHolder(val binding : ItemOrderStatusCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : OrderStatusResponseData){
            binding.orderStatusCardImageView.gone()
            binding.orderStatusCardTitleTv.text = item.storeName
            binding.orderStatusCardTotalItemTv.text = CurrencyHelper.convertToRupiah(item.totalAmount)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerOrderStatusViewHolder {
        return SellerOrderStatusViewHolder(ItemOrderStatusCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SellerOrderStatusViewHolder, position: Int) {
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