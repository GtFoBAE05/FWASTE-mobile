package com.example.ta_mobile.ui.buyer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.databinding.ItemStoreNearBuyerCardLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerHomeStoreNearBuyerAdapter(private val listener : (SearchStoreData) -> Unit) : RecyclerView.Adapter<BuyerHomeStoreNearBuyerAdapter.BuyerHomeStoreNearBuyerViewHolder>() {
    private var items = emptyList<SearchStoreData>()

    class BuyerHomeStoreNearBuyerViewHolder(val binding : ItemStoreNearBuyerCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SearchStoreData){
            Glide.with(binding.root).load(item.imageUrl).into(binding.storeNearBuyerCardImageView)
            binding.storeNearBuyerCardStoreName.setText(item.fullname)
            binding.storeNearBuyerCardStoreAddress.setText(item.address)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyerHomeStoreNearBuyerViewHolder {
        return BuyerHomeStoreNearBuyerViewHolder(ItemStoreNearBuyerCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerHomeStoreNearBuyerViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    fun setData(data : List<SearchStoreData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}