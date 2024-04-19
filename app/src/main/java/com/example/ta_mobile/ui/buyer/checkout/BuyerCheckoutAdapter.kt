package com.example.ta_mobile.ui.buyer.checkout

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.databinding.ItemCartProductCardLayoutBinding
import com.example.ta_mobile.databinding.ItemProductCheckoutCardLayoutBinding
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerCheckoutAdapter() : RecyclerView.Adapter<BuyerCheckoutAdapter.BuyerCheckoutHolder>(){
    private var items = emptyList<CartProductEntity>()
    class BuyerCheckoutHolder(val binding : ItemProductCheckoutCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CartProductEntity){
            Glide.with(binding.root).load(item.imageUrl).into(binding.itemProductCheckoutImageView)
            binding.itemProductCheckoutAmountTv.text = "x${item.amountPurchase}"
            binding.itemProductCheckoutName.text = item.name
            binding.itemProductCheckoutPrice.text = CurrencyHelper.convertToRupiah(item.price.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerCheckoutHolder {
        return BuyerCheckoutHolder(ItemProductCheckoutCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerCheckoutHolder, position: Int) {
        var item = items[position]
        holder.bind(item)

    }
    fun setData(data : List<CartProductEntity>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}