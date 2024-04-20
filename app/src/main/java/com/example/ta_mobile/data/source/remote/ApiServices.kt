package com.example.ta_mobile.data.source.remote

import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.LoginForm
import com.example.ta_mobile.data.source.remote.model.auth.SellerRegisterForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.AddOrderResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.UpdateOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    //auth
    @POST("user/login")
    suspend fun login(
        @Body loginForm: LoginForm
    ): Response<LoginResponse>

    @POST("user/buyer/register")
    suspend fun registerAsBuyer(
        @Body buyerRegisterForm: BuyerRegisterForm
    ): Response<RegisterResponse>

    @POST("user/seller/register")
    suspend fun registerAsSeller(
        @Body sellerRegisterForm: SellerRegisterForm
    ): Response<RegisterResponse>


    //buyer
    @GET("user/search-seller")
    suspend fun searchStore(
        @Query("keyword") keyword:String
    ) : Response<SearchStoreResponse>

    @GET("product/get-store-product/{storeId}")
    suspend fun getStoreDetail(
        @Path("storeId") storeId:String
    ) : Response<StoreDetailResponse>

    @GET("product/get-single-product/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId:String
    ) : Response<ProductDetailResponse>

    @GET("voucher/user-owned-voucher")
    suspend fun getUserOwnedVoucher(
    ) : Response<UserOwnedVoucherResponse>

    @POST("transaction/add")
    suspend fun addOrder(
        @Body addOrderForm: BuyerAddOrderForm
    ) : Response<AddOrderResponse>

    @GET("transaction/search-by-status")
    suspend fun getOrderByStatus(
        @Query("status") keyword:String
    ) : Response<BuyerOrderStatusResponse>

    @GET("transaction/order-detail/{transactionId}")
    suspend fun getOrderDetailByTransaction(
        @Path("transactionId") transactionId:String
    ) : Response<BuyerOrderDetailResponse>

    @PUT("transaction/update-order-status/{transactionId}")
    suspend fun updateOrderStatusByTransaction(
        @Path("transactionId") transactionId:String
    ) : Response<UpdateOrderStatusResponse>
}