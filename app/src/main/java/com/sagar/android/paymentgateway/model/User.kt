package com.sagar.android.paymentgateway.model

data class User(
    var _id: String,
    var name: String,
    var email: String
) {
    constructor() : this("", "", "")
}