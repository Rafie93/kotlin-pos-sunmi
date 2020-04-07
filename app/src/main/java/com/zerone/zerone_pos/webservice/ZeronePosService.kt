package com.zerone.zerone_pos.webservice
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import com.zerone.zerone_pos.ui.home.HomeModel
import com.zerone.zerone_pos.ui.kategori.KategoriModel
import com.zerone.zerone_pos.ui.login.LoginResponse
import com.zerone.zerone_pos.ui.produk.ProdukModel
import com.zerone.zerone_pos.ui.register.RegisterResponse
import com.zerone.zerone_pos.ui.transaction.TransactionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ZeronePosService {
    @FormUrlEncoded
    @POST("login?")
    fun login(
        @Field("email")email:String,
        @Field("password")password:String
        ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register?")
    fun register(
        @Field("name")name:String,
        @Field("email")email:String,
        @Field("phone")phone:String,
        @Field("password")password:String,
        @Field("password_confirmation")password_confirmation:String,
        @Field("merchant_name")merchant_name:String,
        @Field("business_type")business_type:String,
        @Field("address")address:String,
        @Field("city")city:String
        ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("otp_resend?")
    fun otpResend(
        @Field("email")email:String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("otp_verify?")
    fun otpVerify(
        @Field("code")code:String,
        @Field("email")email:String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("changepassword?")
    fun changePassword(
        @Header("Authorization")auth:String,
        @Field("password_old")p_old:String,
        @Field("password_new")p_new:String,
        @Field("password_confirmation")p_confir:String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("changeprofile?")
    fun changeProfile(
        @Header("Authorization")auth:String,
        @Field("name")name:String,
        @Field("phone")phone:String
    ): Call<ResponseBody>


    @Multipart
    @POST("changeprofile?")
    fun changeProfileWithImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @PartMap text: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ResponseBody>

    @GET("dashboard?")
    fun dashboard(
        @Header("Authorization")auth:String
    ): Call<HomeModel>

    @FormUrlEncoded
    @POST("category/store?")
    fun categoryCreate(
        @Header("Authorization")auth:String,
        @Field("category")category:String
    ): Response<ResponseBody>

    @GET("category?")
    suspend fun getCategory(
        @Header("Authorization")auth:String,
        @Query("keyword") keyword: String
    ): Response<KategoriModel.KategoriResponse>


    @GET("product?")
    suspend fun getProduk(
        @Header("Authorization")auth:String,
        @Query("kategori") kategori: String,
        @Query("keyword") keyword: String
    ): Response<ProdukModel.ProdukResponse>

    @GET("transaction?")
    suspend fun getTransaction(
        @Header("Authorization")auth:String,
        @Query("invoice") invoice: String
    ): Response<TransactionModel.TransactionResponse>

    @FormUrlEncoded
    @POST("transaction/store?")
     fun storeTransaction(
        @Header("Authorization")auth:String,
        @Field("invoice")invoice: String,
        @Field("date")date:String,
        @Field("time")time:String,
        @Field("total_product")total_product:String,
        @Field("total_sales")total_sales:String,
        @Field("additional")additional:String,
        @Field("tax")tax:String,
        @Field("discount")discount:String,
        @Field("grand_total")grand_total:String,
        @Field("pay")pay:String,
        @Field("change")change:String,
        @Field("note")note:String,
        @Field("product_transaction")product_transaction:String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("product/store?")
    fun storeProduct(
        @Header("Authorization")auth:String,
        @Field("code")code: String,
        @Field("name")name:String,
        @Field("basic_price")basic_price:String,
        @Field("selling_price")selling_price:String,
        @Field("is_stock")is_stock:Int,
        @Field("unit")unit:String,
        @Field("category")category:String,
        @Field("stock")stock:String,
        @Field("description")description:String
    ): Call<ResponseBody>

    @Multipart
    @POST("product/store?")
    fun storeProductWithImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @PartMap text: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("product/{id}/update")
    fun updateProduct(
        @Header("Authorization")auth:String,
        @Field("code")code: String,
        @Field("name")name:String,
        @Field("basic_price")basic_price:String,
        @Field("selling_price")selling_price:String,
        @Field("is_stock")is_stock:Int,
        @Field("unit")unit:String,
        @Field("category")category:String,
        @Field("stock")stock:String,
        @Field("description")description:String,
        @Path("id")id: String
    ): Call<ResponseBody>

    @Multipart
    @POST("product/{id}/update")
    fun updateProductWithImage(
        @Header("Authorization") authorization: String,
        @Path("id")id:String,
        @Part file: MultipartBody.Part,
        @PartMap text: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ResponseBody>


    @POST("product/{id}/delete")
    fun deleteProduk(
        @Header("Authorization")auth:String,
        @Path("id")id: String
    ): Call<ResponseBody>
}