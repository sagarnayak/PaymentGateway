package com.sagar.android.paymentgateway.model

import com.google.gson.annotations.SerializedName

data class CreateOrderIdReq(
    val currency: String,
    val amount: String,
    @SerializedName("notes")
    val notesForOrderIdReq: NotesForOrderIdReq
)