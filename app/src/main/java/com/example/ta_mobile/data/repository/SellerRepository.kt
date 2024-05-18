package com.example.ta_mobile.data.repository

import android.util.Log
import com.example.ta_mobile.data.source.remote.ApiServices
import com.example.ta_mobile.data.source.remote.model.seller.GetReportForm
import com.example.ta_mobile.data.source.remote.model.seller.notification.SendNotificationForm
import com.example.ta_mobile.data.source.remote.model.seller.product.SetProductVisibilityForm
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.RejectOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.seller.notification.SendNotificationResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerAddProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerDeleteProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerEditProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMySingleProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerSetVisibilityProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.profile.SellerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.seller.report.BestSellingProductResponse
import com.example.ta_mobile.data.source.remote.response.seller.report.TotalIncomeResponse
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

class SellerRepository(
    private val apiServices: ApiServices,
) {

    suspend fun getBestSellingProduct(startMonth: Int = 0, endMonth: Int = 0, startYear: Int = 0, endYear: Int = 0): Flow<NetworkResult<BestSellingProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val getBestSellingProductResponse = apiServices.getBestSellingProduct(
                    GetReportForm(
                        startMonth,
                        endMonth,
                        startYear,
                        endYear
                    )
                )
                if (getBestSellingProductResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            getBestSellingProductResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    getBestSellingProductResponse.errorBody()?.let {
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

    suspend fun getTotalIncome(startMonth: Int = 0, endMonth: Int = 0, startYear: Int = 0, endYear: Int = 0): Flow<NetworkResult<TotalIncomeResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val totalIncomeResponse = apiServices.getTotalIncome(
                    GetReportForm(
                        startMonth,
                        endMonth,
                        startYear,
                        endYear
                    )
                )
                if (totalIncomeResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            totalIncomeResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    totalIncomeResponse.errorBody()?.let {
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

    suspend fun updateSellerProfile(
        fileImage: MultipartBody.Part,
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        location: String,
        operationalHour : String
    ): Flow<NetworkResult<SellerUpdateProfileResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerUpdateProfileResponse = apiServices.sellerUpdateUserProfile(
                    fileImage,
                    name.toRequestBody("text/plain".toMediaType()),
                    email.toRequestBody("text/plain".toMediaType()),
                    phoneNumber.toRequestBody("text/plain".toMediaType()),
                    address.toRequestBody("text/plain".toMediaType()),
                    location.toRequestBody("text/plain".toMediaType()),
                    operationalHour.toRequestBody("text/plain".toMediaType())
                )
                if (sellerUpdateProfileResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            sellerUpdateProfileResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerUpdateProfileResponse.errorBody()?.let {
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

    suspend fun getSingleProduct(productId: String): Flow<NetworkResult<SellerGetMySingleProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerGetMySingleProductResponse = apiServices.getSingleProduct(
                    productId
                )
                if (sellerGetMySingleProductResponse.isSuccessful) {
                    emit(NetworkResult.Success(
                            sellerGetMySingleProductResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerGetMySingleProductResponse.errorBody()?.let {
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

    suspend fun addProduct(
        fileImage: MultipartBody.Part,
        name: String,
        category: String,
        description: String,
        price: String,
        originalPrice : String,
        stockCount: String,
        rackPosition: String,
    ): Flow<NetworkResult<SellerAddProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerAddProductResponse = apiServices.addProduct(
                    fileImage,
                    name.toRequestBody("text/plain".toMediaType()),
                    category.toRequestBody("text/plain".toMediaType()),
                    description.toRequestBody("text/plain".toMediaType()),
                    price.toRequestBody("text/plain".toMediaType()),
                    originalPrice.toRequestBody("text/plain".toMediaType()),
                    stockCount.toRequestBody("text/plain".toMediaType()),
                    rackPosition.toRequestBody("text/plain".toMediaType()),
                )
                if (sellerAddProductResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            sellerAddProductResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerAddProductResponse.errorBody()?.let {
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

    suspend fun editProduct(
        productId: String,
        fileImage: MultipartBody.Part,
        name: String,
        category: String,
        description: String,
        price: String,
        originalPrice: String,
        stockCount: String,
        rackPosition: String,
    ): Flow<NetworkResult<SellerEditProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerEditProductResponse = apiServices.editProduct(
                    productId,
                    fileImage,
                    name.toRequestBody("text/plain".toMediaType()),
                    category.toRequestBody("text/plain".toMediaType()),
                    description.toRequestBody("text/plain".toMediaType()),
                    price.toRequestBody("text/plain".toMediaType()),
                    originalPrice.toRequestBody("text/plain".toMediaType()),
                    stockCount.toRequestBody("text/plain".toMediaType()),
                    rackPosition.toRequestBody("text/plain".toMediaType()),
                )
                if (sellerEditProductResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            sellerEditProductResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerEditProductResponse.errorBody()?.let {
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

    suspend fun setProductVisibility(
        productId: String,
        status: Boolean,
    ): Flow<NetworkResult<SellerSetVisibilityProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerSetproductVisibilityResponse = apiServices.setProductVisibility(
                    SetProductVisibilityForm(
                        productId,
                        status
                    )
                )
                if (sellerSetproductVisibilityResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            sellerSetproductVisibilityResponse.body()
                                ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    sellerSetproductVisibilityResponse.errorBody()?.let {
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

    suspend fun deleteProduct(productId: String): Flow<NetworkResult<SellerDeleteProductResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sellerDeleteProductReponse = apiServices.deleteProduct(
                    productId
                )
                if (sellerDeleteProductReponse.isSuccessful) {
                    emit(NetworkResult.Success(
                        sellerDeleteProductReponse.body()
                            ?: throw NoDataException("No data found")
                    )
                    )
                } else {
                    sellerDeleteProductReponse.errorBody()?.let {
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

    suspend fun sendNotification(title:String, message:String, productId: String): Flow<NetworkResult<SendNotificationResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val sendNotificationResponse = apiServices.sendNotification(
                    SendNotificationForm(
                        title, message, productId
                    )
                )
                if (sendNotificationResponse.isSuccessful) {
                    emit(NetworkResult.Success(
                        sendNotificationResponse.body()
                            ?: throw NoDataException("No data found")
                    )
                    )
                } else {
                    sendNotificationResponse.errorBody()?.let {
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