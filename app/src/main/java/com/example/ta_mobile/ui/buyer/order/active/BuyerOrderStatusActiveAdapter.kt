package com.example.ta_mobile.ui.buyer.order.active

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.databinding.ItemCartProductCardLayoutBinding
import com.example.ta_mobile.ui.buyer.cart.BuyerCartViewModel
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerOrderStatusActiveAdapter(private val listener : (CartProductEntity) -> Unit) : RecyclerView.Adapter<BuyerOrderStatusActiveAdapter.BuyerOrderStatusActiveViewHolder>(){
    private var items = emptyList<CartProductEntity>()
    class BuyerOrderStatusActiveViewHolder(val binding : ItemCartProductCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CartProductEntity){
            Glide.with(binding.root).load(item.imageUrl).into(binding.itemCartProductImageView)
            binding.itemCartProductAmountCountTv.text = item.amountPurchase.toString()
            binding.itemCartProductProductName.text = item.name
            binding.itemCartProductProductPrice.text = CurrencyHelper.convertToRupiah(item.price.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerOrderStatusActiveViewHolder {
        return BuyerOrderStatusActiveViewHolder(ItemCartProductCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerOrderStatusActiveViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }

    }
    fun setData(data : List<CartProductEntity>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}