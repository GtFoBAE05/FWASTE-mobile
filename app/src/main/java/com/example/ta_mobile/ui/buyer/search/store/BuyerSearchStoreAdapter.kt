package com.example.ta_mobile.ui.buyer.search.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.databinding.ItemSearchStoreCardLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil
import com.example.ta_mobile.utils.helper.EmojiHelper

class BuyerSearchStoreAdapter(private val listener: (SearchStoreData) -> Unit) :
    RecyclerView.Adapter<BuyerSearchStoreAdapter.BuyerSearchStoreViewHolder>() {
    private var items = emptyList<SearchStoreData>()

    class BuyerSearchStoreViewHolder(val binding: ItemSearchStoreCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchStoreData) {
            Glide.with(binding.root).load(item.imageUrl).into(binding.SearchStoreCardImageView)
            binding.SearchStoreCardStoreName.text = item.fullname
            binding.SearchStoreCardStoreRating.text = EmojiHelper.getEmoji(0x2605) + item.rating.toString()
            binding.SearchStoreCardStoreAddress.text = item.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerSearchStoreViewHolder {
        return BuyerSearchStoreViewHolder(
            ItemSearchStoreCardLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BuyerSearchStoreViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(data: List<SearchStoreData>) {
        val diffUtil = DiffUtil(items, data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }



}


