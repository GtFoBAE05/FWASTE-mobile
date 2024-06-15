package com.example.ta_mobile.ui.buyer.search.product

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.product.SearchProductResponseData
import com.example.ta_mobile.databinding.ItemSearchStoreCardLayoutBinding
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerSearchProductAdapter(private val listener: (SearchProductResponseData) -> Unit) :
    RecyclerView.Adapter<BuyerSearchProductAdapter.BuyerSearchProductViewHolder>() {
    private var items = emptyList<SearchProductResponseData>()

    class BuyerSearchProductViewHolder(val binding: ItemSearchStoreCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchProductResponseData) {
            Glide.with(binding.root).load(item.imageUrl).into(binding.SearchStoreCardImageView)
            binding.SearchStoreCardStoreName.text = item.name
            binding.SearchStoreCardStoreRating.text = CurrencyHelper.convertToRupiah(item.price)
            binding.SearchStoreCardStoreAddress.text = CurrencyHelper.convertToRupiah(item.originalPrice)
            binding.SearchStoreCardStoreAddress.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG 
            binding.SearchStoreCardStoreRating.setTypeface(null, Typeface.BOLD)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerSearchProductViewHolder {
        return BuyerSearchProductViewHolder(
            ItemSearchStoreCardLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BuyerSearchProductViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(data: List<SearchProductResponseData>) {
        val diffUtil = DiffUtil(items, data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }



}


