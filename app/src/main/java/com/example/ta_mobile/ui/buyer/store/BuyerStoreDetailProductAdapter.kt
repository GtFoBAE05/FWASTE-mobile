package com.example.ta_mobile.ui.buyer.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponseData
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponseProductsData
import com.example.ta_mobile.databinding.StoreProductCardLayoutBinding
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerStoreDetailProductAdapter(private val listener: (StoreDetailResponseProductsData) -> Unit) :
    RecyclerView.Adapter<BuyerStoreDetailProductAdapter.BuyerStoreDetailProductViewHolder>() {

    private var items = emptyList<StoreDetailResponseProductsData>()

    class BuyerStoreDetailProductViewHolder(val binding: StoreProductCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoreDetailResponseProductsData) {
            Glide.with(binding.root).load(item.imageUrl).into(binding.productCardItemImageView)
            binding.productCardItemTitleTv.setText(item.name)
            binding.productCardItemPriceTv.setText(CurrencyHelper.convertToRupiah(item.price))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyerStoreDetailProductViewHolder {
        return BuyerStoreDetailProductViewHolder(
            StoreProductCardLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerStoreDetailProductViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    fun setData(data: List<StoreDetailResponseProductsData>) {
        val diffUtil = DiffUtil(items, data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}