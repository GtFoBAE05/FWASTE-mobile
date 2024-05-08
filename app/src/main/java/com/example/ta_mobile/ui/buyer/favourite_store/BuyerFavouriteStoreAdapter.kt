package com.example.ta_mobile.ui.buyer.favourite_store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.GetFavouriteStoreResponseData
import com.example.ta_mobile.databinding.StoreInfoMapCardLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil
import com.example.ta_mobile.utils.helper.EmojiHelper

class BuyerFavouriteStoreAdapter(private val listener : (GetFavouriteStoreResponseData) -> Unit) : RecyclerView.Adapter<BuyerFavouriteStoreAdapter.BuyerFavouriteStoreViewHolder>() {
    private var items = emptyList<GetFavouriteStoreResponseData>()

    class BuyerFavouriteStoreViewHolder(val binding : StoreInfoMapCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : GetFavouriteStoreResponseData){
            Glide.with(binding.root).load(item.imageUrl).into(binding.sellerInfoImageView)
            binding.sellerInfoName.text = item.fullname
            binding.sellerInfoAddress.text = item.address
            binding.sellerInfoRating.text = EmojiHelper.getEmoji(0x2605) + item.rating.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyerFavouriteStoreViewHolder {
        return BuyerFavouriteStoreViewHolder(StoreInfoMapCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerFavouriteStoreViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    fun setData(data : List<GetFavouriteStoreResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}