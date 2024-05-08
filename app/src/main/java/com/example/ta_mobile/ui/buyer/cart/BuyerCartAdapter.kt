package com.example.ta_mobile.ui.buyer.cart

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.databinding.ItemCartProductCardLayoutBinding
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DiffUtil

class BuyerCartAdapter(private val listener : (CartProductEntity) -> Unit,  private val viewModel : BuyerCartViewModel) : RecyclerView.Adapter<BuyerCartAdapter.BuyerCartViewHolder>(){
    private var items = emptyList<CartProductEntity>()
    class BuyerCartViewHolder(val binding : ItemCartProductCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CartProductEntity){
            Glide.with(binding.root).load(item.imageUrl).into(binding.itemCartProductImageView)
            binding.itemCartProductAmountCountTv.text = item.amountPurchase.toString()
            binding.itemCartProductProductName.text = item.name
            binding.itemCartProductProductPrice.text = CurrencyHelper.convertToRupiah(item.price.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerCartViewHolder {
        return BuyerCartViewHolder(ItemCartProductCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BuyerCartViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
        holder.binding.itemCartProductProductMinusBtn.setOnClickListener {
            if((item.amountPurchase-1) ==0){
                AlertDialog.Builder(holder.binding.root.context)
                    .setTitle("Are you sure want to delete this item?")
                    .setPositiveButton(
                        "Yes"
                    ) { dialogInterface, i ->
                        viewModel.removeProduct(item.productId)
                    }
                    .setNegativeButton(
                        "No"
                    ){dialogInterface, i ->
                        dialogInterface.cancel()
                    }.show()
            }else{
                viewModel.updateProductAmount(item.productId, (item.amountPurchase-1))
            }
        }

        holder.binding.itemCartProductProductPlus.setOnClickListener {
            viewModel.updateProductAmount(item.productId, (item.amountPurchase+1))
        }
    }
    fun setData(data : List<CartProductEntity>){
        val diffUtil = DiffUtil(items,data)
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

}