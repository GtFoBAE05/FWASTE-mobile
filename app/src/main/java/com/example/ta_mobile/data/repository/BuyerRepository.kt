package com.example.ta_mobile.data.repository

import android.util.Log
import com.example.ta_mobile.data.source.local.db.dao.CartProductDao
import com.example.ta_mobile.data.source.local.db.entity.CartProductEntity
import com.example.ta_mobile.data.source.remote.ApiServices
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponse
import com.example.ta_mobile.data.source.remote.response.order.AddOrderResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerMissionResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.exception.NoDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException

class BuyerRepository(
    private val apiServices: ApiServices,
    private val cartProductDao: CartProductDao
) {

    fun getCartProduct(): Flow<List<CartProductEntity>> {
        return cartProductDao.getCartProduct()
    }

    suspend fun insertToCartProduct(data: CartProductEntity) {
        cartProductDao.insert(data)
    }

    suspend fun updateCartProductAmount(productId: String, amount: Int) {
        cartProductDao.updateCartProductAmount(productId, amount)
    }

    suspend fun deleteByProductId(productId: String) {
        cartProductDao.deleteByProductId(productId)
    }

    suspend fun deleteAll() {
        cartProductDao.deleteAll()
    }

    suspend fun searchStore(keyword: String): Flow<NetworkResult<SearchStoreResponse>> {
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

    suspend fun getNearestStore(latitude: Double, longitude: Double, radius: Double): Flow<NetworkResult<NearestStoreResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val nearestStoreResponse = apiServices.getNearestStore(
                    latitude, longitude, radius
                )
                if (nearestStoreResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            nearestStoreResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    nearestStoreResponse.errorBody()?.let {
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

    suspend fun getStoreDetail(storeId: String): Flow<NetworkResult<StoreDetailResponse>> {
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

    suspend fun getProductDetail(productId: String): Flow<NetworkResult<ProductDetailResponse>> {
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
                            userOwnedVoucherResponse.body()
                                ?: throw NoDataException("No data found")
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

    suspend fun addOrder(data: BuyerAddOrderForm): Flow<NetworkResult<AddOrderResponse>> {
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

    suspend fun getOrderByStatus(keyword: String): Flow<NetworkResult<OrderStatusResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerOrderStatusResponse = apiServices.getOrderByStatus(
                    keyword
                )
                if (buyerOrderStatusResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerOrderStatusResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerOrderStatusResponse.errorBody()?.let {
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

    suspend fun getOrderDetailByTransaction(transactionId: String): Flow<NetworkResult<OrderDetailResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerOrderDetailResponse = apiServices.getOrderDetailByTransaction(
                    transactionId
                )
                if (buyerOrderDetailResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerOrderDetailResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerOrderDetailResponse.errorBody()?.let {
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

    suspend fun updateOrderStatusDetailByTransaction(transactionId: String): Flow<NetworkResult<UpdateOrderStatusResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerUpdateOrderStatusResponse = apiServices.updateOrderStatusByTransaction(
                    transactionId
                )
                if (buyerUpdateOrderStatusResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerUpdateOrderStatusResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerUpdateOrderStatusResponse.errorBody()?.let {
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

    suspend fun getBuyerPointDetail(): Flow<NetworkResult<BuyerPointResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerPointResponse = apiServices.getBuyerPointDetail()
                if (buyerPointResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerPointResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerPointResponse.errorBody()?.let {
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

    suspend fun getBuyerMission(): Flow<NetworkResult<BuyerMissionResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerMissionResponse = apiServices.getBuyerMission()
                if (buyerMissionResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerMissionResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerMissionResponse.errorBody()?.let {
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

    suspend fun updateBuyerProfile(
        fileImage: MultipartBody.Part,
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        location: String
    ): Flow<NetworkResult<BuyerUpdateProfileResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerUpdateProfileResponse = apiServices.buyerUpdateUserProfile(
                    fileImage,
                    name.toRequestBody("text/plain".toMediaType()),
                    email.toRequestBody("text/plain".toMediaType()),
                    phoneNumber.toRequestBody("text/plain".toMediaType()),
                    address.toRequestBody("text/plain".toMediaType()),
                    location.toRequestBody("text/plain".toMediaType())
                )
                if (buyerUpdateProfileResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            buyerUpdateProfileResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    buyerUpdateProfileResponse.errorBody()?.let {
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