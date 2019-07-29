package com.sagar.android.paymentgateway.repository

import com.sagar.android.logutilmaster.LogUtil
import com.sagar.android.paymentgateway.repository.retrofit.ApiInterface

class Repository(var apiInterface: ApiInterface, var logUtil: LogUtil) {
}