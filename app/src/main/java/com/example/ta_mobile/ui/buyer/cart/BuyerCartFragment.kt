package com.example.ta_mobile.ui.buyer.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerCartBinding
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.android.ext.android.inject

class BuyerCartFragment : Fragment() {

    private var _binding : FragmentBuyerCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyerCartViewModel by inject()

    private lateinit var adapter: BuyerCartAdapter

    private var price:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerProductDetailToolbar.setTitle("Cart")
        viewModel.getProductCart()
        setupButton()
        setupAdapter()
        setupObserver()
    }

    private fun setupButton(){
        binding.buyerCartCheckoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_buyerCartFragment_to_buyerCheckoutFragment)
        }
    }

    private fun setupAdapter(){
        binding.buyerCartRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerCartAdapter({
            val bundle = Bundle()
            bundle.putString("productId", it.productId)
            findNavController().navigate(R.id.action_buyerCartFragment_to_buyerProductFragment, bundle)
        }, viewModel)

        binding.buyerCartRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.getProductCart().observe(viewLifecycleOwner){
            price = 0
            adapter.setData(it)
            if(it.isEmpty()){
                binding.BuyerCartStoreName.gone()
                binding.bottomAppBar.gone()
            }else{
                binding.BuyerCartStoreName.visible()
                binding.bottomAppBar.visible()
                binding.BuyerCartStoreName.text = it.first().storeName
                it.forEach {
                    price = (price + (it.price * it.amountPurchase)).toInt()
                }
                binding.buyerCartTotalPrice.text = CurrencyHelper.convertToRupiah(price)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}