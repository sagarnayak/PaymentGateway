package com.sagar.android.paymentgateway.repository.retrofit

import com.sagar.android.paymentgateway.model.CreateORderIdReq
import com.sagar.android.paymentgateway.model.LoginRequest
import com.sagar.android.paymentgateway.model.SignUpRequest
import com.sagar.android.paymentgateway.model.VerifyPaymentReq
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("signUp")
    fun signup(
        @Body signUpRequest: SignUpRequest
    ): Observable<Response<ResponseBody>>

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Observable<Response<ResponseBody>>

    @POST("logout")
    fun logout(
        @Header("Authorization") authHeader: String
    ): Observable<Response<ResponseBody>>

    @GET("razorPayKey")
    fun getRazorPayKey(
        @Header("Authorization") authHeader: String
    ): Observable<Response<ResponseBody>>

    @POST("createOrder")
    fun createOrderId(
        @Header("Authorization") authHeader: String,
        @Body reqBody: CreateORderIdReq
    ): Observable<Response<ResponseBody>>

    @POST("verifyPayment")
    fun verifyPayment(
        @Header("Authorization") authHeader: String,
        @Body verifyPaymentReq: VerifyPaymentReq
    ): Observable<Response<ResponseBody>>
}