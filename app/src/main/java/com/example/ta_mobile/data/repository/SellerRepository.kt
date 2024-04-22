package com.example.ta_mobile.data.repository

import android.util.Log
import com.example.ta_mobile.data.source.remote.ApiServices
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.RejectOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponse
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.exception.NoDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class SellerRepository(
    private val apiServices: ApiServices,
) {

    suspend fun getOrderByStatus(keyword: String): Flow<NetworkResult<OrderStatusResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val buyerOrderStatusResponse = apiServices.sellerGetOrderByStatus(
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
                Log.e("SellerRepository", "HttpException: " + e.message)
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
                Log.e("SellerRepository", "HttpException: " + e.message)
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
                Log.e("SellerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun rejectOrderByTransaction(transactionId: String): Flow<NetworkResult<RejectOrderStatusResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val rejectOrderResponse = apiServices.rejectOrderByTransaction(
                    transactionId
                )
                if (rejectOrderResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            rejectOrderResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    rejectOrderResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("SellerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMyProduct(): Flow<NetworkResult<SellerGetMyProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerGetMyProductResponse = apiServices.getMyProduct(
                )
                if (sellerGetMyProductResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            sellerGetMyProductResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerGetMyProductResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("SellerRepository", "HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}