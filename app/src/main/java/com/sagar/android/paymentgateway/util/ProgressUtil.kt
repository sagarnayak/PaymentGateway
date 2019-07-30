@file:Suppress("MemberVisibilityCanBePrivate")

package com.sagar.android.paymentgateway.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.sagar.android.paymentgateway.R


class ProgressUtil(private val context: Context) {
    private var dialog: Dialog? = null

    fun show() {
        hide()
        dialog = Dialog(context, R.style.progressBarTheme)

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog!!.setContentView(R.layout.progress_layout)
        dialog!!.setCancelable(false)
        dialog!!.show()

        UiUtil.hideSoftKeyboard(context)
    }

    fun hide() {
        if (dialog != null && dialog!!.isShowing())
            dialog!!.dismiss()
    }
}