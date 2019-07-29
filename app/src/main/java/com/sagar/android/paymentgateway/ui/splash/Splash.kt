package com.sagar.android.paymentgateway.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sagar.android.paymentgateway.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class Splash : AppCompatActivity(), KodeinAware {

    override val kodein by org.kodein.di.android.kodein()

    private val viewModelProvider: SplashViewModelProvider by instance()
    private lateinit var binding: com.sagar.android.paymentgateway.databinding.ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSupportActionBar(toolbar)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash
        )
        binding.context = this
    }
}
