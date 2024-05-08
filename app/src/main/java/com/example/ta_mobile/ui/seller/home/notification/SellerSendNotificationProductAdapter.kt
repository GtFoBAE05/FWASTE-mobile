package com.example.ta_mobile.ui.seller.home.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponseData
import com.example.ta_mobile.databinding.ChooseProductCardLayoutBinding
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.DiffUtil

class SellerSendNotificationProductAdapter(val listener : (SellerGetMyProductResponseData) -> Unit) : RecyclerView.Adapter<SellerSendNotificationProductAdapter.SellerIncomingOrderViewHolder>(){
    private var items = emptyList<SellerGetMyProductResponseData>()
    class SellerIncomingOrderViewHolder(val binding : ChooseProductCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SellerGetMyProductResponseData){
            binding.chooseProductImageView.visible()
            Glide.with(binding.root).load(item.imageUrl).into(binding.chooseProductImageView)
            binding.chooseProductTitleTv.text = item.name
            binding.chooseProductArrow.gone()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerIncomingOrderViewHolder {
        return SellerIncomingOrderViewHolder(ChooseProductCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SellerIncomingOrderViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }

    }
    fun setData(data : List<SellerGetMyProductResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}