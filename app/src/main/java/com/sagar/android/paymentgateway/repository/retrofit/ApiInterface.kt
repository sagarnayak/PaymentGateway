package com.sagar.android.paymentgateway.repository.retrofit

import com.sagar.android.paymentgateway.model.LoginRequest
import com.sagar.android.paymentgateway.model.SignUpRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
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
}