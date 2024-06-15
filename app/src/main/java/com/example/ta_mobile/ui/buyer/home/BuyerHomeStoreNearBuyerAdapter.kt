package com.example.ta_mobile.ui.buyer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponseData
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.databinding.ItemStoreNearBuyerCardLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerHomeStoreNearBuyerAdapter(private val listener : (NearestStoreResponseData) -> Unit) : RecyclerView.Adapter<BuyerHomeStoreNearBuyerAdapter.BuyerHomeStoreNearBuyerViewHolder>() {
    private var items = emptyList<NearestStoreResponseData>()

    class BuyerHomeStoreNearBuyerViewHolder(val binding : ItemStoreNearBuyerCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : NearestStoreResponseData){
            Glide.with(binding.root).load(item.imageUrl).into(binding.storeNearBuyerCardImageView)
            binding.storeNearBuyerCardStoreName.text = item.fullname
            binding.storeNearBuyerCardStoreAddress.text = item.address
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

    fun setData(data : List<NearestStoreResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}