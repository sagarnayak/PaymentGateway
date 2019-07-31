package com.sagar.android.paymentgateway.model

import com.google.gson.annotations.SerializedName

data class CreateORderIdReq(
    val currency: String,
    val amount: String,
    @SerializedName("notes")
    val notesForOrderIdReq: NotesForOrderIdReq
)