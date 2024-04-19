package com.example.ta_mobile.ui.buyer.checkout.voucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponseData
import com.example.ta_mobile.databinding.ItemVoucherLayoutBinding
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerCheckoutVoucherListAdapter(private val listener : (UserOwnedVoucherResponseData) -> Unit) : RecyclerView.Adapter<BuyerCheckoutVoucherListAdapter.BuyerCheckoutVOucherListHolder>(){
    private var items = emptyList<UserOwnedVoucherResponseData>()
    class BuyerCheckoutVOucherListHolder(val binding : ItemVoucherLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : UserOwnedVoucherResponseData){
            binding.buyerCheckoutVoucherListTitle.text = item.title
            binding.buyerCheckoutVoucherListSubtitle.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerCheckoutVOucherListHolder {
        return BuyerCheckoutVOucherListHolder(ItemVoucherLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerCheckoutVOucherListHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }

    }
    fun setData(data : List<UserOwnedVoucherResponseData>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}