package com.example.ta_mobile.ui.seller.home.incoming_order

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.response.order.BuyerOrderDetailResponseDataProduct
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreData
import com.example.ta_mobile.databinding.ItemCartProductCardLayoutBinding
import com.example.ta_mobile.databinding.ItemProductCheckoutCardLayoutBinding
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class SellerIncomingOrderAdapter() : RecyclerView.Adapter<SellerIncomingOrderAdapter.SellerIncomingOrderViewHolder>(){
    private var items = emptyList<BuyerOrderDetailResponseDataProduct>()
    class SellerIncomingOrderViewHolder(val binding : ItemProductCheckoutCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : BuyerOrderDetailResponseDataProduct){
            Glide.with(binding.root).load(item.productImageUrl).into(binding.itemProductCheckoutImageView)
            binding.itemProductCheckoutAmountTv.text = "x${item.quantity}"
            binding.itemProductCheckoutName.text = item.productName
            binding.itemProductCheckoutPrice.text = CurrencyHelper.convertToRupiah(item.subtotal.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerIncomingOrderViewHolder {
        return SellerIncomingOrderViewHolder(ItemProductCheckoutCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SellerIncomingOrderViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)

    }
    fun setData(data : List<BuyerOrderDetailResponseDataProduct>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}