@file:Suppress("LeakingThis")

package com.sagar.android.paymentgateway.util

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class SuperActivity : AppCompatActivity() {

    private var dialogUtil: DialogUtil = DialogUtil(this)

    private var progressUtil: ProgressUtil = ProgressUtil(this)

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