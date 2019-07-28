package com.sagar.android.paymentgateway.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sagar.android.paymentgateway.R
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSupportActionBar(toolbar)
    }

}
