package com.example.ta_mobile.ui.buyer.profile.point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponseDataVoucherReward
import com.example.ta_mobile.databinding.ItemVoucherLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerProfilePointListAdapter : RecyclerView.Adapter<BuyerProfilePointListAdapter.BuyerProfilePointListViewHolder>(){
    private var items = emptyList<BuyerPointResponseDataVoucherReward>()
    class BuyerProfilePointListViewHolder(val binding : ItemVoucherLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : BuyerPointResponseDataVoucherReward){
            binding.buyerCheckoutVoucherListTitle.text = item.title
            binding.buyerCheckoutVoucherListSubtitle.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerProfilePointListViewHolder {
        return BuyerProfilePointListViewHolder(ItemVoucherLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerProfilePointListViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
    }
    fun setData(data : List<BuyerPointResponseDataVoucherReward>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}