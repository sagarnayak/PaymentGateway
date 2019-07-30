package com.sagar.android.paymentgateway.model

data class UserData(
    var user: User,
    var token: String
) {
    constructor() : this(User(), "")
}