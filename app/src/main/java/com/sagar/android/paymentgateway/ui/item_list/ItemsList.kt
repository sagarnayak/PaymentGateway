package com.sagar.android.paymentgateway.ui.item_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.sagar.android.paymentgateway.R
import kotlinx.android.synthetic.main.activity_items_list.*

class ItemsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)
        setSupportActionBar(toolbar)

        var checkout=Checkout()

        checkout.setKeyID("sagarnayak")
    }

}
