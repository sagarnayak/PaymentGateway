package com.sagar.android.paymentgateway.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.sagar.android.paymentgateway.core.KeyWordsAndConstants

class SharedPreferenceModule(application: Application) {
    var sharedpreference: SharedPreferences = application.getSharedPreferences(
        KeyWordsAndConstants.SHARED_PREF_DB,
        Context.MODE_PRIVATE
    )
}