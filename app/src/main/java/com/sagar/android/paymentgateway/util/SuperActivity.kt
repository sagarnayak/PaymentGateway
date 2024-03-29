@file:Suppress("LeakingThis")

package com.sagar.android.paymentgateway.util

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

@Suppress("unused")
@SuppressLint("Registered")
open class SuperActivity : AppCompatActivity() {

    private val dialogUtil: DialogUtil = DialogUtil(this)

    private val progressUtil: ProgressUtil = ProgressUtil(this)

    fun isConnectedToNetwork() = NetworkUtil.isConnected(this)

    fun showProgress() {
        progressUtil.show()
    }

    fun hideProgress() {
        progressUtil.hide()
    }

    fun showMessageInDialog(message: String) {
        dialogUtil.showMessage(
            message = message
        )
    }
}