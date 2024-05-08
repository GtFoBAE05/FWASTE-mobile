package com.example.ta_mobile.ui.buyer.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BuyerProductViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _productDetailResult = MutableLiveData<NetworkResult<ProductDetailResponse>>()
    val productDetailResult: LiveData<NetworkResult<ProductDetailResponse>> = _productDetailResult



    fun getProductDetail(productId : String){
        viewModelScope.launch {
            buyerRepository.getProductDetail(productId).collect{
                _productDetailResult.postValue(it)
            }
        }
    }


    fun checkIfStoreValid(storeId:String):LiveData<Boolean>{
        var result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = buyerRepository.getCartProduct()
            data.collectLatest{
                if (it.isEmpty()){
                    result.value = true
                }else if(it.first().storeId == storeId){
                    result.value = true
                }else{
                    result.value = false
                }
            }
        }
        return result
    }

    fun insertCartProduct(productId:String, storeId: String, storeName:String, imageUrl:String, name:String, price:Double, amount:Int){
        viewModelScope.launch {
            buyerRepository.insertToCartProduct(CartProductEntity(
                productId = productId,
                storeId = storeId,
                storeName = storeName,
                imageUrl = imageUrl,
                name = name,
                price = price,
                amountPurchase = amount
            ))
        }
    }

    fun clearCartProduct(){
        viewModelScope.launch {
            buyerRepository.deleteAll()
        }
    }

}