package com.sagar.android.paymentgateway.model

data class VerifyPaymentReq(
    val orderId: String,
    val paymentId: String,
    val signature: String
)