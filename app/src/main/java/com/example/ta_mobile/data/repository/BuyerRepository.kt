package com.example.ta_mobile.data.repository

import android.util.Log
import com.example.ta_mobile.data.source.local.db.dao.CartProductDao
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.ApiServices
import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.AddOrderResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.exception.NoDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class BuyerRepository(
    private val apiServices: ApiServices,
    private val cartProductDao : CartProductDao
) {

    fun getCartProduct() :Flow<List<CartProductEntity>>{
        return cartProductDao.getCartProduct()
    }

    suspend fun insertToCartProduct(data:CartProductEntity){
        cartProductDao.insert(data)
    }

    suspend fun updateCartProductAmount(productId: String, amount:Int){
        cartProductDao.updateCartProductAmount(productId, amount)
    }

    suspend fun deleteByProductId(productId: String){
        cartProductDao.deleteByProductId(productId)
    }

    suspend fun deleteAll(){
        cartProductDao.deleteAll()
    }

    suspend fun searchStore(keyword:String): Flow<NetworkResult<SearchStoreResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val searchStoreResponse = apiServices.searchStore(
                    keyword
                )
                if (searchStoreResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            searchStoreResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    searchStoreResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("BuyerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStoreDetail(storeId:String): Flow<NetworkResult<StoreDetailResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val storeDetailResponse = apiServices.getStoreDetail(
                    storeId
                )
                if (storeDetailResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            storeDetailResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    storeDetailResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("BuyerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductDetail(productId:String): Flow<NetworkResult<ProductDetailResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val productDetailResponse = apiServices.getProductDetail(
                    productId
                )
                if (productDetailResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            productDetailResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    productDetailResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("BuyerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserOwnedVoucher(): Flow<NetworkResult<UserOwnedVoucherResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val userOwnedVoucherResponse = apiServices.getUserOwnedVoucher()
                if (userOwnedVoucherResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            userOwnedVoucherResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    userOwnedVoucherResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("BuyerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addOrder(data : BuyerAddOrderForm): Flow<NetworkResult<AddOrderResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val addOrderResponse = apiServices.addOrder(
                    data
                )
                if (addOrderResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            addOrderResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    addOrderResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("BuyerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}