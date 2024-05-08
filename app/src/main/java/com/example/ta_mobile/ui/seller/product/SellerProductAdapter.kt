package com.example.ta_mobile.ui.seller.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponseData
import com.example.ta_mobile.databinding.ItemSellerProductCardLayoutBinding
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class SellerProductAdapter(val listener : (SellerGetMyProductResponseData) -> Unit) : RecyclerView.Adapter<SellerProductAdapter.SellerProductViewHolder>(){
    private var items = emptyList<SellerGetMyProductResponseData>()
    class SellerProductViewHolder(val binding : ItemSellerProductCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SellerGetMyProductResponseData){
            Glide.with(binding.root).load(item.imageUrl).into(binding.itemSellerProductCardImageView)
            binding.itemSellerProductCardTitleTv.text = item.name
            binding.itemSellerProductCardPriceTv.text = CurrencyHelper.convertToRupiah(item.price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        return SellerProductViewHolder(ItemSellerProductCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
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