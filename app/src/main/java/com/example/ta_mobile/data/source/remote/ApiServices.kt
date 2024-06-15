package com.example.ta_mobile.data.source.remote

import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.LoginForm
import com.example.ta_mobile.data.source.remote.model.auth.SellerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.UpdatePasswordForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.AddSellerRatingForm
import com.example.ta_mobile.data.source.remote.model.buyer.order.BuyerAddOrderForm
import com.example.ta_mobile.data.source.remote.model.seller.GetReportForm
import com.example.ta_mobile.data.source.remote.model.seller.notification.SendNotificationForm
import com.example.ta_mobile.data.source.remote.model.seller.product.SetProductVisibilityForm
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.data.source.remote.response.auth.UpdatePasswordResponse
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.AddFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.GetFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.RemoveFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.SearchProductResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerMissionResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.buyer.referral.BuyerInputReferralResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.data.source.remote.response.order.AddOrderResponse
import com.example.ta_mobile.data.source.remote.response.order.AddSellerRatingResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @Multipart
    @PUT("user/seller/update")
    suspend fun sellerUpdateUserProfile(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part("location") location: RequestBody,
        @Part("operational_hour") operationalHour: RequestBody,
    ): Response<SellerUpdateProfileResponse>

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

    @GET("user/find-nearest-store")
    suspend fun getNearestStore(
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Query("radius") radius: Double
    ): Response<NearestStoreResponse>


    //buyer
    @PUT("user/buyer/update-referral-filled/{referral}")
    suspend fun inputReferral(
        @Path("referral") referral: String
    ): Response<BuyerInputReferralResponse>

    @GET("user/search-seller")
    suspend fun searchStore(
        @Query("keyword") keyword: String
    ): Response<SearchStoreResponse>

    @POST("user/buyer/add-fav-store/{storeId}")
    suspend fun addFavouriteStore(
        @Path("storeId") storeId: String
    ): Response<AddFavouriteStoreResponse>

    @DELETE("user/buyer/remove-fav-store/{favId}")
    suspend fun removeFavouriteStore(
        @Path("favId") storeId: String
    ): Response<RemoveFavouriteStoreResponse>

    @GET("user/buyer/get-fav-store")
    suspend fun getFavouriteStore(
    ): Response<GetFavouriteStoreResponse>

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

    //report
    @POST("user/seller/get-best-selling-product")
    suspend fun getBestSellingProduct(
        @Body getReportForm: GetReportForm
    ): Response<BestSellingProductResponse>

    @POST("user/seller/get-total-income")
    suspend fun getTotalIncome(
        @Body getReportForm: GetReportForm
    ): Response<TotalIncomeResponse>


    //order
    @POST("user/seller/send-notification")
    suspend fun sendNotification(
        @Body body : SendNotificationForm
    ): Response<SendNotificationResponse>

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

    @PUT("transaction/update-order-rating")
    suspend fun addOrderRating(
        @Body body : AddSellerRatingForm
    ): Response<AddSellerRatingResponse>


    //product
    @GET("product/get-my-product")
    suspend fun getMyProduct(
    ): Response<SellerGetMyProductResponse>

    @GET("product/get-single-product/{productId}")
    suspend fun getSingleProduct(
        @Path("productId") productId: String
    ): Response<SellerGetMySingleProductResponse>

    @GET("product/search-by-category/")
    suspend fun searchProductByCategory(
        @Query("keyword") keyword: String
    ): Response<SearchProductResponse>

    @GET("product/search-by-discount/")
    suspend fun searchProductByDiscount(
        @Query("discount") discount: String
    ): Response<SearchProductResponse>

    @Multipart
    @POST("product/add")
    suspend fun addProduct(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("original_price") originalPrice: RequestBody,
        @Part("stock_count") stockCount: RequestBody,
        @Part("rack_position") rackPosition: RequestBody,
        @Part("production_date") productionDate: RequestBody,
        @Part("expire_date") expireDate: RequestBody,
    ): Response<SellerAddProductResponse>

    @Multipart
    @PUT("product/update/{productId}")
    suspend fun editProduct(
        @Path("productId") productId: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("original_price") originalPrice: RequestBody,
        @Part("stock_count") stockCount: RequestBody,
        @Part("rack_position") rackPosition: RequestBody,
        @Part("production_date") productionDate: RequestBody,
        @Part("expire_date") expireDate: RequestBody,
    ): Response<SellerEditProductResponse>

    @PUT("product/edit-visible")
    suspend fun setProductVisibility(
        @Body body : SetProductVisibilityForm
    ): Response<SellerSetVisibilityProductResponse>

    @DELETE("product/delete/{productId}")
    suspend fun deleteProduct(
        @Path("productId") productId: String
    ): Response<SellerDeleteProductResponse>





}