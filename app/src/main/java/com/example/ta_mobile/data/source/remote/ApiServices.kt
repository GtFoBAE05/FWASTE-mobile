package com.example.ta_mobile.data.source.remote

import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.LoginForm
import com.example.ta_mobile.data.source.remote.model.auth.SellerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.UpdatePasswordForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponse
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.data.source.remote.response.order.AddOrderResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerMissionResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponse
import com.example.ta_mobile.data.source.remote.response.auth.UpdatePasswordResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.data.source.remote.response.order.RejectOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("user/detail/")
    suspend fun GetUserDetail(
    ): Response<UserDetailResponse>

    @Multipart
    @PUT("user/buyer/update")
    suspend fun buyerUpdateUserProfile(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part("location") location: RequestBody,
    ): Response<BuyerUpdateProfileResponse>

    @PUT("user/password/update")
    suspend fun updatePassword(
        @Body updatePasswordForm : UpdatePasswordForm
    ): Response<UpdatePasswordResponse>

    //common
    @GET("level/detail")
    suspend fun getBuyerPointDetail(
    ): Response<BuyerPointResponse>

    @GET("mission/available-mission")
    suspend fun getBuyerMission(
    ): Response<BuyerMissionResponse>


    //buyer
    @GET("user/search-seller")
    suspend fun searchStore(
        @Query("keyword") keyword: String
    ): Response<SearchStoreResponse>

    @GET("product/get-store-product/{storeId}")
    suspend fun getStoreDetail(
        @Path("storeId") storeId: String
    ): Response<StoreDetailResponse>

    @GET("product/get-single-product/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: String
    ): Response<ProductDetailResponse>

    @GET("voucher/user-owned-voucher")
    suspend fun getUserOwnedVoucher(
    ): Response<UserOwnedVoucherResponse>

    @POST("transaction/add")
    suspend fun addOrder(
        @Body addOrderForm: BuyerAddOrderForm
    ): Response<AddOrderResponse>


    //order
    @GET("transaction/search-by-status")
    suspend fun getOrderByStatus(
        @Query("status") keyword: String
    ): Response<OrderStatusResponse>

    @GET("transaction/seller-search-by-status")
    suspend fun sellerGetOrderByStatus(
        @Query("status") keyword: String
    ): Response<OrderStatusResponse>

    @GET("transaction/order-detail/{transactionId}")
    suspend fun getOrderDetailByTransaction(
        @Path("transactionId") transactionId: String
    ): Response<OrderDetailResponse>

    @PUT("transaction/update-order-status/{transactionId}")
    suspend fun updateOrderStatusByTransaction(
        @Path("transactionId") transactionId: String
    ): Response<UpdateOrderStatusResponse>

    @PUT("transaction/reject/{transactionId}")
    suspend fun rejectOrderByTransaction(
        @Path("transactionId") transactionId: String
    ): Response<RejectOrderStatusResponse>

    @GET("product/get-my-product")
    suspend fun getMyProduct(
    ): Response<SellerGetMyProductResponse>



}