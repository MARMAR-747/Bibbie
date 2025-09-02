package com.example.handy_shopping.retrofit

import com.example.handy_shopping.Carrello.CarrelloItem
import com.example.handy_shopping.Acquisti.AcquistoItem
import com.example.handy_shopping.Home.ProdottoItem
import com.example.handy_shopping.DettagliProdotto.ReviewItem
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
//1234ABcd
//1234Abcd
interface UserAPI {
    @POST("/pwm/register")
    fun register(@Body registerRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/login")
    fun login(@Body loginRequest: JsonObject): Call<JsonObject>

    @GET("/pwm/profile")
    fun getProfile(@Query("user_id") userId: Int): Call<JsonObject>

    @POST("/pwm/update_profile")
    fun updateUserProfile(@Body updateRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/update_card")
    fun updateCardProfile(@Body updateRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/update_address")
    fun updateAddressProfile(@Body updateRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/update_premium")
    fun updatePremium(@Body updateRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/check_email")
    fun checkEmailExists(@Body emailRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/update_password")
    fun updatePassword(@Body passwordRequest: JsonObject): Call<JsonObject>

    @POST("/pwm/add_to_cart")
    fun aggiungiAlCarrello(@Body request: JsonObject): Call<JsonObject>

    @GET("/pwm/cart/{user_id}")
    fun getCarrello(@Path("user_id") userId: Int): Call<List<CarrelloItem>>

    @POST("/pwm/delete_cart_item")
    fun deleteCarrelloItem(@Body request: JsonObject): Call<JsonObject>

    @POST("/pwm/update_cart_item_quantity")
    fun updateCartItemQuantity(@Body request: JsonObject): Call<JsonObject>

    @POST("/pwm/add_order")
    fun addOrder(@Body request: JsonObject): Call<JsonObject>

    @GET("/pwm/get_orders/{user_id}")
    fun getOrders(@Path("user_id") userId: Int): Call<List<AcquistoItem>>

    @POST("/pwm/delete_order")
    fun deleteOrder(@Body request: JsonObject): Call<JsonObject>

    @POST("/pwm/verify_password_and_delete_account")
    fun verifyPasswordAndDeleteAccount(@Body request: JsonObject): Call<JsonObject>

    @GET("/pwm/get_discounted_products")
    fun getDiscountedProducts(): Call<List<ProdottoItem>>

    @GET("/pwm/get_product_price/{idprodotto}")
    fun getProductPrice(@Path("idprodotto") idprodotto: Int): Call<JsonObject>

    @GET("/pwm/get_product_details/{id}")
    fun getProductDetails(@Path("id") prodottoId: Int): Call<ProdottoItem>

    @GET("/pwm/product_reviews/{idprodotto}")
    fun getProductReviews(@Path("idprodotto") idprodotto: Int): Call<List<ReviewItem>>

    @POST("/pwm/submit_review")
    fun submitReview(@Body review: JsonObject): Call<JsonObject>

    @POST("/pwm/delete_review")
    fun deleteReview(@Body review: JsonObject): Call<JsonObject>

    @POST("/pwm/update_review")
    fun updateReview(@Body review: JsonObject): Call<JsonObject>

    @POST("/pwm/has_purchased_product")
    fun hasPurchasedProduct(@Body purchaseCheck: JsonObject): Call<JsonObject>

    @POST("/pwm/has_reviewed_product")
    fun hasReviewedProduct(@Body reviewCheck: JsonObject): Call<JsonObject>


    @GET("/pwm/search_products")
    fun searchProducts(
        @Query("query") query: String,
        @Query("categories") categories: List<String>?,
        @Query("min_price") minPrice: Float?,
        @Query("max_price") maxPrice: Float?
    ): Call<List<ProdottoItem>>

    companion object {
        const val BASE_URL = "http://192.168.178.36:9000/"
        const val USER_URI = "pwm"
    }
}
