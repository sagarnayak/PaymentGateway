@file:Suppress("unused")

package com.sagar.android.paymentgateway.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.sagar.android.paymentgateway.R


class DialogUtil(private var context: Context) {

    var customDialog: Dialog? = null

    interface DialogWithOneButtonCallBack {
        fun dialogCancelled()

        fun buttonClicked()
    }

    interface DialogWithMessageCallBack {
        fun dialogCancelled()

        fun buttonClicked()
    }

    fun showDialogWithOneButtonAndCallBack(
        message: String,
        cancellable: Boolean,
        dialogWithOneButtonCallBack: DialogWithOneButtonCallBack
    ) {
        if (customDialog != null && customDialog!!.isShowing)
            customDialog!!.dismiss()
        customDialog = Dialog(context)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_with_single_button)

        customDialog!!.window
            ?.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        customDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val textViewMessage = customDialog!!.findViewById<TextView>(R.id.text_view_message)
        val buttonAction = customDialog!!.findViewById<TextView>(R.id.button_action)

        textViewMessage.text = message

        customDialog!!.setCancelable(cancellable)

        buttonAction.setOnClickListener {
            dialogWithOneButtonCallBack.buttonClicked()
            customDialog!!.dismiss()
        }
        customDialog!!.setOnCancelListener { dialogWithOneButtonCallBack.dialogCancelled() }

        customDialog!!.show()
    }

    fun showDialogWithMessage(
        message: String
    ) {
        if (customDialog != null && customDialog!!.isShowing)
            customDialog!!.dismiss()
        customDialog = Dialog(context)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_with_single_button)

        customDialog!!.window
            ?.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        customDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val textViewMessage = customDialog!!.findViewById<TextView>(R.id.text_view_message)
        val buttonAction = customDialog!!.findViewById<TextView>(R.id.button_action)
        textViewMessage.text = message
        buttonAction.setOnClickListener {
            customDialog!!.dismiss()
        }
        customDialog!!.setCancelable(true)
        customDialog!!.show()
    }
}
