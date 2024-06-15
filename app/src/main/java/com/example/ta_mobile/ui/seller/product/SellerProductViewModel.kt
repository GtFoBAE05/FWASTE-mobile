package com.example.ta_mobile.ui.seller.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.SellerRepository
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerAddProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerDeleteProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerEditProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMySingleProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerSetVisibilityProductResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SellerProductViewModel(private val sellerRepository: SellerRepository) : ViewModel() {
    private var _mySingleProductData = MutableLiveData<NetworkResult<SellerGetMySingleProductResponse>>()
    val mySingleProductData: LiveData<NetworkResult<SellerGetMySingleProductResponse>> = _mySingleProductData

    private var _myProductData = MutableLiveData<NetworkResult<SellerGetMyProductResponse>>()
    val myProductData: LiveData<NetworkResult<SellerGetMyProductResponse>> = _myProductData

    private var _setProductVisibility = MutableLiveData<NetworkResult<SellerSetVisibilityProductResponse>>()
    val setProductVisibility: LiveData<NetworkResult<SellerSetVisibilityProductResponse>> = _setProductVisibility

    private var _deleteProductResponse = MutableLiveData<NetworkResult<SellerDeleteProductResponse>>()
    val deleteProductResponse: LiveData<NetworkResult<SellerDeleteProductResponse>> = _deleteProductResponse

    private var _addProductResponse = MutableLiveData<NetworkResult<SellerAddProductResponse>>()
    val addProductResponse: LiveData<NetworkResult<SellerAddProductResponse>> =
        _addProductResponse

    private var _editProductResponse = MutableLiveData<NetworkResult<SellerEditProductResponse>>()
    val editProductResponse: LiveData<NetworkResult<SellerEditProductResponse>> =
        _editProductResponse

    fun getSingleProduct(id: String) {
        viewModelScope.launch {
            sellerRepository.getSingleProduct(id).collect {
                _mySingleProductData.postValue(it)
            }
        }
    }

    fun getMyProduct() {
        viewModelScope.launch {
            sellerRepository.getMyProduct().collect {
                _myProductData.postValue(it)
            }
        }
    }

    fun addProduct(
        fileImage: MultipartBody.Part,
        name: String,
        category: String,
        description: String,
        price: String,
        originalPrice : String,
        stockCount: String,
        rackPosition: String,
        productionDate:String,
        expiredDate:String,
    ) {
        viewModelScope.launch {
            sellerRepository.addProduct(
                fileImage,
                name,
                category,
                description,
                price,
                originalPrice,
                stockCount,
                rackPosition,
                productionDate,
                expiredDate
            ).collect {
                _addProductResponse.postValue(it)
            }
        }
    }

    fun editProduct(
        productId: String,
        fileImage: MultipartBody.Part,
        name: String,
        category: String,
        description: String,
        price: String,
        originalPrice : String,
        stockCount: String,
        rackPosition: String,
        productionDate:String,
        expiredDate:String,
    ) {
        viewModelScope.launch {
            sellerRepository.editProduct(
                productId,
                fileImage,
                name,
                category,
                description,
                price,
                originalPrice,
                stockCount,
                rackPosition,
                productionDate,
                expiredDate
            ).collect {
                _editProductResponse.postValue(it)
            }
        }
    }

    fun setMyProductVisibility(
        productId: String,
        visibility: Boolean,
    ) {
        viewModelScope.launch {
            sellerRepository.setProductVisibility(productId, visibility).collect {
                _setProductVisibility.postValue(it)
            }
        }
    }

    fun deleteProduct(
        productId: String,
    ) {
        viewModelScope.launch {
            sellerRepository.deleteProduct(productId).collect {
                _deleteProductResponse.postValue(it)
            }
        }
    }


}