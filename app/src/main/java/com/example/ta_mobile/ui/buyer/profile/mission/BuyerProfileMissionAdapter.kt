package com.example.ta_mobile.ui.buyer.profile.mission

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerMissionResponseData
import com.example.ta_mobile.databinding.ItemMissionCardLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerProfileMissionAdapter(val listener : () -> Unit) : RecyclerView.Adapter<BuyerProfileMissionAdapter.BuyerProfileMissionViewHolder>(){
    private var items = emptyList<BuyerMissionResponseData>()
    class BuyerProfileMissionViewHolder(val binding : ItemMissionCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : BuyerMissionResponseData){
            binding.itemMissionTitle.text = "${item.pointReward} Points"
            binding.itemMissionSubtitle.text = item.description
            binding.itemMissionDescription.text = "Current total transactions: ${item.curentTransaction}/${item.transactionNeeded}"

            if(item.curentTransaction >= item.transactionNeeded){
                binding.itemMissionTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemMissionSubtitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemMissionDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerProfileMissionViewHolder {
        return BuyerProfileMissionViewHolder(ItemMissionCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerProfileMissionViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener { listener() }
    }
    fun setData(data : List<BuyerMissionResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}