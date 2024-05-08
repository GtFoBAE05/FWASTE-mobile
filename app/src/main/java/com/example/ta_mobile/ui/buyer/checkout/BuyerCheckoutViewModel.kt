package com.example.ta_mobile.ui.buyer.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderFormProduct
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponseData
import com.example.ta_mobile.data.source.remote.response.order.AddOrderResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerCheckoutViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {

    private var _paymentMetod = MutableLiveData<String>("cash")
    val paymentMethhod: LiveData<String> = _paymentMetod

    private var _deliveryMethod = MutableLiveData<String>("pickup")
    val deliveryMethod: LiveData<String> = _deliveryMethod

    private var _subtotalPrice = MutableLiveData<Int>(0)
    val subtotalPrice: LiveData<Int> = _subtotalPrice

    private var _shippingFee = MutableLiveData<Int>(0)
    val shippingFee: LiveData<Int> = _shippingFee

    private var _orderFee = MutableLiveData<Int>(2000)
    val orderFee: LiveData<Int> = _orderFee

    private var _totalPrice = MutableLiveData<Int>(0)
    val totalPrice: LiveData<Int> = _totalPrice

    private var _userVoucherList = MutableLiveData<NetworkResult<UserOwnedVoucherResponse>>()
    val userVoucherList: LiveData<NetworkResult<UserOwnedVoucherResponse>> = _userVoucherList

    private var _selectedVoucher = MutableLiveData<UserOwnedVoucherResponseData?>()
    val selectedVoucher: LiveData<UserOwnedVoucherResponseData?> = _selectedVoucher

    private var _addOrderResult = MutableLiveData<NetworkResult<AddOrderResponse>>()
    val addOrderResult: LiveData<NetworkResult<AddOrderResponse>> = _addOrderResult

    var storeId = ""

    init {
        viewModelScope.launch {
            buyerRepository.getCartProduct().collect {
                if(it.isNotEmpty()){
                    storeId = it.first().storeId
                }
            }
        }
    }

    fun getProductCart() = buyerRepository.getCartProduct().asLiveData()

    fun updatePaymentDelivery(payment: String, delivery: String) {
        _paymentMetod.value = payment
        _deliveryMethod.value = delivery
    }

    fun calculateTotal() {
        val subtotal = _subtotalPrice.value ?: 0
        val shippingFee = _shippingFee.value ?: 0
        val orderFee = _orderFee.value ?: 0
        val selectedVoucher = _selectedVoucher.value

        var total = subtotal + shippingFee + orderFee

        selectedVoucher?.let {
            total -= it.amount
        }

        _totalPrice.value = total
    }


    fun getUserOwnedVoucher() {
        viewModelScope.launch {
            buyerRepository.getUserOwnedVoucher().collect {
                _userVoucherList.postValue(it)
            }
        }
    }

    fun setSelectedVoucher(voucher: UserOwnedVoucherResponseData) {
        _selectedVoucher.value = voucher
    }

    fun setSubtotal(data: Int) {
        _subtotalPrice.value = data
    }

    fun setShippingFee(data: Int) {
        _shippingFee.value = data
    }

    fun clearSelectedVoucher() {
        _selectedVoucher.postValue(null)
    }

    fun addOrder() {
        viewModelScope.launch {
            val productList: MutableList<BuyerAddOrderFormProduct> = mutableListOf()

            buyerRepository.getCartProduct().collect { cartProducts ->
                if (cartProducts.isNotEmpty()) { // Periksa apakah cartProducts tidak kosong
                    storeId = cartProducts.first().storeId
                    val buyerProducts = cartProducts.map {
                        BuyerAddOrderFormProduct(
                            productId = it.productId,
                            quantity = it.amountPurchase,
                            unitPrice = it.price.toInt(),
                            productSubtotal = it.amountPurchase * it.price.toInt()
                        )
                    }
                    productList.addAll(buyerProducts)

                    buyerRepository.addOrder(
                        BuyerAddOrderForm(
                            storeId = storeId,
                            deliveryFee = _shippingFee.value ?: 0,
                            serviceFee = 2000,
                            userVoucherUsedId = _selectedVoucher.value?.id ?: "",
                            dicountAmount = _selectedVoucher.value?.amount ?: 0,
                            subtotal = _subtotalPrice.value ?: 0,
                            shippingMethod = _deliveryMethod.value ?: "",
                            paymentMethod = _paymentMetod.value ?: "",
                            totalAmount = _totalPrice.value ?: 0,
                            product = productList

                        )
                    ).collect {
                        _addOrderResult.value = it
                    }
                } else {
                    // Lakukan penanganan kesalahan atau logika lain jika cartProducts kosong
                }
            }
        }
    }


    fun clearData() {
        viewModelScope.launch {
            buyerRepository.deleteAll()
        }
    }


}